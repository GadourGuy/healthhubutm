package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "member-dashboard"; // Routes to member-dashboard.html
    }

    // --- BMI MODULE ---
    @GetMapping("/bmi")
    public String showBmiForm() {
        return "member-bmi-form"; // Routes to member-bmi-form.html
    }

    @PostMapping("/bmi")
    public String calculateBmi() {
        // Logic to calculate and save BMI will go here
        return "redirect:/member/dashboard";
    }

    // --- BROWSE PROGRAMS ---
    @GetMapping("/programs")
    public String listPrograms(Model model) {
        return "member-program-list"; // Routes to member-program-list.html
    }

    @PostMapping("/programs/enroll")
    public String enrollInProgram() {
        // Logic to enroll user will go here
        return "redirect:/member/plans";
    }

    // --- MY PERSONAL PLANS ---
    @GetMapping("/plans")
    public String showMyPlans() {
        return "member-my-plans"; // Routes to member-my-plans.html
    }
}