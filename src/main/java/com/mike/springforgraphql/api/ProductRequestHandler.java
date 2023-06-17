package com.mike.springforgraphql.api;

import com.mike.springforgraphql.api.input.ProductInput;
import com.mike.springforgraphql.api.input.ProductSearchCriteriaInput;
import com.mike.springforgraphql.api.response.Product;
import com.mike.springforgraphql.api.response.ProductPriceHistory;
import com.mike.springforgraphql.db.entity.ProductEntity;
import com.mike.springforgraphql.db.entity.ProductPriceHistoryEntity;
import com.mike.springforgraphql.db.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
@Controller
public class ProductRequestHandler {

    SimpleDateFormat simpleDateFormat;

    private final ProductService productService;

    private final Random rn;

    public ProductRequestHandler(ProductService productService) {

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        rn = new Random();

        this.productService = productService;

    }

    @QueryMapping("getProduct") // value (i.e. "getProduct") must match GraphQL schema operation
    public Product findProduct(@Argument Long id) {

        log.debug("Find Product for id {}", id);

        var productEntity = productService.findProduct(id);

        if (productEntity == null) {

            log.debug("No Product found for id {}", id);

            return null;

        }

        var product = convertProductEntityToProduct(productEntity);

        log.debug("Found Product {} for id {}", product, id);

        return product;
    }

    @QueryMapping("allProducts") // value (i.e. "allProducts") must match GraphQL schema operation
    public List<Product> findAllProducts() {

        log.debug("Find All Products");

        var productEntities = productService.findAllProducts();

        var products = convertProductEntityListToProductList(productEntities);

        log.debug("Found All Product {}", products);

        return products;

    }

    @QueryMapping("searchProducts") // value (i.e. "searchProducts") must match GraphQL schema
    // operation
    public List<Product> searchProducts(@Argument ProductSearchCriteriaInput productSearchCriteriaInput) {

        log.debug("Search for Products using criteria {}", productSearchCriteriaInput);

        List<ProductEntity> productEntities;

        productEntities = productService.searchProducts(productSearchCriteriaInput);

        if (productEntities == null) {
            log.debug("No Products found for search criteria {}", productSearchCriteriaInput);

            return new ArrayList<>();
        }

        var products = convertProductEntityListToProductList(productEntities);

        log.debug("Found {} Products using criteria {}", products, productSearchCriteriaInput);

        return products;

    }

    @MutationMapping("saveProduct") // value (i.e. "saveProduct") must match GraphQL schema
    // operation
    public Product saveProduct(@Argument ProductInput productInput) {

        if (productInput.id() == null) {

            log.debug("Insert Product for ProductInput {}", productInput);

        } else {

            log.debug("Update Product for ProductInput {}", productInput);

        }

        var savedProduct = productService.saveProduct(productInput);

        var apiProduct = convertProductEntityToProduct(savedProduct);

        log.debug("Created Product {}", apiProduct);

        return apiProduct;

    }

    @MutationMapping("deleteProduct") // value (i.e. "deleteProduct") must match GraphQL schema
    // operation
    public Long deleteProduct(@Argument Long id) {

        log.debug("Delete Product for Id {}", id);

        var deletedId = productService.deleteProduct(id);

        if (deletedId == null) {

            log.debug("Product for id {} did not exist so could not be deleted", id);

        } else {

            log.debug("Product for id {} deleted", id);

        }

        return deletedId;

    }

    private Product convertProductEntityToProduct(ProductEntity productEntity) {

        var product = new Product(productEntity.getId(), productEntity.getTitle(),
                productEntity.getDescription(), productEntity.getPrice(), new ArrayList<>());

        for (ProductPriceHistoryEntity productPriceHistoryEntity : productEntity.getProductPriceHistories()) {

            product.productPriceHistories().add(
                    new ProductPriceHistory(
                            productPriceHistoryEntity.getId(), productPriceHistoryEntity.getStartDate(),
                            productPriceHistoryEntity.getPrice()));

        }

        return product;
    }

    private List<Product> convertProductEntityListToProductList(List<ProductEntity> productEntities) {

        List<Product> apiProducts = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {

            Product apiProduct = convertProductEntityToProduct(productEntity);
            apiProducts.add(apiProduct);

        }

        return apiProducts;
    }

    // A DataFetcher provides the logic to fetch the data for a query or for any schema field.
    // The Spring Boot starter for GraphQL has auto-configurations that automates this registration.
    // For the type of 'Product'
    // when the 'desc' field is being resolved, use this to render the result
    // The @SchemaMapping annotation maps a handler method to a field in the GraphQL schema
    // and declares it to be the DataFetcher for that field.
    // The annotation can specify the parent type name, and the field name
    @SchemaMapping(typeName = "ProductPriceHistory")
    public String startDate(ProductPriceHistory productPriceHistory) {

        return simpleDateFormat.format(productPriceHistory.startDate());

    }

    @SubscriptionMapping("notifyProductPriceChange")
    public Flux<ProductPriceHistory> notifyProductPriceChange(@Argument Long productId) {

        // A flux is the publisher of data
        return Flux.fromStream(
                Stream.generate(() -> {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return new ProductPriceHistory(productId, new Date(),
                            (int) (rn.nextInt(10) + 1 + productId));
                }));

    }

}
