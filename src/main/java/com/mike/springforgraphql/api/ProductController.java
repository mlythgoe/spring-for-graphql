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

    private final ProductRepository productRepository;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductRepository productRepository) {

        this.productRepository = productRepository;

    }

    @QueryMapping("allProducts")  // value (i.e. "allProducts") must match GraphQL schema operation
    public List<Product> findAllProducts() {

        logger.debug("Find All Products");

        List<ProductEntity> productEntities = productRepository.findAll();

        List<Product> products = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {

            products.add(new Product(productEntity.getId(), productEntity.getTitle(),
                    productEntity.getDescription(), productEntity.getPrice()));

        }

        logger.debug("Returning All Products - {}", products);

        return products;

    }

    @QueryMapping("getProduct") // value (i.e. "getProduct") must match GraphQL schema operation
    public Product findProduct(@Argument Long id) {

        logger.debug("Find Product for id {}", id);

        ProductEntity productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            logger.debug("No Product found for id {}", id);

            return null;
        }

        Product product = new Product(productEntity.getId(), productEntity.getTitle(),
                productEntity.getDescription(), productEntity.getPrice());

        logger.debug("Found Product {} for id {}", product, id);

        return product;

    }

    @MutationMapping("saveProduct")
    public Product saveProduct(@Argument ProductInput productInput) {

        if (productInput.id() == null) {

            logger.debug("Insert Product for ProductInput {}", productInput);

        } else {

            logger.debug("Update Product for ProductInput {}", productInput);


        }

        ProductEntity newProductEntity;

        if (productInput.id() == null) {

            newProductEntity = new ProductEntity(productInput.title(),
                    productInput.desc(), productInput.price());

        } else {

            newProductEntity = new ProductEntity(productInput.id(), productInput.title(),
                    productInput.desc(), productInput.price());

        }

        ProductEntity savedProductEntity = productRepository.save(newProductEntity);

        Product product = new Product(
                savedProductEntity.getId(), savedProductEntity.getTitle(),
                savedProductEntity.getDescription(), savedProductEntity.getPrice());

        logger.debug("Created Product {}", product);

        return product;

    }

    @MutationMapping("deleteProduct")
    public Long deleteProduct(@Argument Long id) {

        logger.debug("Delete Product for Id {}", id);

        if (productRepository.existsById(id)) {

            productRepository.deleteById(id);
            logger.debug("Deleted Product for id {}", id);
            return id;

        }

        logger.debug("Product for id {} did not exist so could not be deleted", id);

        return null;

    }
}
