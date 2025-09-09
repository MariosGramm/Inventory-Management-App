package com.company.inventory.simple_inventory.dto;


import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;

import java.time.LocalDateTime;

public class ProductReadOnlyDTO {

    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String uuid;
    private Double quantity;
    private UnitOfMeasure unit;
    private Long warehouseName;
}
