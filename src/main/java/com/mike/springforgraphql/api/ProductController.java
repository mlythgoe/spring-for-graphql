package com.mike.springforgraphql.api;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.model.ProductPriceHistoryEntity;
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

    @QueryMapping("getProduct") // value (i.e. "getProduct") must match GraphQL schema operation
    public Product findProduct(@Argument Long id) {

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

        List<ProductEntity> productEntityEntities = productService.findAllProducts();

        return convertProductEntityListToProductList(productEntityEntities);

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

        return convertProductEntityListToProductList(productEntities);

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

        logger.debug("Converting ProductEntity {} to Product", productEntity);

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

        logger.debug("Returning Product - {}", product);

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

        logger.debug("Returning Products - {}", productEntities);

        return apiProducts;
    }
}
