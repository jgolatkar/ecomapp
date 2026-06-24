package com.ecommerce.ecomapp.controller;

import com.ecommerce.ecomapp.payload.CategoryDTO;
import com.ecommerce.ecomapp.payload.CategoryResponse;
import com.ecommerce.ecomapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse  categoryResponse = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category,
                                                   @PathVariable Long categoryId) {
            CategoryDTO updatedCategoryDTO = categoryService.updateCategory(category, categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCategoryDTO);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
            CategoryDTO deleteCategoryDTO = categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(deleteCategoryDTO);
    }
}