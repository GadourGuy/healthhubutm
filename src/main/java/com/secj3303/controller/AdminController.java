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

import com.secj3303.dao.ProgramDao;
import com.secj3303.dao.UserDaoHibernate;
import com.secj3303.model.Program;
import com.secj3303.model.User;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    @Autowired
    private ProgramDao programDao;
    
    @Autowired
    private UserDaoHibernate userDao;

    // ===========================
    //       1. DASHBOARD
    // ===========================
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        List<Program> programs = programDao.findAll();
        List<User> users = userDao.findAll();
        
        // Count specific roles for the dashboard stats
        long memberCount = users.stream().filter(u -> "MEMBER".equalsIgnoreCase(u.getRole())).count();
        long trainerCount = users.stream().filter(u -> "TRAINER".equalsIgnoreCase(u.getRole())).count();

        model.addAttribute("programCount", programs.size());
        model.addAttribute("memberCount", memberCount);
        model.addAttribute("trainerCount", trainerCount);

        return "admin-dashboard"; 
    }

    // ===========================
    //    2. USER MANAGEMENT
    // ===========================
    
    // List all users (Directory)
    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        List<User> users = userDao.findAll();
        model.addAttribute("users", users);
        return "admin-user-list"; 
    }
    
    // --- ADD TRAINER ---
    @GetMapping("/users/add-trainer")
    public String showAddTrainerForm(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin-trainer-form"; 
    }
    
    @PostMapping("/users/save-trainer")
    public String saveTrainer(@RequestParam String firstName, @RequestParam String lastName, 
                              @RequestParam String email, @RequestParam String password, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            User trainer = new User(firstName, lastName, email, password);
            trainer.setRole("TRAINER");
            userDao.insert(trainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/users";
    }

    // --- ADD MEMBER ---
    @GetMapping("/users/add-member")
    public String showAddMemberForm(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin-member-form"; 
    }

    @PostMapping("/users/save-member")
    public String saveMember(@RequestParam String firstName, @RequestParam String lastName, 
                              @RequestParam String email, @RequestParam String password, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            User member = new User(firstName, lastName, email, password);
            member.setRole("MEMBER"); 
            userDao.insert(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/users";
    }

    // --- DELETE USER ---
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            userDao.delete(id); 
            System.out.println("Successfully deleted user ID: " + id); 
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }

    // ===========================
    //    3. PROGRAM MANAGEMENT
    // ===========================
    @GetMapping("/programs")
    public String listPrograms(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        List<Program> programs = programDao.findAll();
        model.addAttribute("programs", programs);
        return "admin-program-list"; 
    }

    @GetMapping("/programs/add")
    public String showAddProgramForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("program", new Program()); 
        return "admin-program-form"; 
    }
    
    @GetMapping("/programs/edit")
    public String showEditProgramForm(@RequestParam Integer id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        Program program = programDao.findById(id);
        model.addAttribute("program", program);
        return "admin-program-form"; 
    }

    @PostMapping("/programs/save")
    public String saveProgram(@RequestParam(required=false) Integer id, @RequestParam String name, 
                              @RequestParam String description, @RequestParam Integer durationWeeks, 
                              @RequestParam Double monthlyFee, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            Program p;
            if (id != null && id > 0) {
                p = programDao.findById(id); 
            } else {
                p = new Program(); 
            }
            
            p.setName(name);
            p.setDescription(description);
            p.setDurationWeeks(durationWeeks);
            p.setMonthlyFee(monthlyFee);
            
            programDao.save(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "redirect:/admin/programs"; 
    }

    @PostMapping("/programs/delete") 
    public String deleteProgram(@RequestParam Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            programDao.delete(id); 
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/programs?error=delete_failed";
        }
        
        return "redirect:/admin/programs";
    }

    // ===========================
    //       4. REPORTS
    // ===========================
    @GetMapping("/reports")
    public String showReports(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        List<Program> programs = programDao.findAll();
        double totalPotentialRevenue = programs.stream().mapToDouble(Program::getMonthlyFee).sum();
        
        model.addAttribute("totalPrograms", programs.size());
        model.addAttribute("estRevenue", totalPotentialRevenue);
        
        return "admin-reports"; 
    }
    
    // Helper to secure admin routes
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }
}