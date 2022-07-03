package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.Product;
import com.mike.springforgraphql.model.ProductPriceHistory;
import com.mike.springforgraphql.repository.ProductCustomRepository;
import com.mike.springforgraphql.repository.ProductRepository;
import com.mike.springforgraphql.service.ProductService;
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

    private final ProductService productService;

    private final ProductCustomRepository productCustomRepository;
    private final ProductRepository productRepository;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService, ProductRepository productRepository, ProductCustomRepository productCustomRepository) {

        this.productService = productService;
        this.productRepository = productRepository;
        this.productCustomRepository = productCustomRepository;

    }

    @QueryMapping("allProducts")  // value (i.e. "allProducts") must match GraphQL schema operation
    public List<com.mike.springforgraphql.api.Product> findAllProducts() {

        logger.debug("Find All Products");

        List<Product> productEntities = productRepository.findAll();

        return convertProductEntityListToProductList(productEntities);

    }

    @QueryMapping("getProduct") // value (i.e. "getProduct") must match GraphQL schema operation
    public com.mike.springforgraphql.api.Product findProduct(@Argument Long id) {

        logger.debug("Find Product for id {}", id);

        com.mike.springforgraphql.model.Product productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            logger.debug("No Product found for id {}", id);

            return null;
        }

        com.mike.springforgraphql.api.Product product = new com.mike.springforgraphql.api.Product(productEntity.getId(), productEntity.getTitle(),
                productEntity.getDescription(), productEntity.getPrice(), new ArrayList<>());

        logger.debug("Found Product {} for id {}", product, id);

        return product;

    }

    @QueryMapping("searchProducts") // value (i.e. "searchProducts") must match GraphQL schema operation
    public List<com.mike.springforgraphql.api.Product> searchProducts(@Argument ProductSearchCriteria productSearchCriteria) {

        logger.debug("Search for Products using criteria {}", productSearchCriteria);

        List<com.mike.springforgraphql.model.Product> productEntities;

        productEntities = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteria);

        if (productEntities == null) {
            logger.debug("No Products found for search criteria {}", productSearchCriteria);

            return null;
        }

        return convertProductEntityListToProductList(productEntities);

    }

    @MutationMapping("saveProduct")  // value (i.e. "saveProduct") must match GraphQL schema operation
    public com.mike.springforgraphql.api.Product saveProduct(@Argument ProductInput productInput) {

        if (productInput.id() == null) {

            logger.debug("Insert Product for ProductInput {}", productInput);

        } else {

            logger.debug("Update Product for ProductInput {}", productInput);

        }

        com.mike.springforgraphql.model.Product savedProduct = productService.saveProduct(productInput);

        var productList = new ArrayList<Product>();
        productList.add(savedProduct);

        var apiProductList = convertProductEntityListToProductList(productList);


        logger.debug("Created Product {}", productList.get(0));

        return apiProductList.get(0);

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

    private List<com.mike.springforgraphql.api.Product> convertProductEntityListToProductList(List<Product> products) {

        List<com.mike.springforgraphql.api.Product> apiProducts = new ArrayList<>();

        for (Product product : products) {

            com.mike.springforgraphql.api.Product apiProduct =
                    new com.mike.springforgraphql.api.Product(product.getId(), product.getTitle(),
                            product.getDescription(), product.getPrice(), new ArrayList<>());

            for (ProductPriceHistory productPriceHistory : product.getProductPriceHistories()) {
                apiProduct.productPriceHistories().add(
                        new com.mike.springforgraphql.api.ProductPriceHistory(
                                productPriceHistory.getId(), productPriceHistory.getStartDate(),
                                productPriceHistory.getPrice()
                        )
                );
            }

            apiProducts.add(apiProduct);

        }

        logger.debug("Returning Products - {}", products);

        return apiProducts;
    }
}
