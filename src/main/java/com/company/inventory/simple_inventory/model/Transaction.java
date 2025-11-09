package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Double quantity;

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void assignUser(User user){
        if (this.user != null){
            this.user.getTransactions().remove(this);
        }

        this.user = user;
        if (user != null) {
            user.getTransactions().add(this);
        }
    }

    @Getter(AccessLevel.PUBLIC)
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void assignTransactionProduct(Product product) {
        if (this.product != null){
            this.product.getTransactions().remove(this);
        }

        this.product = product;
        if (product != null){
            product.getTransactions().add(this);
        }
    }

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;


    public Warehouse getWarehouseSafe() {
        return this.warehouse;
    }


}
