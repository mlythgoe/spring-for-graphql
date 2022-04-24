package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    @QueryMapping(value = "allProducts")  // - value must match GraphQL schema operation
    public List<Product> findAllProducts() {
        logger.debug("A DEBUG Message");

        List<ProductEntity> productEntities = productRepository.findAll();

        List<Product> products = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            products.add(new Product(productEntity.getId(), productEntity.getTitle(), productEntity.getDescription()));

        }

        return products;
    }

    @QueryMapping(value = "getProduct")
    public Product findProduct(@Argument Long id) {

        ProductEntity productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            return null;
        }

        return new Product(productEntity.getId(), productEntity.getTitle(), productEntity.getDescription());
    }

    @MutationMapping
    public Product addProduct(@Argument ProductInput productInput) {

        ProductEntity newProductEntity;
        if (productInput.id() == null) {
            newProductEntity = new ProductEntity(productInput.title(), productInput.desc());
        } else {
            newProductEntity = new ProductEntity(productInput.id(), productInput.title(), productInput.desc());
        }

        ProductEntity savedProduct = productRepository.save(newProductEntity);
        return new Product(savedProduct.getId(), savedProduct.getTitle(), savedProduct.getDescription());
    }
}
