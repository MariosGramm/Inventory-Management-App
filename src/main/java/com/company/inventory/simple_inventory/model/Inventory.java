package com.company.inventory.simple_inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "inventory" ,
            uniqueConstraints = {
                @UniqueConstraint(columnNames = {"warehouse_id", "product_id"})     //can't insert the same product on the same warehouse more than one time
                                }
            )
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void assignInventoryProduct(Product product) {
        if (this.product != null){
            this.product.getInventories().remove(this);
        }

        this.product = product;
        if (product != null){
            product.getInventories().add(this);
        }
    }

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public void assignInventoryWarehouse(Warehouse warehouse) {
        if (this.warehouse != null){
            this.warehouse.getInventories().remove(this);
        }

        this.warehouse = warehouse;
        if (warehouse != null){
            warehouse.getInventories().add(this);
        }
    }
}
