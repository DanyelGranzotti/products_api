package com.granzotti.productcrud.repository;

import com.granzotti.productcrud.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
   
    List<Product> findAllByOrderByProductName();

    List<Product> findAllByOrderByProductPriceAsc();

    List<Product> findAllByOrderByProductPriceDesc();
}
