package com.ecommerce.ecomapp.service;

import com.ecommerce.ecomapp.payload.CategoryDTO;
import com.ecommerce.ecomapp.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO updatedCategory, Long categoryId);
}
