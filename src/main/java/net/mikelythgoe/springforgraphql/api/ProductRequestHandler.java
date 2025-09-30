package net.mikelythgoe.springforgraphql.api;

import net.mikelythgoe.springforgraphql.api.input.ProductInput;
import net.mikelythgoe.springforgraphql.api.input.ProductSearchCriteriaInput;
import net.mikelythgoe.springforgraphql.api.response.Product;
import net.mikelythgoe.springforgraphql.api.response.ProductPriceHistory;
import net.mikelythgoe.springforgraphql.db.entity.ProductEntity;
import net.mikelythgoe.springforgraphql.db.entity.ProductPriceHistoryEntity;
import net.mikelythgoe.springforgraphql.db.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@Controller
public class ProductRequestHandler {

    SimpleDateFormat simpleDateFormat;

    private final ProductService productService;

    private final Random rn;

    Logger log = LoggerFactory.getLogger(this.getClass());

    public ProductRequestHandler(ProductService productService) {

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        rn = new Random();

        this.productService = productService;

    }

    @QueryMapping("getProduct") // value (i.e. "getProduct") must match GraphQL schema operation
    public Product findProduct(@Argument String id) {

        log.debug("Find Product for id {}", id);

        var productEntity = productService.findProduct(UUID.fromString(id));

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
    public String deleteProduct(@Argument String id) {

        log.debug("Delete Product for Id {}", id);

        var deletedId = productService.deleteProduct(UUID.fromString(id));

        if (deletedId == null) {

            log.debug("Product for id {} did not exist so could not be deleted", id);

            return null;

        } else {

            log.debug("Product for id {} deleted", id);

            return deletedId.toString();

        }

    }

    // A DataFetcher provides the logic to fetch the data for a query or for any schema field.
    // The Spring Boot starter for GraphQL has autoconfiguration that automates this registration.
    // This method gets called when the ProductPriceHistory response is returning the startDate
    // field.
    // It 'renders' (transforms) the field value.
    // The @SchemaMapping annotation maps a handler method to a field in the GraphQL schema
    // and declares the method to be the DataFetcher for that field.
    // The annotation specifies the parent type name and the field name
    // In this method, the type is ProductPriceHistory, and the field of that type is startDate
    // The typeName annotation and the method name define the type and field
    @SchemaMapping(typeName = "ProductPriceHistory")
    public String startDate(ProductPriceHistory productPriceHistory) {

        return simpleDateFormat.format(productPriceHistory.startDate());

    }

    // Pointless method that just provides another example of schema mapping to refine response
    // data.
    // This method changes the 'desc' property value of the 'Product' type
    @SchemaMapping(typeName = "Product")
    public String desc(Product product) {

        return product.desc() + " Verified";

    }

    @SubscriptionMapping("notifyProductPriceChange")
    public Flux<ProductPriceHistory> notifyProductPriceChange(@Argument String productId) {

        // Flux is the publisher of data
        return Flux.fromStream(
                Stream.generate(() -> {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return new ProductPriceHistory(productId, new Date(),
                            rn.nextInt(1000));
                }));

    }

    private Product convertProductEntityToProduct(ProductEntity productEntity) {

        var product = new Product(productEntity.getId().toString(), productEntity.getTitle(),
                productEntity.getDescription(), productEntity.getPrice(), new ArrayList<>());

        for (ProductPriceHistoryEntity productPriceHistoryEntity : productEntity.getProductPriceHistories()) {

            product.productPriceHistories().add(
                    new ProductPriceHistory(
                            productPriceHistoryEntity.getId().toString(), productPriceHistoryEntity.getStartDate(),
                            productPriceHistoryEntity.getPrice()));

        }

        return product;
    }

    private List<Product> convertProductEntityListToProductList(List<ProductEntity> productEntities) {

        var apiProducts = new ArrayList<Product>();

        for (ProductEntity productEntity : productEntities) {

            var apiProduct = convertProductEntityToProduct(productEntity);
            apiProducts.add(apiProduct);

        }

        return apiProducts;
    }

}
