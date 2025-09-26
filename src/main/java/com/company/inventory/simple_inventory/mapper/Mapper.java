package com.company.inventory.simple_inventory.mapper;

import com.company.inventory.simple_inventory.dto.*;
import com.company.inventory.simple_inventory.model.Inventory;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.model.Warehouse;


import java.util.List;

public class Mapper {

    public Product mapToProductEntity(ProductInsertDTO dto){
        return new Product(null,null, dto.getName(), dto.getDescription(),dto.getUnit(),null,null);
    }

    public Inventory mapToInventoryEntity(InventoryInsertDTO dto, Product product, Warehouse warehouse) {
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(dto.getQuantity());
        return inventory;
    }

    public ProductReadOnlyDTO mapToProductReadOnlyDTO(Product product) {
        List<InventoryReadOnlyDTO> inventoryDTOs = product.getAllProductInventories().stream()
                .map(inv -> new InventoryReadOnlyDTO(
                        inv.getWarehouse().getName(),
                        inv.getQuantity(),
                        product.getName()
                ))
                .toList();

        return new ProductReadOnlyDTO(
                product.getName(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getUuid(),
                product.getUnit(),
                inventoryDTOs
        );
    }


    public InventoryReadOnlyDTO mapToInventoryReadOnlyDTO(Inventory inventory) {
        return new InventoryReadOnlyDTO
                        (inventory.getWarehouse().getName(),
                        inventory.getQuantity(),
                        inventory.getProduct().getName());
    }



}
