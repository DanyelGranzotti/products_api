package com.granzotti.productcrud.controller;

import com.granzotti.productcrud.model.Product;
import com.granzotti.productcrud.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Tag(name = "Product", description = "Product API")
@RestController
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Validated
    @PostMapping("/products")
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "500", description = "An error occurred while saving the product")
    })
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while saving the product", e);
        }
    }

    @GetMapping("/products")
    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found"),
            @ApiResponse(responseCode = "204", description = "No products found")
    })
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = switch (sort) {
                case "priceasc" -> PageRequest.of(page, size, Sort.by("productPrice").ascending());
                case "pricedesc" -> PageRequest.of(page, size, Sort.by("productPrice").descending());
                default -> PageRequest.of(page, size, Sort.by("productName"));
            };

            Page<Product> products = productRepository.findAll(pageable);

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get a product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
        try {
            return productRepository.findById(id)
                    .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @PutMapping("/products/{id}")
    @Operation(summary = "Update a product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @Valid @RequestBody Product product) {
        try {
            return productRepository.findById(id)
                    .map(record -> {
                        record.setProductName(product.getProductName());
                        record.setProductDescription(product.getProductDescription());
                        record.setProductPrice(product.getProductPrice());
                        record.setProductDisponibility(product.isProductDisponibility());
                        Product updated = productRepository.save(record);
                        return new ResponseEntity<>(updated, HttpStatus.OK);
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete a product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
        try {
            return productRepository.findById(id)
                    .map(record -> {
                        productRepository.deleteById(id);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }
}