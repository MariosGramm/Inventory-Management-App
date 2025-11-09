package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.core.enums.Role;
import com.company.inventory.simple_inventory.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login-page";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password , Model model) {

        //User user = authService.authenticateUser(email,password)          To do

        //Hardcoded credentials for testing
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("admin123");
        adminUser.setRole(Role.ADMIN);

        User normalUser = new User();
        normalUser.setEmail("normaluser@example.com");
        normalUser.setPassword("normal123");
        normalUser.setRole(Role.USER);

        User loggedInUser = null;

        if(email.equals(adminUser.getEmail()) && password.equals(adminUser.getPassword())) {
            loggedInUser = adminUser;
        } else if(email.equals(normalUser.getEmail()) && password.equals(normalUser.getPassword())) {
            loggedInUser = normalUser;
        }

        if(loggedInUser == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login-page";
        }


        if(loggedInUser.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }
}






