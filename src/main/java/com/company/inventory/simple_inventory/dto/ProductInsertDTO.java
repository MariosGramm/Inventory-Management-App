package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    @Size(min = 2, message = "Product name must be at least 2 characters long")
    private String name;

    @NotNull(message = "A unit of measure must be selected")
    private UnitOfMeasure unit;

    private String description;

    @PositiveOrZero(message = "Quantity must be 0 or positive")
    private Double quantity; // optional — defaults to 0.0

    @NotBlank(message = "Warehouse must be selected")
    private String warehouseUuid;

    @NotNull(message = "Product price is mandatory")
    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;
}
