package com.ecommerce.ecomapp.repositories;

import com.ecommerce.ecomapp.model.Category;
import com.ecommerce.ecomapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Object> findByCategory(Category category);

    Collection<Object> findByProductNameLikeIgnoreCase(String keyword);
}
