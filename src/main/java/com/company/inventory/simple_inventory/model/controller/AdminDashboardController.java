package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.model.User;
import com.company.inventory.simple_inventory.service.IInventoryService;
import com.company.inventory.simple_inventory.service.IProductService;
import com.company.inventory.simple_inventory.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final IProductService productService;
    private final IInventoryService inventoryService;
    private final IUserService userService;

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model, Principal principal){

        String username = (principal != null) ? principal.getName() : "Guest";
        User user;

        long totalProducts = productService.countProducts();
        long totalUsers = userService.countUsers();
        long totalTransactions = inventoryService.countTransactions();
        String lastLogin;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            lastLogin = userService.getLastLoginByUsername(username).format(formatter);
        }catch (EntityNotFoundException e){
            lastLogin = "--";
        }

        model.addAttribute("username",username);
        model.addAttribute("totalProducts",totalProducts);
        model.addAttribute("totalTransactions",totalTransactions);
        model.addAttribute("totalUsers",totalUsers);
        model.addAttribute("lastLogin",lastLogin);

        return "admin-dashboard";




    }

}
