package com.company.inventory.simple_inventory.model.controller;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.ProductInsertDTO;
import com.company.inventory.simple_inventory.dto.ProductReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.ProductSearchDTO;
import com.company.inventory.simple_inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserProductsController {

    private final ProductService productService;

    @GetMapping("/user/products")
    public String showUserProducts(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {

        Page<ProductReadOnlyDTO> productPage = productService.getPaginatedNotDeletedProducts(page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("pageNumber", page);
        model.addAttribute("hasNext", productPage.hasNext());

        return "user-products";
    }


    @GetMapping("/user/products/search")
    public String searchProducts(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String unit,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        ProductSearchDTO searchDTO = new ProductSearchDTO();
        searchDTO.setName(name);
        searchDTO.setUnit(unit != null && !unit.isBlank() ? UnitOfMeasure.valueOf(unit) : null);

        try {
            if (productService.searchProduct(searchDTO) == null){

            }
            model.addAttribute("products", products);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "No products found.");
            return "redirect:/user/products";
        }

        model.addAttribute("searchName", name);
        model.addAttribute("selectedUnit", unit);
        return "user-products";
    }
}
