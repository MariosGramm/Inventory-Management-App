package com.company.inventory.simple_inventory.dto;


import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadOnlyDTO {

    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String uuid;
    private UnitOfMeasure unit;
    private List<InventoryReadOnlyDTO> inventories;
}
