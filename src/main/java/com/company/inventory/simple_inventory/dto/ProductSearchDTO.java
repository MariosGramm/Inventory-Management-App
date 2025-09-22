package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchDTO {

    private String name;
    private String uuid;
    private String warehouseName;
    private UnitOfMeasure unit;
}
