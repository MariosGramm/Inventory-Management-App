package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.dto.UserInsertDTO;
import com.company.inventory.simple_inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminAddUserController {

    private final UserService userService;

    @GetMapping("/admin/users/add")
    public String showAddUserForm(Model model){
        model.addAttribute("user", new UserInsertDTO());

        return "admin-user-add";
    }

    @PostMapping("/admin/users/add")
    public String addUser(UserInsertDTO dto, RedirectAttributes redirectAttributes) {
        try {
            userService.addUser(dto);
            redirectAttributes.addFlashAttribute("success", "User added successfully!");
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users/add";
    }

}
