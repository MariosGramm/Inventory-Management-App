package com.company.inventory.simple_inventory.dto;


import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadOnlyDTO {

    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String uuid;
    private Double quantity;
    private UnitOfMeasure unit;
    private String warehouseName;
}
