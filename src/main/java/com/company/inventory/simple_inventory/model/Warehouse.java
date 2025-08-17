package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.model.abstract_classes.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "warehouses")
public class Warehouse extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid = UUID.randomUUID().toString();

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String address;

    private Integer postalCode;


}
