package com.company.inventory.simple_inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "warehouses")
public class Warehouse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String address;

    private Integer postalCode;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "warehouse")
    public Set<Inventory> inventories = new HashSet<>();

    public Set<Inventory> getAllWarehouseInventories() {return Collections.unmodifiableSet(inventories);}

    public void addWarehouseInventory(Inventory inventory) {
        if (inventories == null) inventories = new HashSet<>();
        inventories.add(inventory);
        inventory.setWarehouse(this);
    }

    public void removeWarehouseInventory(Inventory inventory) {
        if (inventories == null) return;
        inventories.remove(inventory);
        inventory.setWarehouse(null);
    }


}
