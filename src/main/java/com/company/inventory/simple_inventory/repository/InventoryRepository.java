package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> , JpaSpecificationExecutor<Inventory> {

    Optional<Inventory> findByUuid(String uuid);
    List<Inventory> findByWarehouse_Id(Long warehouseId);
    Boolean existsByProductIdAndWarehouseId(Long productId , Long warehouseId);
}
