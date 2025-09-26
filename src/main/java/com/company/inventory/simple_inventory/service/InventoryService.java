package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryInsertDTO;
import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.InventoryUpdateDTO;
import com.company.inventory.simple_inventory.mapper.Mapper;
import com.company.inventory.simple_inventory.model.Inventory;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.model.Warehouse;
import com.company.inventory.simple_inventory.repository.InventoryRepository;
import com.company.inventory.simple_inventory.repository.ProductRepository;
import com.company.inventory.simple_inventory.repository.WarehouseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService implements IInventoryService{

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackOn = {EntityAlreadyExistsException.class,EntityInvalidArgumentException.class})
    public InventoryReadOnlyDTO createInventory(InventoryInsertDTO dto) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            if (inventoryRepository.findByProductUuidAndWarehouseUuid(dto.getProductUuid(), dto.getWarehouseUuid()).isPresent()) {
                throw new EntityAlreadyExistsException("Inventory","Inventory already exists");
            }

            Product product = productRepository.findByUuid(dto.getProductUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Product","Product not found"));

            Warehouse warehouse = warehouseRepository.findByUuid(dto.getWarehouseUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse","Warehouse not found"));

            Inventory inventory = mapper.mapToInventoryEntity(dto,product,warehouse);

            inventoryRepository.save(inventory);
            return mapper.mapToInventoryReadOnlyDTO(inventory);

        }catch (EntityAlreadyExistsException e){
            log.error("Save failed for inventory with product uuid = {} and warehouse uuid = {}.Inventory already exists",dto.getProductUuid(),dto.getWarehouseUuid());
            throw e;
        }catch (EntityNotFoundException e){
            log.error("Save failed for inventory with product uuid = {} and warehouse uuid = {}.Inventory not found",dto.getProductUuid(),dto.getWarehouseUuid());
        }
    }

    @Override
    public InventoryReadOnlyDTO updateInventory(InventoryUpdateDTO dto) {

    }

    @Override
    public void deleteInventory(String inventoryUuid) {

    }

    @Override
    public List<InventoryReadOnlyDTO> findByProduct(String productUuid) {
        return List.of();
    }

    @Override
    public List<InventoryReadOnlyDTO> findByWarehouse(String warehouseUuid) {
        return List.of();
    }

    @Override
    public InventoryReadOnlyDTO findByProductAndWarehouse(String productUuid, String warehouseUuid) {
        return null;
    }
}
