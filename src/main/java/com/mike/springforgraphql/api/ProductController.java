package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.repository.ProductCustomRepository;
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

    private final ProductCustomRepository productCustomRepository;
    private final ProductRepository productRepository;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductRepository productRepository, ProductCustomRepository productCustomRepository) {

        this.productRepository = productRepository;
        this.productCustomRepository = productCustomRepository;

    }

    @QueryMapping("allProducts")  // value (i.e. "allProducts") must match GraphQL schema operation
    public List<Product> findAllProducts() {

        logger.debug("Find All Products");

        List<ProductEntity> productEntities = productRepository.findAll();

        return convertProductEntityListToProductList(productEntities);

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

    @QueryMapping("searchProducts") // value (i.e. "searchProducts") must match GraphQL schema operation
    public List<Product> searchProducts(@Argument ProductSearchCriteria productSearchCriteria) {

        logger.debug("Search for Products using criteria {}", productSearchCriteria);

        List<ProductEntity> productEntities;

        productEntities = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteria);

        if (productEntities == null) {
            logger.debug("No Products found for search criteria {}", productSearchCriteria);

            return null;
        }

        return convertProductEntityListToProductList(productEntities);

    }

    @MutationMapping("saveProduct")  // value (i.e. "saveProduct") must match GraphQL schema operation
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

    @MutationMapping("deleteProduct") // value (i.e. "deleteProduct") must match GraphQL schema operation
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

    private List<Product> convertProductEntityListToProductList(List<ProductEntity> productEntities) {

        List<Product> products = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {

            products.add(new Product(productEntity.getId(), productEntity.getTitle(),
                    productEntity.getDescription(), productEntity.getPrice()));

        }

        logger.debug("Returning All Products - {}", products);

        return products;
    }
}
