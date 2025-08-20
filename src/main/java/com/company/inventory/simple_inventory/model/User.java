package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.core.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "user")
    private Set<Transaction> transactions = new HashSet<>();

    public Set<Transaction> getAllUserTransactions() {return Collections.unmodifiableSet(transactions);}

    public void addUserTransaction(Transaction transaction) {
        if (transactions == null) transactions = new HashSet<>();
        transactions.add(transaction);
        transaction.setUser(this);
    }

    public void removeUserTransaction(Transaction transaction) {
        if (transactions == null) return;
        transactions.remove(transaction);
        transaction.setUser(null);
    }
}
