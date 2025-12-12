package com.secj3303.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    
    // --- LOGIN ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Routes to login.html
    }

    @PostMapping("/login")
    public String processLogin() {
        return "";
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
}