package com.granzotti.productcrud.model;

import jakarta.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "product_name", nullable = false)
    @NotBlank(message = "O nome do produto não pode ser vazio")
    private String productName;

    @Column(name = "product_description", nullable = false)
    @NotBlank(message = "A descrição do produto não pode ser vazia")
    private String productDescription;

    @Column(name = "product_price", nullable = false)
    @NotNull(message = "O preço do produto não pode ser vazio")
    @Min(value = 0, message = "O preço do produto não pode ser negativo")
    private double productPrice;

    @Column(name = "product_disponibility", nullable = false)
    @NotNull(message = "A disponibilidade do produto não pode ser vazia")
    private boolean productDisponibility;

    public Product(Object productName, Object productDescription, Object productPrice, Object productDisponibility) {
        this.productName = (String) productName;
        this.productDescription = (String) productDescription;
        this.productPrice = (double) productPrice;
        this.productDisponibility = (boolean) productDisponibility;
    }

    public Product() {

    }

    public Object getProductName() {
        return productName;
    }

    public Object getProductDescription() {
        return productDescription;
    }

    public Object getProductPrice() {
        return productPrice;
    }

    public Object isProductDisponibility() {
        return productDisponibility;
    }
}
