package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.InventorySearchDTO;
import com.company.inventory.simple_inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserTransactionsController {

    private final InventoryService inventoryService;

    @GetMapping("/user/transactions")
    public String showTransactions(Model model) {
        List<InventoryReadOnlyDTO> transactions = inventoryService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "user-transactions";
    }

    @GetMapping("/user/transactions/search")
    public String searchTransactions(
            InventorySearchDTO searchDTO,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            List<InventoryReadOnlyDTO> transactions = inventoryService.searchTransactions(searchDTO);

            model.addAttribute("transactions", transactions);
            model.addAttribute("searchName", searchDTO.getProductName());
            model.addAttribute("selectedType", searchDTO.getTransactionType());
            model.addAttribute("fromDate", searchDTO.getFromDate());
            model.addAttribute("toDate", searchDTO.getToDate());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No transactions found with the given filters.");
            return "redirect:/user/transactions";
        }

        return "user-transactions";
    }
}
