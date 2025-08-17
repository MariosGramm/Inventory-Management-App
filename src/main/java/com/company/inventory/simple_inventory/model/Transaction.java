package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import com.company.inventory.simple_inventory.model.abstract_classes.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transaction")
public class Transaction extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Long quantity;

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
