package com.product.product_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Double price;

}
