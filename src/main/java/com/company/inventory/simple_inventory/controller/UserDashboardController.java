package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.dto.UserReadOnlyDTO;
import com.company.inventory.simple_inventory.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserService userService;

    @GetMapping("/user/dashboard")
    public String showUserDashboard(Model model, HttpSession session) {

        UserReadOnlyDTO loggedUser = (UserReadOnlyDTO) session.getAttribute("user");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedUser);
        return "user-dashboard";
    }
}
