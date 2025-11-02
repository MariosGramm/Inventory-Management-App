package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.dto.*;
import com.company.inventory.simple_inventory.mapper.Mapper;
import com.company.inventory.simple_inventory.model.Inventory;
import com.company.inventory.simple_inventory.model.Product;
import com.company.inventory.simple_inventory.model.Warehouse;
import com.company.inventory.simple_inventory.repository.InventoryRepository;
import com.company.inventory.simple_inventory.repository.ProductRepository;
import com.company.inventory.simple_inventory.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final Mapper mapper;


    @Override
    @Transactional(rollbackOn = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public ProductReadOnlyDTO saveProduct(ProductInsertDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            if (productRepository.findByName(dto.getName()).isPresent()){
                throw new EntityAlreadyExistsException("Product","Product with name" + dto.getName() + "already exists");
            }

            Product product = mapper.mapToProductEntity(dto);

            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Warehouse","Invalid Warehouse id"));

            InventoryInsertDTO inventoryDto = new InventoryInsertDTO();
            inventoryDto.setQuantity(dto.getQuantity()); // αν υπάρχει quantity στο ProductInsertDTO
            inventoryDto.setProductUuid(product.getUuid());
            inventoryDto.setWarehouseUuid(warehouse.getUuid());

            Inventory inventory = mapper.mapToInventoryEntity(inventoryDto, product, warehouse);

            product.addProductInventory(inventory);
            productRepository.save(product);

            return mapper.mapToProductReadOnlyDTO(product);
        }catch (EntityAlreadyExistsException e){
            log.error("Save failed for product with name = {}. Product already exists",dto.getName(),e);
            throw e;
        }catch (EntityInvalidArgumentException e){
            log.error("Save failed for product with name = {}. Warehouse id {} invalid", dto.getName(),dto.getWarehouseId(),e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = {EntityInvalidArgumentException.class,EntityNotFoundException.class})
    public List<ProductReadOnlyDTO> searchProduct(ProductSearchDTO dto) throws EntityInvalidArgumentException, EntityNotFoundException {
        List<Product> products;

        try {
            if (dto == null){
                throw new EntityInvalidArgumentException("Product","Search DTO cannot be null");
            }
            if (dto.getUuid() != null){         //uuid based search
                Product product = productRepository.findByUuid(dto.getUuid())
                        .orElseThrow(() -> new EntityNotFoundException("Product with uuid " + dto.getUuid() + " not found"));
                products = List.of(product);

            }else if (dto.getName() != null && !dto.getName().isBlank()) {      //name based search
                products = productRepository.findByNameContainingIgnoreCase(dto.getName());

                if (products.isEmpty()){
                    throw new EntityNotFoundException("Product with name: " + dto.getName() + " not found" );
                }
            }else if (dto.getWarehouseName() != null && !dto.getWarehouseName().isBlank()) {    //warehouse name based search
                products = productRepository.findByInventories_Warehouse_NameIgnoreCase(dto.getWarehouseName());

                if (products.isEmpty()){
                    throw new EntityNotFoundException("Product with warehouse name: " + dto.getWarehouseName() + " not found" );
                }
            }else if (dto.getUnit() != null){       //unit of measure based search
                products = productRepository.findByUnit(dto.getUnit());

                if (products.isEmpty()){
                    throw new EntityNotFoundException("Product with unit: " + dto.getUnit() + " not found" );
                }

            }else {     //no criteria provided - exception throw
                throw new EntityInvalidArgumentException("Product","No search criteria provided");
            }

            return products.stream()
                    .map(product -> mapper.mapToProductReadOnlyDTO(product))
                    .toList();

        }catch (EntityNotFoundException e){
            log.error("Product not found",e);
            throw e;
        }catch (EntityInvalidArgumentException e){
            log.error("Search failed. Invalid search terms", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn ={EntityNotFoundException.class,EntityAlreadyExistsException.class})
    public ProductReadOnlyDTO updateProduct(String uuid , ProductUpdateDTO dto) throws EntityAlreadyExistsException, EntityAlreadyExistsException {
        try {
            Product product = productRepository.findByUuid(uuid)
                    .orElseThrow(()-> new EntityNotFoundException("Product with uuid: " + uuid + "not found"));

            if (!product.getName().equals(dto.getName())){
                if (productRepository.findByName(dto.getName()).isEmpty()){
                    product.setName(dto.getName());
                } else throw new EntityAlreadyExistsException("Product","Product with name " + dto.getName() + " already exists");
            }

            product.setDescription(dto.getDescription());
            product.setUnit(dto.getUnit());

            productRepository.save(product);
            log.info("Product with uuid = {} updated",uuid);

            return mapper.mapToProductReadOnlyDTO(product);
        }catch (EntityNotFoundException e){
            log.error("Product not found");
            throw e;
        }catch (EntityAlreadyExistsException e){
            log.error("Product already exists");
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = EntityNotFoundException.class)
    public void deleteProductByUuid(String uuid) throws EntityNotFoundException {
        try {
            Product product = productRepository.findByUuid(uuid)
                    .orElseThrow(()-> new EntityNotFoundException("Product with uuid: " + uuid + "not found"));

            //Soft delete
            productRepository.deleteById(product.getId());
            log.info("Product with uuid = {} successfully deleted",product.getUuid());

        }catch (EntityNotFoundException e){
            log.error("Product does not exist");
            throw e;
        }
    }

    @Override
    public Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.findAll(pageable);
        log.debug("Page = {} , Size = {}",page,size);
        return productPage.map(mapper::mapToProductReadOnlyDTO);
    }

    @Override
    public long countProducts() {
        return productRepository.count();
    }
}
