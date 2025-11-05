package com.company.inventory.simple_inventory.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryReadOnlyDTO {
    private String warehouseName;
    private Double quantity;
    private String productName;
    private String transactionType;
}
