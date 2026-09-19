package com.product.product_service.service;

import com.product.product_service.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    ProductDto getProductById(Long id);
    Page<ProductDto> getAllProducts(Pageable pageable);
}
