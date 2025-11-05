package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import com.company.inventory.simple_inventory.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> , JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByUuid(String uuid);
    Optional<TransactionType> findTransactionTypeByUuid(String uuid);
    Page<Transaction> findByDeletedFalse(Pageable pageable);
    List<Transaction> findByTypeAndCreatedAtBetween(
            TransactionType type,
            LocalDateTime from,
            LocalDateTime to
    );

    @Query("""
    SELECT t FROM Transaction t
    WHERE (:type IS NULL OR t.type = :type)
    AND (:from IS NULL OR t.createdAt >= :from)
    AND (:to IS NULL OR t.createdAt <= :to)
    AND t.deleted = false
""")
    List<Transaction> searchTransactions(
            @Param("type") TransactionType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}
