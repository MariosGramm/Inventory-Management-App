package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.service.IInventoryService;
import com.company.inventory.simple_inventory.service.IProductService;
import com.company.inventory.simple_inventory.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final IProductService productService;
    private final IInventoryService inventoryService;
    private final IUserService userService;     //TO DO

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model, Principal principal) {

        // Παίρνουμε το username από το session ή δίνουμε fallback
        String username = (principal != null) ? principal.getName() : "Guest";

        // Δυναμικά δεδομένα
        long totalProducts = productService.countProducts();
        long totalTransactions = inventoryService.countTransactions();
        long totalUsers = userService.countUsers();
        String lastLogin = userService.getLastLoginFor(username);

        // Στέλνουμε τα δεδομένα στο View
        model.addAttribute("username", username);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("lastLogin", lastLogin);

        return "admin-dashboard";
    }
}
