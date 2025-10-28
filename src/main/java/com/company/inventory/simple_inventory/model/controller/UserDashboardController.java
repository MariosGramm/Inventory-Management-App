package com.company.inventory.simple_inventory.model.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDashboardController {

    @GetMapping("/user/dashboard")
    public String showUserDashboard() {
        return "user-dashboard";
    }
}
