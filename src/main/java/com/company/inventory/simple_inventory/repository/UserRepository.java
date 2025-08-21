package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {

    Optional<User> findByUuid(String uuid);
    Optional<User> findByTransactionUuid(String uuid);

    @Query("SELECT u.lastname,u.username,u.uuid,count(t) FROM User u JOIN u.transactions t GROUP BY u ORDER BY count(t) DESC")
    List<Object> getTransactionCountPerUser();
}
