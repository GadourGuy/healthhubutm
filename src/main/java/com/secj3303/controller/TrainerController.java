package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainer") 
public class TrainerController {

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

    // --- CREATE FITNESS PLAN ---
    @GetMapping("/create-plan")
    public String showPlanForm() {
        
        return "trainer-plan-form"; // Routes to trainer-plan-form.html
    }

    @PostMapping("/create-plan")
    public String createPlan() {
        // Logic to save the plan will go here
        return "redirect:/trainer/members"; // Redirect back to member list
    }
}