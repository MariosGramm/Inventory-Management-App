package com.company.inventory.simple_inventory.controller;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.ProductReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.ProductSearchDTO;
import com.company.inventory.simple_inventory.mapper.Mapper;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminProductsController {

    private final IProductService productService;
    private final Mapper mapper;


    @GetMapping("/admin/products")
    public String showAdminProducts(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "false") boolean showDeleted,
                                    Model model){

        Page<ProductReadOnlyDTO> productPage;


        if (showDeleted) {
            productPage = productService.getPaginatedProducts(page, size); // includes deleted
        } else {
            productPage = productService.getPaginatedNotDeletedProducts(page, size); // only not deleted
        }



        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalProducts", productPage.getTotalElements());
        model.addAttribute("pageNumber",page + 1 );
        model.addAttribute("hasNext", productPage.hasNext());

        List<ProductReadOnlyDTO> products = productService.getAllProducts().stream()
                .toList();

        model.addAttribute("products", products);

        return "admin-products";
    }

    @GetMapping("/admin/products/delete/{uuid}")
    public String deleteProduct(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductByUuid(uuid);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Product not found!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred while deleting product.");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/search")
    public String searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String unit,
            Model model,
            RedirectAttributes redirectAttributes) {

        ProductSearchDTO searchDTO = new ProductSearchDTO();
        searchDTO.setName(name);


        try {
            searchDTO.setUnit(unit != null && !unit.isBlank() ? UnitOfMeasure.valueOf(unit.trim().toUpperCase()) : null);
            List<ProductReadOnlyDTO> products = productService.searchProduct(searchDTO);
            model.addAttribute("products", products);
            model.addAttribute("searchName", name);
            return "admin-products";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "No products found for that search.");
            return "redirect:/admin/products";
        } catch (EntityInvalidArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid search criteria.");
            return "redirect:/admin/products";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred while searching.");
            return "redirect:/admin/products";
        }
    }

}
