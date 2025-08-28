package com.company.inventory.simple_inventory.model.static_data;

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
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false,nullable = false,unique = true)
    private String uuid;

    @PrePersist
    public void generateUUID(){
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
        }
    }

    @Column(unique = true)
    private String name;

}
