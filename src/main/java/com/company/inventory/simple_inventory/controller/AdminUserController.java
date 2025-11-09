package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.UserReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.UserSearchDTO;
import com.company.inventory.simple_inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String showUserManagementPage(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "false") boolean showDeleted,
                                         Model model){
        Page<UserReadOnlyDTO> userPage;

        if (showDeleted) {
            userPage = userService.getPaginatedUsers(page, size);
        }else {
            userPage = userService.getPaginatedNotDeletedUsers(page, size);
        }

        model.addAttribute("users",userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",userPage.getTotalPages());
        model.addAttribute("totalTransactions", userPage.getTotalElements());
        model.addAttribute("showDeleted", showDeleted);

        return "admin-users";
    }

    @GetMapping("/admin/users/delete/{uuid}")
    public String deleteUser(@PathVariable String uuid, RedirectAttributes redirectAttributes) throws EntityNotFoundException {
        try {
            userService.deleteUser(uuid);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred while deleting user.");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/search")
    public String searchUsers(UserSearchDTO dto,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            List<UserReadOnlyDTO> users = userService.searchUsers(dto);

            model.addAttribute("users", users);
            model.addAttribute("keyword", dto.getKeyword());
            model.addAttribute("selectedRole", dto.getRole());
            model.addAttribute("showDeleted", dto.isShowDeleted());

            return "admin-users";

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "No users found with the given filters.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error during user search.");
            return "redirect:/admin/users";
        }
    }


}
