package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session){
        Role userRole = (Role) session.getAttribute("role");

        if (userRole == null) return "redirect:/login";

        if (userRole == Role.ADMIN) return "redirect:/admin/dashboard";
        else return "redirect:/user/dashboard";
    }


}
