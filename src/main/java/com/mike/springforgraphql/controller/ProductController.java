package com.mike.springforgraphql.controller;

import com.mike.springforgraphql.model.Product;
import com.mike.springforgraphql.repository.ProductRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @SchemaMapping(typeName = "Query", value = "allProducts")
    // Define typeName as Query or Mutation, value should map to a query on the GraphQL schema
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @QueryMapping // Same as @SchemaMapping(typeName = "Query", value = "allProducts") - it uses the method name as the value
    public Product getProduct(@Argument Integer id) {
        return productRepository.findOne(id);
    }

    @MutationMapping // Same as @SchemaMapping(typeName = "Mutation", value = "addProduct") - it uses the method name as the value
    public void addProduct(Product product) {
        productRepository.addProduct(product);
    }
}
