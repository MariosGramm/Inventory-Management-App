package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.dto.ProductInsertDTO;
import com.company.inventory.simple_inventory.dto.ProductReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.ProductSearchDTO;
import com.company.inventory.simple_inventory.dto.ProductUpdateDTO;
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
import org.springframework.stereotype.Service;

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
            if ((productRepository.findByName(dto.getName())).isPresent()){
                throw new EntityAlreadyExistsException("Product","Product with name " + dto.getName() + " already exists");
            }

            Product product = mapper.mapToProductEntity(dto);

            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Warehouse","Invalid warehouse id"));

            Inventory inventory = mapper.mapToInventoryEntity(dto, product, warehouse);






        }
    }

    @Override
    public ProductReadOnlyDTO searchProduct(ProductSearchDTO productSearchDTO) throws EntityInvalidArgumentException, EntityNotFoundException {
        return null;
    }

    @Override
    public ProductReadOnlyDTO updateProduct(ProductUpdateDTO productUpdateDTO) throws EntityNotFoundException, EntityInvalidArgumentException {
        return null;
    }

    @Override
    public ProductReadOnlyDTO deleteProductByUuid(String uuid) throws EntityNotFoundException, EntityInvalidArgumentException {
        return null;
    }

    @Override
    public Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size) {
        return null;
    }
}
