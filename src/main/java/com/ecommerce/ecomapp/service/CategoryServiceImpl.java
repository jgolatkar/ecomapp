package com.ecommerce.ecomapp.service;

import com.ecommerce.ecomapp.exceptions.APIException;
import com.ecommerce.ecomapp.exceptions.ResourceNotFoundException;
import com.ecommerce.ecomapp.model.Category;
import com.ecommerce.ecomapp.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories =  categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No categories are available");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category duplicateCategory = categoryRepository.findByCategoryName((category.getCategoryName()));
        if(duplicateCategory != null) {
            throw new APIException(String.format("Category with name '%s' already exist", category.getCategoryName()));
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);
        return "Category " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        category.setCategoryId(categoryId);
        return categoryRepository.save(category);
    }
}
