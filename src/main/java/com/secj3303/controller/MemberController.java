package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "member-dashboard";
    }

    @GetMapping("/bmi")
    public String showBmiForm() {
        return "member-bmi-form";
    }

    @PostMapping("/bmi")
    public String calculateBmi() {
        return "redirect:/member/dashboard";
    }

    @GetMapping("/programs")
    public String listPrograms(Model model) {
        return "member-program-list";
    }

    @PostMapping("/programs/enroll")
    public String enrollInProgram() {
        return "redirect:/member/plans";
    }

    @GetMapping("/plans")
    public String showMyPlans() {
        return "member-my-plans";
    }
}