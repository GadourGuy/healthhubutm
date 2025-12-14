package com.secj3303.controller;

import com.secj3303.dao.*;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.model.Person;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired  
    private PersonDao personDao;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"MEMBER".equals(role)) {
            return "redirect:/login";
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
    public String calculateBmi(@RequestParam String name, @RequestParam Integer yob, @RequestParam Double height, @RequestParam Double weight, @RequestParam(required = false) String[] interests) {

        Person person = new Person(name, yob, height, weight, interests);
        
        personDao.insert(person);
        
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