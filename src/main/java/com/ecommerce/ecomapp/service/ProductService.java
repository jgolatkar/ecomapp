package com.ecommerce.ecomapp.service;


import com.ecommerce.ecomapp.model.Product;
import com.ecommerce.ecomapp.payload.ProductDTO;
import com.ecommerce.ecomapp.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(Long categoryId);
}
