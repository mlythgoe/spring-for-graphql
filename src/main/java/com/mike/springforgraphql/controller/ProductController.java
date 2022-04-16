package com.mike.springforgraphql.controller;

import com.mike.springforgraphql.model.Product;
import com.mike.springforgraphql.model.ProductInput;
import com.mike.springforgraphql.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryMapping(value="allProducts")  // Same as @SchemaMapping(typeName = "Query", value = "allProducts") - it uses the value instead of the method name
    public List<Product> findAllProducts() {
        logger.debug("A DEBUG Message");

        return productRepository.findAll();
    }

    @QueryMapping // Same as @SchemaMapping(typeName = "Query", value = "getProduct") - it uses the method name as the value
    public Product getProduct(@Argument Integer id) {
        return productRepository.findOne(id);
    }

    @MutationMapping // Same as @SchemaMapping(typeName = "Mutation", value = "addProduct") - it uses the method name as the value
    public Product addProduct(@Argument ProductInput productInput) {
        Product newProduct;
        if (productInput.id()==null) {
            newProduct = new Product(ThreadLocalRandom.current().nextInt(1, 9999), productInput.title(), productInput.desc());
        } else {
            newProduct = new Product(productInput.id(), productInput.title(), productInput.desc());
        }
        productRepository.save(newProduct);
        return newProduct;
    }
}
