package com.product.product_service.controller;

import com.product.product_service.dto.ProductDto;
import com.product.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductDto create(@Valid @RequestBody ProductDto product){
        return service.createProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public ProductDto update(@PathVariable Long id,@Valid @RequestBody ProductDto product){
        return service.updateProduct(id, product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.deleteProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id){
        return service.getProductById(id);
    }

    @PreAuthorize("hasRole('ADMIN','USER')")
    @GetMapping
    public Page<ProductDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return service.getAllProducts(pageable);
    }
}
