package com.secj3303.controller;


import javax.servlet.http.HttpSession;
import com.secj3303.dao.UserDaoJdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.model.*;

@Controller
public class AuthController {

@Autowired
private UserDaoJdbc userDaoJdbc;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
    String role = (String) session.getAttribute("role");
    if (role != null) {
        return redirectBasedOnRole(role);
    }
    return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session) { 
    
    User user = null; 
    
    try {
        user = userDaoJdbc.findByEmailAndPassword(email, password);
    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/login?error=true";
    }
    
    if (user != null && user.getId() != null) {
        session.setAttribute("email", user.getEmail());
        session.setAttribute("role", user.getRole());
        session.setAttribute("userId", user.getId());
        return redirectBasedOnRole(user.getRole());
    } else {
        return "redirect:/login?error=true";
    }   
}

    // --- LOGOUT ---
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    // --- REGISTER ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister() {
        return "redirect:/login";
    }

    //added to ease up on them things yeah
    private String redirectBasedOnRole(String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/admin-dashboard";
        }
        else if ("TRAINER".equalsIgnoreCase(role)) {
            return "redirect:/trainer-dashboard";
        }
        else if ("MEMBER".equalsIgnoreCase(role)) {
            return "redirect:/member-dashboard";
        }
        else {
            return "redirect:/login";
        }
    }
}