package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import com.company.inventory.simple_inventory.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {

    Optional<Product> findByUuid(String uuid);
    Optional<Product> findByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByInventories_Warehouse_NameIgnoreCase(String warehouseName);
    List<Product> findByUnit(UnitOfMeasure unit);
    Page<Product> findByDeletedFalse(Pageable pageable);
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT t.product FROM Transaction t WHERE t.uuid = :transactionUuid")
    Optional<Product> findProductByTransactionUuid(@Param("transactionUuid") String transactionUuid);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.inventories i LEFT JOIN FETCH i.warehouse")
    List<Product> findAllWithInventories();
}
