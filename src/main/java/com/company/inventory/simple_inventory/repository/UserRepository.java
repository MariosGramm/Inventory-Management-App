package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {

    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);


    @Query("SELECT u.lastname,u.username,u.uuid,count(t) FROM User u JOIN u.transactions t GROUP BY u ORDER BY count(t) DESC")
    List<Object> getTransactionCountPerUser();

    @Query("SELECT t.user FROM Transaction t WHERE t.uuid = :transactionUuid")
    Optional<User> findUserByTransactionUuid(@Param("transactionUuid") String transactionUuid);

}
