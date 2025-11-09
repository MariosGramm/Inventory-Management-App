package com.company.inventory.simple_inventory.mapper;

import com.company.inventory.simple_inventory.dto.*;
import com.company.inventory.simple_inventory.model.*;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public Product mapToProductEntity(ProductInsertDTO dto){

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .unit(dto.getUnit())
                .price(dto.getPrice())
                .build();

    }

    public Inventory mapToInventoryEntity(InventoryInsertDTO dto, Product product, Warehouse warehouse) {
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(dto.getQuantity());
        return inventory;
    }



    public ProductReadOnlyDTO mapToProductReadOnlyDTO(Product product) {
        List<InventoryReadOnlyDTO> inventoryDTOs = product.getInventoriesSafe().stream()
                .map(inv -> InventoryReadOnlyDTO.builder()
                        .warehouseName(inv.getWarehouse().getName())
                        .quantity(inv.getQuantity())
                        .productName(product.getName())
                        .build())
                .collect(Collectors.toList());

        double totalStock = product.getInventoriesSafe().stream()
                .peek(inv -> System.out.println(
                        ">>> Inventory found for product " + product.getName() +
                                ": " + inv.getWarehouse().getName() + " -> " + inv.getQuantity()))
                .filter(inv -> inv.getQuantity() != null)
                .mapToDouble(Inventory::getQuantity)
                .sum();

        System.out.println(">>> Total stock for " + product.getName() + " = " + totalStock);

        return new ProductReadOnlyDTO(
                product.getName(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getUuid(),
                product.getUnit(),
                inventoryDTOs,
                totalStock
        );
    }


    public InventoryReadOnlyDTO mapToInventoryReadOnlyDTO(Inventory inventory) {
        return InventoryReadOnlyDTO.builder()
                .warehouseName(inventory.getWarehouse().getName())
                .quantity(inventory.getQuantity())
                .productName(inventory.getProduct().getName())
                .build();
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user){
        return new UserReadOnlyDTO
                (
                        user.getUuid(), user.getUsername(), user.getEmail(), user.getRole(), user.getFirstname(), user.getLastname()
                );
    }

    public User mapToUser(UserInsertDTO insertDTO){
        User user = new User();

        user.setEmail(insertDTO.getEmail());
        user.setFirstname(insertDTO.getFirstname());
        user.setLastname(insertDTO.getLastname());
        user.setPassword(insertDTO.getPassword());
        user.setRole(insertDTO.getRole());

        return user;
    }

    public InventoryUpdateDTO mapToUpdateInventoryDTO(Transaction transaction) {
        InventoryUpdateDTO dto = new InventoryUpdateDTO();
        dto.setUuid(transaction.getUuid());
        dto.setProductUuid(transaction.getProduct().getUuid());
        dto.setWarehouseUuid(transaction.getWarehouseSafe().getUuid());
        dto.setQuantity(transaction.getQuantity());
        dto.setTransactionType(transaction.getType());
        return dto;
    }

    public ProductUpdateDTO mapToProductUpdateDTO(Product product) {
        ProductUpdateDTO dto = new ProductUpdateDTO();

        dto.setUuid(product.getUuid());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setUnit(product.getUnit());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getInventoriesSafe()
                .stream()
                .filter(inv -> !inv.getWarehouse().getDeleted())
                .findFirst()
                .map(Inventory::getQuantity)
                .orElse(0.0));


        product.getInventoriesSafe().stream()
                .filter(inv -> !inv.getWarehouse().getDeleted())
                .findFirst()
                .ifPresent(inv -> dto.setWarehouseUuid(inv.getWarehouse().getUuid()));

        return dto;
    }



}
