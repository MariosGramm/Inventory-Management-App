package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse,Long> , JpaSpecificationExecutor<Warehouse> {

    Optional<Warehouse> findByUuid(String uuid);

    @Query("SELECT w.name, w.city, w.uuid, COUNT(i) " +
            "FROM Warehouse w " +
            "JOIN w.inventories i " +
            "GROUP BY w.name, w.city, w.uuid " +
            "ORDER BY COUNT(i) DESC")
    List<Object[]> getInventoryCountPerWarehouse();

    @Query("SELECT DISTINCT w FROM Warehouse w JOIN w.inventories i JOIN i.product p WHERE p.name = :productName")
    List<Warehouse> getWarehousesWhereProductExists();
}
