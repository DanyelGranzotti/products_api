package com.granzotti.productcrud.service;

import com.granzotti.productcrud.model.Product;
import com.granzotti.productcrud.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProducts(String Filter) {
        return switch (Filter) {
            case "name" -> productRepository.findAllByOrderByProductName();
            case "priceasc" -> productRepository.findAllByOrderByProductPriceAsc();
            case "pricedesc" -> productRepository.findAllByOrderByProductPriceDesc();
            default -> productRepository.findAll();
        };
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }
}