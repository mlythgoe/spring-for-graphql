package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.model.ProductPriceHistoryEntity;
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

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {

        this.productService = productService;

    }

    @QueryMapping("getProduct") // value (i.e. "getProduct") must match GraphQL schema operation
    public Product findProduct(@Argument Long id) {

        logger.debug("Find Product for id {}", id);

        ProductEntity productEntity = productService.findProduct(id);

        if ( productEntity == null ) {

            logger.debug("No Product found for id {}", id);

            return null;

        }

        Product product = convertProductEntityToProduct(productEntity);

        logger.debug("Found Product {} for id {}", product, id);

        return product;
    }

    @QueryMapping("allProducts")  // value (i.e. "allProducts") must match GraphQL schema operation
    public List<Product> findAllProducts() {

        logger.debug("Find All Products");

        List<ProductEntity> productEntities = productService.findAllProducts();

        List<Product> products = convertProductEntityListToProductList(productEntities);

        logger.debug("Found All Product {}", products);

        return products;

    }

    @QueryMapping("searchProducts") // value (i.e. "searchProducts") must match GraphQL schema operation
    public List<Product> searchProducts(@Argument ProductSearchCriteriaInput productSearchCriteriaInput) {

        logger.debug("Search for Products using criteria {}", productSearchCriteriaInput);

        List<ProductEntity> productEntities;

        productEntities = productService.searchProducts(productSearchCriteriaInput);

        if (productEntities == null) {
            logger.debug("No Products found for search criteria {}", productSearchCriteriaInput);

            return null;
        }

        List<Product> products = convertProductEntityListToProductList(productEntities);

        logger.debug("Found {} Products using criteria {}", products, productSearchCriteriaInput);

        return products;

    }

    @MutationMapping("saveProduct")  // value (i.e. "saveProduct") must match GraphQL schema operation
    public Product saveProduct(@Argument ProductInput productInput) {

        if (productInput.id() == null) {

            logger.debug("Insert Product for ProductInput {}", productInput);

        } else {

            logger.debug("Update Product for ProductInput {}", productInput);

        }

        ProductEntity savedProduct = productService.saveProduct(productInput);

        Product apiProduct = convertProductEntityToProduct(savedProduct);

        logger.debug("Created Product {}", apiProduct);

        return apiProduct;

    }

    @MutationMapping("deleteProduct") // value (i.e. "deleteProduct") must match GraphQL schema operation
    public Long deleteProduct(@Argument Long id) {

        logger.debug("Delete Product for Id {}", id);

        var deletedId = productService.deleteProduct(id);

        if (deletedId == null ) {
            logger.debug("Product for id {} did not exist so could not be deleted", id);

        } else {
            logger.debug("Product for id {} deleted", id);

        }

        return deletedId;

    }

    private Product convertProductEntityToProduct(ProductEntity productEntity) {

            Product product =
                    new Product(productEntity.getId(), productEntity.getTitle(),
                            productEntity.getDescription(), productEntity.getPrice(), new ArrayList<>());

            for (ProductPriceHistoryEntity productPriceHistoryEntity : productEntity.getProductPriceHistories()) {

                product.productPriceHistories().add(
                        new ProductPriceHistory(
                                productPriceHistoryEntity.getId(), productPriceHistoryEntity.getStartDate(),
                                productPriceHistoryEntity.getPrice()
                        )
                );

            }

        return product;
    }

    private List<Product> convertProductEntityListToProductList(List<ProductEntity> productEntities) {

        List<Product> apiProducts = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {

            Product apiProduct =
                    new Product(productEntity.getId(), productEntity.getTitle(),
                            productEntity.getDescription(), productEntity.getPrice(), new ArrayList<>());

            for (ProductPriceHistoryEntity productPriceHistoryEntity : productEntity.getProductPriceHistories()) {

                apiProduct.productPriceHistories().add(
                        new ProductPriceHistory(
                                productPriceHistoryEntity.getId(), productPriceHistoryEntity.getStartDate(),
                                productPriceHistoryEntity.getPrice()
                        )
                );

            }

            apiProducts.add(apiProduct);

        }

        return apiProducts;
    }
}
