package com.company.inventory.simple_inventory.dto;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
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
    @NotNull(message = "Transaction UUID cannot be null")
    private String uuid;

    private String productUuid;     //read-only
    private String warehouseUuid;   // read-only

    private TransactionType transactionType;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity must be >= 0")
    private Double quantity;
}
