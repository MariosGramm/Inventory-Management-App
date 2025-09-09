package com.company.inventory.simple_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductInsertDTO {

    @NotBlank(message = "Name field cannot be blank")
    @Size(min = 2 , message = "Product name must be at least 2 characters in size")
    private String name;

    private String description;

    @NotBlank(message = "Quantity field cannot be blank")
    private Long quantity;

    @NotNull(message = "A warehouse must be selected")
    private Long warehouseId;
}
