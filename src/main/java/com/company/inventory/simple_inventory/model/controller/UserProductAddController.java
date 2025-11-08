package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.dto.ProductInsertDTO;
import com.company.inventory.simple_inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserProductAddController {

    private final ProductService productService;

    @GetMapping("/user/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductInsertDTO());
        model.addAttribute("units", UnitOfMeasure.values());
        return "user-product-add";
    }

    @PostMapping("/user/products/add")
    public String addProduct(ProductInsertDTO productInsertDTO, RedirectAttributes redirectAttributes) {
        try {
            productService.addProduct(productInsertDTO);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", "A product with this name already exists!");
        } catch (EntityInvalidArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid product data!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error while adding product!");
        }
        return "redirect:/user/products";
    }
}
