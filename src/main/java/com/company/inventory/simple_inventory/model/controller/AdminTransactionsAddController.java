package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryInsertDTO;
import com.company.inventory.simple_inventory.service.InventoryService;
import com.company.inventory.simple_inventory.service.ProductService;
import com.company.inventory.simple_inventory.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminTransactionsAddController {

    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final InventoryService inventoryService;

    @GetMapping("/admin/transactions/new")
    public String showAddTransactionForm(Model model){
        model.addAttribute("transaction",new InventoryInsertDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());

        return "admin-transaction-add";
    }

    @PostMapping("/admin/transactions/add")
    public String addTransaction(InventoryInsertDTO inventoryInsertDTO, RedirectAttributes redirectAttributes) throws EntityNotFoundException {
        try {
            inventoryService.addTransaction(inventoryInsertDTO);
            redirectAttributes.addFlashAttribute("success","Transaction added successfully");
        }catch (EntityNotFoundException e){         // Should never occur since product and warehouse are chosen via dropdowns,kept for consistency purposes
            redirectAttributes.addFlashAttribute("error","Transaction not found");
        }catch (EntityInvalidArgumentException e) {     // Should never occur since product and warehouse are chosen via dropdowns,kept for consistency purposes
            redirectAttributes.addFlashAttribute("error", "Invalid transaction parameters");
        }

        return "redirect:/admin/transactions";
    }
}
