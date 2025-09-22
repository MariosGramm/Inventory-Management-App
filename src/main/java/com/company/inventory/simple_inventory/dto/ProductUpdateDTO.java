package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {

    @NotBlank(message = "Name field cannot be blank" )
    private String name;

    @NotBlank(message = "Description field cannot be blank")
    private String description;

    @NotBlank(message = "Unit field cannot be blank")
    private UnitOfMeasure unit;
}
