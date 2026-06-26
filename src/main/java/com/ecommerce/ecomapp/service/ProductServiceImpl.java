package com.ecommerce.ecomapp.service;

import com.ecommerce.ecomapp.exceptions.ResourceNotFoundException;
import com.ecommerce.ecomapp.model.Category;
import com.ecommerce.ecomapp.model.Product;
import com.ecommerce.ecomapp.payload.ProductDTO;
import com.ecommerce.ecomapp.payload.ProductResponse;
import com.ecommerce.ecomapp.repositories.CategoryRepository;
import com.ecommerce.ecomapp.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper productModelMapper;

    public ProductServiceImpl(CategoryRepository categoryRepository,
                              ProductRepository productRepository,
                              ModelMapper productModelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productModelMapper = productModelMapper;
    }

    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = calculateSpecialPrice(product.getPrice(), product.getDiscount());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        return productModelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<ProductDTO> productDTOS = productRepository.findAll().stream()
                .map(product -> productModelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<ProductDTO> productDTOS = productRepository.findByCategory(category).stream()
                .map(product -> productModelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    private static double calculateSpecialPrice(double price, double discount) {
        return price * (1 - discount / 100.0);
    }
}
