package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import com.company.inventory.simple_inventory.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> , JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByUuid(String uuid);
    Optional<TransactionType> findTransactionTypeByUuid(String uuid);
}
