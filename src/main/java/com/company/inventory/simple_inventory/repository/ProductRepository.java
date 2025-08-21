package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {

    Optional<Product> findByUuid(String uuid);
    Optional<Product> findByName(String name);
    List<Long> findProductIdByTransactionUuid(String uuid);
    List<Product> findByTransactionUuid(String uuid);
}
