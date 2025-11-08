package com.company.inventory.simple_inventory.model.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        // Redirect to logout confirmation page
        return "logout";
    }
}
