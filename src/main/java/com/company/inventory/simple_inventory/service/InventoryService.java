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
    @Transactional(rollbackOn = {EntityAlreadyExistsException.class,EntityNotFoundException.class})
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
            throw e;
        }
    }

    @Override
    @Transactional (rollbackOn = {EntityAlreadyExistsException.class,EntityNotFoundException.class})
    public InventoryReadOnlyDTO updateInventory(InventoryUpdateDTO dto) throws EntityAlreadyExistsException,EntityNotFoundException {
        try {
            Inventory inventory = inventoryRepository.findByProductUuidAndWarehouseUuid(dto.getProductUuid(), dto.getWarehouseUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Inventory","Inventory not found"));

            Product product = productRepository.findByUuid(dto.getProductUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Product","Product does not exist"));

            Warehouse warehouse = warehouseRepository.findByUuid(dto.getWarehouseUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse","Warehouse does not exist"));


            if (!inventory.getProduct().getUuid().equals(dto.getProductUuid()) && inventory.getWarehouse().getUuid().equals(dto.getWarehouseUuid())){
                inventory.setQuantity(dto.getQuantity());
            }else throw new EntityAlreadyExistsException("Inventory",String.format("Inventory with Product name = %s and Warehouse name = %s already exists", product.getName(),warehouse.getName()));

            return mapper.mapToInventoryReadOnlyDTO(inventory);

        } catch (EntityAlreadyExistsException e){
            log.error("Update failed for inventory with product uuid = {} and warehouse uuid = {}.Inventory already exists",dto.getProductUuid(),dto.getWarehouseUuid());
            throw e;
        }catch (EntityNotFoundException e){
            log.error("Update failed for inventory with product uuid = {} and warehouse uuid = {}.Inventory not found",dto.getProductUuid(),dto.getWarehouseUuid());
            throw e;
        }
    }

    @Override
    @Transactional (rollbackOn = {EntityNotFoundException.class})
    public void deleteInventory(String inventoryUuid) throws EntityNotFoundException {
        Inventory inventory = inventoryRepository.findByUuid(inventoryUuid)
                .orElseThrow(() -> new EntityNotFoundException("Inventory","Inventory not found"));

        inventoryRepository.deleteById(inventory.getId());
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public List<InventoryReadOnlyDTO> findByProduct(String productUuid) throws EntityNotFoundException {
        try {
            List<Inventory> inventories = inventoryRepository.findByProductUuid(productUuid);

            if (inventories.isEmpty()) {
                throw new EntityNotFoundException("Inventory", "Inventory with Product uuid " + productUuid + " not found");
            }

            return inventories.stream()
                    .map(mapper :: mapToInventoryReadOnlyDTO)
                    .toList();
        }catch (EntityNotFoundException e){
            log.error("Inventory not found",e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public List<InventoryReadOnlyDTO> findByWarehouse(String warehouseUuid) throws EntityNotFoundException {
        List<Inventory> inventories = inventoryRepository.findByWarehouse_Uuid(warehouseUuid);

        if (inventories.isEmpty()) {
            throw new EntityNotFoundException("Inventory", "Inventory with Warehouse uuid " + warehouseUuid + " not found");
        }

        return inventories.stream()
                .map(mapper :: mapToInventoryReadOnlyDTO)
                .toList();
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public InventoryReadOnlyDTO findByProductAndWarehouse(String productUuid, String warehouseUuid) throws EntityNotFoundException {

        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException("Product","Product does not exist"));

        Warehouse warehouse = warehouseRepository.findByUuid(warehouseUuid)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse","Warehouse does not exist"));

        Inventory inventory = inventoryRepository.findByProductUuidAndWarehouseUuid(productUuid, warehouseUuid)
                .orElseThrow(() -> new EntityNotFoundException("Inventory",
                        String.format("Inventory not found for Product name = %s and Warehouse name = %s",
                                product.getName(), warehouse.getName())));

        return mapper.mapToInventoryReadOnlyDTO(inventory);
    }

    @Override
    public long countTransactions() {
        return inventoryRepository.count();
    }

}
