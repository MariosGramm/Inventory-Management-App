package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;

public class ProductUpdateDTO {

    @NotBlank(message = "Uuid field cannot be blank")
    private String uuid;

    @NotBlank(message = "Name field cannot be blank" )
    private String name;

    @NotBlank(message = "Description field cannot be blank")
    private String description;

    @NotBlank(message = "Unit field cannot be blank")
    private UnitOfMeasure unit;
}
