package com.product.product_service.service.impl;

import com.product.product_service.dto.ProductDto;
import com.product.product_service.entity.ProductEntity;
import com.product.product_service.repository.ProductRepository;
import com.product.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    public ProductDto createProduct(ProductDto dto) {
        ProductEntity entity = ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
        entity = repo.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        ProductEntity entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        entity.setName(productDto.getName());
        entity.setDescription(productDto.getDescription());
        entity.setPrice(productDto.getPrice());
        productDto.setId(entity.getId());
        return productDto;
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        ProductEntity entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(entity);
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return repo.findAll(pageable).map(this::mapToDTO);
    }

    private ProductDto mapToDTO(ProductEntity entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
