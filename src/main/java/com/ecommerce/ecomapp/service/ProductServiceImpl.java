package com.ecommerce.ecomapp.service;

import com.ecommerce.ecomapp.config.AppConstants;
import com.ecommerce.ecomapp.exceptions.APIException;
import com.ecommerce.ecomapp.exceptions.ResourceNotFoundException;
import com.ecommerce.ecomapp.model.Category;
import com.ecommerce.ecomapp.model.Product;
import com.ecommerce.ecomapp.payload.ProductDTO;
import com.ecommerce.ecomapp.payload.ProductResponse;
import com.ecommerce.ecomapp.repositories.CategoryRepository;
import com.ecommerce.ecomapp.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final StorageService storageService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper productModelMapper;

    public ProductServiceImpl(StorageService storageService, CategoryRepository categoryRepository,
                              ProductRepository productRepository,
                              ModelMapper productModelMapper) {
        this.storageService = storageService;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productModelMapper = productModelMapper;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (Product product : products) {
            if(product.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if(!isProductNotPresent) {
            throw new APIException(String.format("Product with name '%s' already exist", productDTO.getProductName()));
        }

        Product product = productModelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = calculateSpecialPrice(product.getPrice(), product.getDiscount());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        return productModelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sorter = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sorter);
        Page<Product> productPage = productRepository.findAll(pageRequest);

        List<ProductDTO> productDTOS = productPage.getContent().stream()
                .map(product -> productModelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sorter = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sorter);
        Page<Product> productPage = productRepository.findByCategory(category, pageRequest);

        List<ProductDTO> productDTOS = productPage.getContent().stream()
                .map(product -> productModelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sorter = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sorter);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageRequest);

        List<ProductDTO> productDTOS = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageRequest).stream()
                .map(product -> productModelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = productModelMapper.map(productDTO, Product.class);

        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));

        Product savedProduct = productRepository.save(existingProduct);

        return productModelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(existingProduct);

        return productModelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(MultipartFile image, Long productId) throws IOException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String filename = storageService.uploadImage(AppConstants.PRODUCT_IMAGE_PATH, image);

        existingProduct.setImage(filename);

        Product savedProduct = productRepository.save(existingProduct);

        return productModelMapper.map(savedProduct, ProductDTO.class);
    }

    private static double calculateSpecialPrice(double price, double discount) {
        return price * (1 - discount / 100.0);
    }
}
