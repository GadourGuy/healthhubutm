package com.secj3303.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.UserDaoHibernate; 
import com.secj3303.dao.UserProgramDao;
import com.secj3303.model.Program;
import com.secj3303.model.User;
import com.secj3303.model.UserProgram;

@Controller
@RequestMapping("/trainer") 
public class TrainerController {

    @Autowired
    private ProgramDao programDao;
    @Autowired
    private UserDaoHibernate userDao;
    @Autowired
    private UserProgramDao userProgramDao;

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/login";

        List<Program> programs = programDao.findAll();
        
        // Count only MEMBERS for the dashboard stat
        List<User> allUsers = userDao.findAll();
        long memberCount = allUsers.stream()
                .filter(u -> "MEMBER".equalsIgnoreCase(u.getRole()))
                .count();

        model.addAttribute("programCount", programs.size());
        model.addAttribute("memberCount", memberCount); 

        return "trainer-dashboard"; 
    }

    // --- MANAGE PLANS ---
    @GetMapping("/plans")
    public String listPlans(HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/login";

        List<Program> programs = programDao.findAll();
        model.addAttribute("programs", programs);
        
        return "trainer-plans"; 
    }

    @GetMapping("/plans/create")
    public String showPlanForm(HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/login";
        model.addAttribute("program", new Program()); 
        return "trainer-plan-form";
    }

    @GetMapping("/plans/edit")
    public String showEditPlanForm(@RequestParam Integer id, HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/login";
        Program program = programDao.findById(id);
        model.addAttribute("program", program);
        return "trainer-plan-form"; 
    }

    @PostMapping("/plans/save")
    public String savePlan(
            @RequestParam(required = false) Integer id, 
            @RequestParam String name, 
            @RequestParam(required = false) String description, 
            @RequestParam Integer durationWeeks, 
            @RequestParam Double monthlyFee, 
            HttpSession session) {
        
        if (!isTrainer(session)) return "redirect:/login";

        try {
            Program program;
            if (id != null && id > 0) {
                program = programDao.findById(id); 
            } else {
                program = new Program(); 
            }
            
            program.setName(name);
            program.setDescription(description != null ? description : "");
            program.setDurationWeeks(durationWeeks);
            program.setMonthlyFee(monthlyFee);
            
            programDao.save(program);

            return "redirect:/trainer/plans"; 
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/trainer/plans?error=true";
        }
    }

    // --- MEMBER DIRECTORY ---
    @GetMapping("/members")
    public String listMembers(HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/login";
        
        List<User> allUsers = userDao.findAll();
        List<User> membersOnly = new ArrayList<>();
        
        // Filter the list in Java
        for (User user : allUsers) {
            if ("MEMBER".equalsIgnoreCase(user.getRole())) {
                membersOnly.add(user);
            }
        }
        
        model.addAttribute("users", membersOnly);
        
        return "trainer-member-list";
    }

    // --- MEMBER DETAILS ---
    @GetMapping("/member-details")
    public String viewMemberDetails(@RequestParam Integer userId, HttpSession session, Model model) {
        if (!isTrainer(session)) return "redirect:/login";

        User user = userDao.findById(userId);
        List<UserProgram> enrollments = userProgramDao.findByUserId(userId);
        
        model.addAttribute("user", user);
        model.addAttribute("enrollments", enrollments);
        
        return "trainer-member-details";
    }

    private boolean isTrainer(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && "TRAINER".equals(role);
    }
}