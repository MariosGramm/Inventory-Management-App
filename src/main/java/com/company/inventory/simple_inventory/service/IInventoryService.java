package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.enums.TransactionType;
import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.core.exceptions.EntityNotFoundException;
import com.company.inventory.simple_inventory.dto.InventoryInsertDTO;
import com.company.inventory.simple_inventory.dto.InventoryReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.InventorySearchDTO;
import com.company.inventory.simple_inventory.dto.InventoryUpdateDTO;
import com.company.inventory.simple_inventory.model.Inventory;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.model.Warehouse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IInventoryService {

    InventoryReadOnlyDTO createInventory(InventoryInsertDTO dto) throws EntityAlreadyExistsException, EntityNotFoundException;
    InventoryReadOnlyDTO updateInventory(InventoryUpdateDTO dto) throws EntityAlreadyExistsException, EntityNotFoundException;
    void deleteInventory (String inventoryUuid) throws EntityNotFoundException;   //incorrect entry cancellation
    List<InventoryReadOnlyDTO> findByProduct(String productUuid) throws EntityNotFoundException;
    List<InventoryReadOnlyDTO> findByWarehouse(String warehouseUuid) throws EntityNotFoundException;
    InventoryReadOnlyDTO findByProductAndWarehouse(String productUuid, String warehouseUuid) throws EntityNotFoundException;
    long countTransactions();
    Page<InventoryReadOnlyDTO> getPaginatedTransactions(int page , int size);
    Page<InventoryReadOnlyDTO> getPaginatedNotDeletedTransactions(int page, int size);
    void addTransaction(InventoryInsertDTO inventoryInsertDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException;
    void deleteTransaction(String uuid) throws EntityNotFoundException;
    List<InventoryReadOnlyDTO> searchTransactions(InventorySearchDTO inventorySearchDTO) throws EntityNotFoundException;
    void updateTransaction(InventoryUpdateDTO updateDTO) throws EntityNotFoundException,EntityInvalidArgumentException;
    InventoryUpdateDTO getTransactionForUpdate(String uuid) throws EntityNotFoundException;
    List<InventoryReadOnlyDTO> getRecentTransactions();
    List<InventoryReadOnlyDTO> getAllTransactions();


}
