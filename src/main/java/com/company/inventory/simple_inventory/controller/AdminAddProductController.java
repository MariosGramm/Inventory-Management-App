package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.dto.ProductInsertDTO;
import com.company.inventory.simple_inventory.service.IProductService;
import com.company.inventory.simple_inventory.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminAddProductController {

    private final IWarehouseService warehouseService;
    private final IProductService productService;

    @GetMapping("/admin/products/new")
    public String showAddProductPage(Model model){

        model.addAttribute("product", new ProductInsertDTO());
        model.addAttribute("units", UnitOfMeasure.values());
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());

        return "admin-product-add";
    }

    @PostMapping("/admin/products/add")
    public String addProduct(ProductInsertDTO productInsertDTO, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(productInsertDTO);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", "Product already exists!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred!");
        }
        return "redirect:/admin/products";
    }

}
