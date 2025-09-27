package com.company.inventory.simple_inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateDTO {       //other fields(warehouse , product etc) cannot change.User has to delete the entry and re-insert it.
    @NotNull(message = "Product UUID cannot be null")
    private String productUuid;

    @NotNull(message = "Warehouse UUID cannot be null")
    private String warehouseUuid;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity must be >= 0")
    private Double quantity;
}
