package com.secj3303.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.PersonDao;
import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.UserDaoHibernate;
import com.secj3303.dao.UserProgramDao;
import com.secj3303.model.BmiResultDto;
import com.secj3303.model.Person;
import com.secj3303.model.Program;
import com.secj3303.model.User;
import com.secj3303.model.UserProgram; 

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired  
    private PersonDao personDao;

    @Autowired 
    private ProgramDao programDao;

    @Autowired
    private UserDaoHibernate userDao; 

    @Autowired
    private UserProgramDao userProgramDao; 

    // ===========================
    //       DASHBOARD & BMI
    // ===========================

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"MEMBER".equals(role)) {
            return "redirect:/login";
        }

        // Retrieve BMI result from session (if it exists)
        Person currentPerson = (Person) session.getAttribute("currentPerson");
        
        if (currentPerson != null) {
            double heightM = currentPerson.getHeight() / 100.0;
            double bmi = currentPerson.getWeight() / (heightM * heightM);
            double bmiValue = Math.round(bmi * 10.0) / 10.0;
            String category = getBmiCategory(bmiValue);
            
            model.addAttribute("currentBmi", new BmiResultDto(currentPerson, bmiValue, category));
        }

        return "member-dashboard"; 
    }

    @GetMapping("/bmi")
    public String showBmiForm(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"MEMBER".equals(role)) {
            return "redirect:/login";
        }
        return "member-bmi-form"; 
    }

    @PostMapping("/bmi")
    public String calculateBmi(
            @RequestParam String name, 
            @RequestParam Integer yob, 
            @RequestParam Double height, 
            @RequestParam Double weight, 
            @RequestParam(required = false) String[] interests,
            HttpSession session) {

        // FIX: Constructor order is (name, yob, WEIGHT, HEIGHT, interests)
        Person person = new Person(name, yob, weight, height, interests);
        
        personDao.insert(person);
        session.setAttribute("currentPerson", person);
        
        return "redirect:/member/dashboard";
    }
    
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal Weight";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }

    // ===========================
    //    PROGRAMS & ENROLLMENT
    // ===========================
    
    // 1. Browse All Programs
    @GetMapping("/programs")
    public String listPrograms(Model model) {
        List<Program> allPrograms = programDao.findAll();
        model.addAttribute("programs", allPrograms);
        return "member-program-list";
    }
    
    // 2. View Program Details (New Feature)
    @GetMapping("/programs/view")
    public String viewProgramDetails(@RequestParam Integer programId, Model model) {
        Program program = programDao.findById(programId);
        if (program == null) {
            return "redirect:/member/programs";
        }
        model.addAttribute("program", program);
        return "member-program-details"; 
    }

    // 3. Enroll (Prevents Duplicates)
    @PostMapping("/programs/enroll")
    public String enrollInProgram(@RequestParam Integer programId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        // Check Duplicate
        if (userProgramDao.isEnrolled(userId, programId)) {
            // Redirect with error flag (optional: handle in HTML)
            return "redirect:/member/plans?error=already_enrolled";
        }

        User user = userDao.findById(userId); 
        Program program = programDao.findById(programId);

        if (user != null && program != null) {
            UserProgram enrollment = new UserProgram(user, program);
            userProgramDao.save(enrollment);
        }

        return "redirect:/member/plans";
    }
    
    // 4. Unenroll (Removes from DB)
    @PostMapping("/programs/unenroll")
    public String unenrollFromProgram(@RequestParam Integer enrollmentId) {
        // Delete by Enrollment ID to remove the specific record
        userProgramDao.deleteById(enrollmentId);
        return "redirect:/member/plans";
    }

    // 5. My Plans (View Registered)
    @GetMapping("/plans")
    public String showMyPlans(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        // Fetch only this user's programs
        List<UserProgram> myPrograms = userProgramDao.findByUserId(userId);
        
        model.addAttribute("userPrograms", myPrograms);
        return "member-my-plans";
    }
}