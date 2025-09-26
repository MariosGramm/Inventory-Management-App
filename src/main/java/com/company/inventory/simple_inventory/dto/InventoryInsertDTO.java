package com.company.inventory.simple_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryInsertDTO {

    private String productUuid;

    @NotBlank(message = "Quantity field cannot be blank")
    private Double quantity;

    @NotNull(message = "A warehouse must be selected")
    private String warehouseUuid;
}
