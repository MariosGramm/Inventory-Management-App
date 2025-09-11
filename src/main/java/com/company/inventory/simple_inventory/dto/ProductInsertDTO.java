package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInsertDTO {

    @NotBlank(message = "Name field cannot be blank")
    @Size(min = 2 , message = "Product name must be at least 2 characters in size")
    private String name;

    @NotBlank(message = "A unit of measure must be selected")
    private UnitOfMeasure unit;

    private String description;

    @NotBlank(message = "Quantity field cannot be blank")
    private Double quantity;

    @NotNull(message = "A warehouse must be selected")
    private Long warehouseId;
}
