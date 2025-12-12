package com.secj3303.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin-dashboard"; // Routes to admin-dashboard.html
    }

    // --- MANAGE PROGRAMS ---
    @GetMapping("/programs")
    public String listPrograms() {
        return "admin-program-list"; // Routes to admin-program-list.html
    }

    @GetMapping("/programs/add")
    public String showAddProgramForm(Model model) {
        return "admin-program-form"; // Routes to admin-program-form.html
    }

    @PostMapping("/programs/save")
    public String saveProgram() {
        // DB logic to save program will go here
        return "redirect:/admin/programs"; // Redirect back to list after save
    }

    @GetMapping("/programs/delete") 
    public String deleteProgram() {
        // DB logic to delete will go here
        return "redirect:/admin/programs";
    }

    // --- REPORTS ---
    @GetMapping("/reports")
    public String showReports() {
        return "admin-reports"; // Routes to admin-reports.html
    }
}