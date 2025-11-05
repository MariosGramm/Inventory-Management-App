package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryUpdateDTO;
import com.company.inventory.simple_inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminTransactionsEditController {

    private final InventoryService inventoryService;

    @GetMapping("/admin/transactions/edit/{uuid}")
    public String showEditTransactionPage(@PathVariable String uuid, Model model, RedirectAttributes redirectAttributes) {
        try {
            InventoryUpdateDTO dto = inventoryService.getTransactionForUpdate(uuid);
            model.addAttribute("transaction", dto);
            return "admin-transaction-edit";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Transaction not found.");
            return "redirect:/admin/transactions";
        }
    }


    @PostMapping("/admin/transactions/edit/{uuid}")
    public String updateTransaction(@PathVariable String uuid,
                                    InventoryUpdateDTO inventoryUpdateDTO,
                                    RedirectAttributes redirectAttributes) {
        try {
            inventoryService.updateTransaction(inventoryUpdateDTO);
            redirectAttributes.addFlashAttribute("success", "Transaction updated successfully!");
        } catch (EntityInvalidArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid parameters for transaction update!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Transaction not found!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error during transaction update.");
        }
        return "redirect:/admin/transactions";
    }
}
