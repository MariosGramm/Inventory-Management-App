package com.company.inventory.simple_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateDTO {       //other fields(warehouse , product etc) cannot change.User has to delete the entry and re-insert it.
    private Double quantity;
}
