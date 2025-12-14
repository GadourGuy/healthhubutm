package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.*;
import com.secj3303.model.Program;

@Controller
@RequestMapping("/trainer") 
public class TrainerController {

    @Autowired
    private ProgramDaoHibernate programDaoHibernate;
    @Autowired
    private UserDao userDao;

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "trainer-dashboard"; // Routes to trainer-dashboard.html
    }

    // --- VIEW ASSIGNED MEMBERS ---
    @GetMapping("/members")
    public String listMembers() {
        return "trainer-member-list"; // Routes to trainer-member-list.html
    }

    @GetMapping("/assign-program")
    public String listMembers() {
        return "trainer-member-list"; // Routes to trainer-member-list.html
    }

    // --- CREATE FITNESS PLAN ---
    @GetMapping("/create-plan")
    public String showPlanForm(HttpSession session) {

        String role = (String) session.getAttribute("role");
        if (role == null || (!"trianer".equals(role))) {
            return "redirect:/login";
        }
        
        return "trainer-plan-form";
    }

    @PostMapping("/create-plan")
    public String createPlan(@RequestParam String name, @RequestParam(required = false) String description, @RequestParam Integer durationWeeks, @RequestParam Double monthlyFee, HttpSession session) {

        String role = (String) session.getAttribute("role");
        if (role == null || (!"trianer".equals(role))) {
            return "redirect:/login";
        }
        
        try {
  
            Program program = new Program();
            program.setName(name);
            program.setDescription(description != null ? description : "");
            program.setDurationWeeks(durationWeeks);
            program.setMonthlyFee(monthlyFee);

            programDaoHibernate.save(program);

            return "redirect:/trainer/members";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/trainer/members";
        }
    }
}