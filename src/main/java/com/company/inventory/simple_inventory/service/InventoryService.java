package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryInsertDTO;
import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.InventoryUpdateDTO;
import com.company.inventory.simple_inventory.mapper.Mapper;
import com.company.inventory.simple_inventory.model.Inventory;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.model.Transaction;
import com.company.inventory.simple_inventory.model.Warehouse;
import com.company.inventory.simple_inventory.repository.InventoryRepository;
import com.company.inventory.simple_inventory.repository.ProductRepository;
import com.company.inventory.simple_inventory.repository.TransactionRepository;
import com.company.inventory.simple_inventory.repository.WarehouseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService implements IInventoryService{

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final TransactionRepository transactionRepository;
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


            if (inventory.getProduct().getUuid().equals(dto.getProductUuid())
                    && inventory.getWarehouse().getUuid().equals(dto.getWarehouseUuid())) {

                inventory.setQuantity(dto.getQuantity());

            } else {
                throw new EntityAlreadyExistsException("Inventory",
                        String.format("Inventory with Product name = %s and Warehouse name = %s already exists",
                                product.getName(), warehouse.getName()));
            }

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

    @Override
    public Page<InventoryReadOnlyDTO> getPaginatedTransactions(int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> transactionPage = inventoryRepository.findAll(pageable);
        log.debug("Page = {} , Size = {}",page,size);
        return transactionPage.map(mapper::mapToInventoryReadOnlyDTO);
    }

    @Override
    public Page<InventoryReadOnlyDTO> getPaginatedNotDeletedTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionPage = transactionRepository.findByDeletedFalse(pageable);

        return  transactionPage.map(transaction -> {
            InventoryReadOnlyDTO dto = new InventoryReadOnlyDTO();
            dto.setProductName(transaction.getProduct().getName());
            dto.setQuantity(transaction.getQuantity());
            dto.setTransactionType(transaction.getType().name());
            dto.setWarehouseName(
                    Optional.ofNullable(transaction.getProduct())
                            .map(Product::getInventoriesSafe)
                            .filter(inventories -> !inventories.isEmpty())
                            .flatMap(inventories -> inventories.stream().findFirst())
                            .map(inventory -> inventory.getWarehouse().getName())
                            .orElse("N/A")
            );
            return dto;
        });
    }

    @Override
    @Transactional(rollbackOn = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public void addTransaction(InventoryInsertDTO dto)
            throws EntityNotFoundException, EntityInvalidArgumentException {

        Product product = productRepository.findByUuid(dto.getProductUuid())
                .orElseThrow(() -> new EntityNotFoundException("Product","Product not found"));

        Warehouse warehouse = warehouseRepository.findByUuid(dto.getWarehouseUuid())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse","Warehouse not found"));

        Inventory inventory = inventoryRepository.findByProductUuidAndWarehouseUuid(product.getUuid(), warehouse.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Inventory","Inventory not found"));

        if (dto.getQuantity() == null || dto.getQuantity() <= 0)
            throw new EntityInvalidArgumentException("Quantity","Quantity must be positive");

        // --- apply stock change based on transactionType ---
        if (dto.getTransactionType() == TransactionType.INCREASE) {
            inventory.setQuantity(inventory.getQuantity() + dto.getQuantity());
        } else if (dto.getTransactionType() == TransactionType.DECREASE) {
            if (inventory.getQuantity() < dto.getQuantity()) {
                throw new EntityInvalidArgumentException("Quantity", "Not enough stock for OUT transaction");
            }
            inventory.setQuantity(inventory.getQuantity() - dto.getQuantity());
        }

        // --- save transaction record ---
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setType(dto.getTransactionType());
        transaction.setQuantity(dto.getQuantity());
        transaction.setDeleted(false);
        transaction.setWarehouse(warehouse);

        transactionRepository.save(transaction);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public void deleteTransaction(String uuid) throws EntityNotFoundException {
        Transaction transaction = transactionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Transaction","Transaction not found with uuid: " + uuid));


        Inventory inventory = inventoryRepository
                .findByProductUuidAndWarehouseUuid(transaction.getProduct().getUuid(), transaction.getWarehouseSafe().getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Inventory","Inventory not found for this transaction."));

        // Changing the inventory quantity with transaction reversal
        if (transaction.getType() == TransactionType.INCREASE) {
            inventory.setQuantity(inventory.getQuantity() - transaction.getQuantity());
        } else if (transaction.getType() == TransactionType.DECREASE) {
            inventory.setQuantity(inventory.getQuantity() + transaction.getQuantity());
        }

        // Soft delete
        transaction.setDeleted(true);


        transactionRepository.save(transaction);
        inventoryRepository.save(inventory);

        log.info("Transaction {} deleted (soft) and inventory adjusted for warehouse {}", uuid, transaction.getWarehouseSafe().getName());
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public List<InventoryReadOnlyDTO> searchTransactions(TransactionType type, LocalDate fromDate, LocalDate toDate) throws EntityNotFoundException {
        LocalDateTime from = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime to = (toDate != null) ? toDate.atTime(23, 59, 59) : null;

        List<Transaction> transactions = transactionRepository.findByTypeAndCreatedAtBetween(type, from, to);

        if (transactions.isEmpty()) {
            throw new EntityNotFoundException("Transaction","No transactions found with the given filters.");
        }

        return transactions.stream()
                .map(t -> new InventoryReadOnlyDTO(
                        t.getProduct().getName(),
                        t.getQuantity(),
                        t.getProduct().getUnit().toString(),
                        t.getType().toString()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public void updateTransaction(InventoryUpdateDTO dto)
            throws EntityNotFoundException, EntityInvalidArgumentException {


        Transaction transaction = transactionRepository.findByUuid(dto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Transaction", "Transaction not found"));

        Product product = transaction.getProduct();
        Warehouse warehouse = transaction.getWarehouseSafe();


        Inventory inventory = inventoryRepository.findByProductUuidAndWarehouseUuid(product.getUuid(), warehouse.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Inventory", "Inventory not found for this transaction"));


        if (dto.getQuantity() == null || dto.getQuantity() < 0) {
            throw new EntityInvalidArgumentException("Quantity", "Quantity must be >= 0");
        }


        double oldQuantity = transaction.getQuantity();
        double newQuantity = dto.getQuantity();
        double diff = newQuantity - oldQuantity;


        if (transaction.getType() == TransactionType.INCREASE) {
            inventory.setQuantity(inventory.getQuantity() + diff);
        } else if (transaction.getType() == TransactionType.DECREASE) {
            if (inventory.getQuantity() - diff < 0) {
                throw new EntityInvalidArgumentException("Quantity", "Not enough stock to decrease further");
            }
            inventory.setQuantity(inventory.getQuantity() - diff);
        }


        transaction.setQuantity(newQuantity);


        if (dto.getTransactionType() != null) {
            transaction.setType(dto.getTransactionType());
        }


        transactionRepository.save(transaction);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public InventoryUpdateDTO getTransactionForUpdate(String uuid) throws EntityNotFoundException {
        Transaction transaction = transactionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Transaction", "Transaction not found"));
        return mapper.mapToUpdateInventoryDTO(transaction);
    }











}
