package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.dto.InventoryInsertDTO;
import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.InventoryUpdateDTO;
import com.company.inventory.simple_inventory.model.Inventory;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.model.Warehouse;

import java.util.List;

public interface IInventoryService {

    InventoryReadOnlyDTO createInventory(InventoryInsertDTO dto);
    InventoryReadOnlyDTO updateInventory(InventoryUpdateDTO dto);
    void deleteInventory (String inventoryUuid);   //incorrect entry cancellation
    List<InventoryReadOnlyDTO> findByProduct(String productUuid);
    List<InventoryReadOnlyDTO> findByWarehouse(String warehouseUuid);
    InventoryReadOnlyDTO findByProductAndWarehouse(String productUuid, String warehouseUuid);
}
