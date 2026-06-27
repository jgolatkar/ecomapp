package com.ecommerce.ecomapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 2, max = 20)
    private String productName;

    private String image;

    @NotBlank
    @Size(min = 10, max = 50)
    private String description;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @PositiveOrZero
    private Double specialPrice;

    @NotNull
    @PositiveOrZero
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}