package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.InventorySearchDTO;
import com.company.inventory.simple_inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminTransactionsController {

    private final InventoryService inventoryService;

    @GetMapping("/admin/transactions")
    public String showAdminTransactions(@RequestParam(defaultValue = "0") int page ,
                                        @RequestParam(defaultValue = "10") int size ,
                                        @RequestParam(defaultValue = "false") boolean showDeleted,
                                        Model model) {

        Page<InventoryReadOnlyDTO> transactionPage;

        if (showDeleted) {
            transactionPage = inventoryService.getPaginatedTransactions(page, size);
        } else {
            transactionPage = inventoryService.getPaginatedNotDeletedTransactions(page, size);
        }

        model.addAttribute("transactions", transactionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transactionPage.getTotalPages());
        model.addAttribute("totalTransactions", transactionPage.getTotalElements());
        model.addAttribute("showDeleted", showDeleted);

        return "admin-transactions";

    }



    @GetMapping("/admin/transactions/search")
    public String searchTransactions(
            InventorySearchDTO searchDTO,
            Model model,
            RedirectAttributes redirectAttributes) throws EntityNotFoundException {
        try {
            List<InventoryReadOnlyDTO> transactions = inventoryService.searchTransactions(searchDTO);
            model.addAttribute("transactions", transactions);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "No transactions found.");
            return "redirect:/admin/transactions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error during search.");
            return "redirect:/admin/transactions";
        }

        return "admin-transactions";
    }



}
