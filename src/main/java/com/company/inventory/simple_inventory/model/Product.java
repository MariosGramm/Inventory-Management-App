package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.model.abstract_classes.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "products")
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid = UUID.randomUUID().toString();

    @Column(unique = true)
    private String name;

    private Long quantity;

    @Column(unique = true)
    private String description;
}
