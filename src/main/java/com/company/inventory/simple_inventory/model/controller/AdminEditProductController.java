package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.ProductUpdateDTO;
import com.company.inventory.simple_inventory.service.IProductService;
import com.company.inventory.simple_inventory.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class AdminEditProductController {

    private final IWarehouseService warehouseService;
    private final IProductService productService;

    @GetMapping("/admin/products/edit/{uuid}")
    public String showEditProductPage(@PathVariable String uuid, Model model, RedirectAttributes redirectAttributes) throws EntityNotFoundException{
        try {
            ProductUpdateDTO dto = productService.getProductForUpdate(uuid);
            model.addAttribute("product", dto);
            model.addAttribute("units", UnitOfMeasure.values());
            model.addAttribute("warehouses", warehouseService.getAllWarehouses());
            return "admin-product-edit";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Product not found!");
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/admin/products/edit/{uuid}")
    public String updateProduct(@PathVariable String uuid,
                                @ModelAttribute ProductUpdateDTO productUpdateDTO,
                                RedirectAttributes redirectAttributes) {
        try {
            if (!uuid.equals(productUpdateDTO.getUuid())) {
                redirectAttributes.addFlashAttribute("error", "Mismatched product identifiers!");
                return "redirect:/admin/products";
            }

            productService.updateProduct(uuid, productUpdateDTO);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");

        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", "A product with this name already exists!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Product not found!");
        } catch (EntityInvalidArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid product parameters!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred while updating product.");
        }

        return "redirect:/admin/products";
    }
}
