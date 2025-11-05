package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.core.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "products")
public class Product extends BaseEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String description;

    @Column(nullable = false)
    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unit;

    private double price;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "product")
    public Set<Transaction> transactions = new HashSet<>();

    public Set<Transaction> getAllProductTransactions() {return Collections.unmodifiableSet(transactions);}

    public void addProductTransaction(Transaction transaction) {
        if (transactions == null) transactions = new HashSet<>();
        transactions.add(transaction);
        transaction.assignTransactionProduct(this);
    }

    public void removeProductTransaction(Transaction transaction) {
        if (transactions == null) return;
        transactions.remove(transaction);
        transaction.assignTransactionProduct(null);
    }

    @OneToMany(mappedBy = "product")
    @Getter(AccessLevel.PROTECTED)
    private Set<Inventory> inventories = new HashSet<>();

    public Set<Inventory> getInventoriesSafe() {
        return Collections.unmodifiableSet(inventories);
    }


    public Set<Inventory> getAllProductInventories() {return Collections.unmodifiableSet(inventories);}

    public void addProductInventory(Inventory inventory) {
        if (inventories == null) inventories = new HashSet<>();
        inventories.add(inventory);
        inventory.setProduct(this);
    }

    public void removeProductInventory(Inventory inventory) {
        if (inventories == null) return;
        inventories.remove(inventory);
        inventory.setProduct(null);
    }


}
