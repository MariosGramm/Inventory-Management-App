package com.company.inventory.simple_inventory.service;

import com.company.inventory.simple_inventory.core.exceptions.EntityAlreadyExistsException;
import com.company.inventory.simple_inventory.core.exceptions.EntityInvalidArgumentException;
import com.company.inventory.simple_inventory.dto.ProductInsertDTO;
import com.company.inventory.simple_inventory.dto.ProductReadOnlyDTO;
import com.company.inventory.simple_inventory.dto.ProductSearchDTO;
import com.company.inventory.simple_inventory.dto.ProductUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {

    ProductReadOnlyDTO saveProduct(ProductInsertDTO productInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    List<ProductReadOnlyDTO> searchProduct(ProductSearchDTO productSearchDTO) throws EntityInvalidArgumentException, EntityNotFoundException;

    ProductReadOnlyDTO updateProduct(String uuid , ProductUpdateDTO productUpdateDTO) throws EntityNotFoundException,EntityInvalidArgumentException,EntityAlreadyExistsException;

    ProductReadOnlyDTO deleteProductByUuid(String uuid) throws EntityNotFoundException,EntityInvalidArgumentException;

    Page<ProductReadOnlyDTO> getPaginatedProducts(int page, int size);
}
