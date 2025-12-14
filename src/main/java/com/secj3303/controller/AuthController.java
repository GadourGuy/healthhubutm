package com.secj3303.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.UserDaoHibernate;
import com.secj3303.model.User;

@Controller
public class AuthController {

@Autowired
private UserDaoHibernate userDaoJdbc;

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
    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        if (session.getAttribute("email") != null) {
            return "redirect:/member/dashboard";
        }
        return "register";
    }

    

    @PostMapping("/register")
    public String processRegister(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, HttpSession session) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=password_mismatch";
        }
        
        if (userDaoJdbc.emailExists(email)) {
            return "redirect:/register?error=email_exists";
        }
        
        try {
            User newUser = new User(firstName, lastName, email, password);

            newUser.setRole("MEMBER");
            
            userDaoJdbc.insert(newUser);

            User savedUser = userDaoJdbc.findByEmailAndPassword(email, password);
            session.setAttribute("email", savedUser.getEmail());
            session.setAttribute("role", savedUser.getRole());
            session.setAttribute("userId", savedUser.getId());
            
            return "redirect:/member/dashboard";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register?error=database_error";
        }
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