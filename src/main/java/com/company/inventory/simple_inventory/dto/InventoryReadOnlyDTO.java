package com.company.inventory.simple_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReadOnlyDTO {
    private String warehouseName;
    private Double quantity;
    private String productName;
}
