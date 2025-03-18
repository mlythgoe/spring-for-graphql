package net.mikelythgoe.springforgraphql.api;

import net.mikelythgoe.springforgraphql.api.input.ProductInput;
import net.mikelythgoe.springforgraphql.api.input.ProductSearchCriteriaInput;
import net.mikelythgoe.springforgraphql.api.response.Product;
import net.mikelythgoe.springforgraphql.api.response.ProductPriceHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductRequestHandlerTests {

    @Autowired
    ProductRequestHandler productRequestHandler;

    @Test
    void testFindAllProducts() {

        List<Product> products = productRequestHandler.findAllProducts();

        assertThat(products).isNotNull().hasSize(13);

    }

    @Test
    void testFindOneProductUsingIdThatExists() {

        Product product = productRequestHandler.findProduct(1L);

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo(1L);

    }

    @Test
    void testFindOneProductUsingIdThatDoesNotExist() {

        Product product = productRequestHandler.findProduct(99999999L);

        assertThat(product).isNull();

    }

    @Test
    void testFindUsingProductSearchCriteriaForBetweenLowerPriceAndUpperPrice() {

        ProductSearchCriteriaInput productSearchCriteriaInput = new ProductSearchCriteriaInput(null, null, 1, 500);

        List<Product> products = productRequestHandler.searchProducts(productSearchCriteriaInput);

        assertThat(products).hasSize(3);

    }

    @Test
    void testFindUsingProductSearchCriteriaForTitle() {

        ProductSearchCriteriaInput productSearchCriteriaInput = new ProductSearchCriteriaInput("Phone", null, null,
                null);

        List<Product> products = productRequestHandler.searchProducts(productSearchCriteriaInput);

        assertThat(products).hasSize(1);

    }

    @Test
    void testFindUsingProductSearchCriteriaForProductThatDoesNotExist() {

        ProductSearchCriteriaInput productSearchCriteriaInput = new ProductSearchCriteriaInput("ProductThatDoesNotExist", null,
                null,
                null);

        List<Product> products = productRequestHandler.searchProducts(productSearchCriteriaInput);

        assertThat(products).isEmpty();

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;
        ProductInput productInput = new ProductInput(productId, "testTitle", "testDescription", 9999,
                new ArrayList<>());

        Product product = productRequestHandler.saveProduct(productInput);

        assertThat(product.id()).isNotNull();
        assertThat(product.id()).isEqualTo(productId);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductInput productInput = new ProductInput(null, "testTitle", "testDescription", 9999, new ArrayList<>());

        Product product = productRequestHandler.saveProduct(productInput);

        assertThat(product.id()).isNotNull();

    }

    @Test
    void testDeleteProductThatExists() {

        Long idToDelete = 2L;

        Long deletedId = productRequestHandler.deleteProduct(idToDelete);

        assertThat(deletedId).isEqualTo(idToDelete);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        Long deletedId = productRequestHandler.deleteProduct(99999999L);

        assertThat(deletedId).isNull();

    }

    @Test
    void testNotifyProductPriceChang() {

        Flux<ProductPriceHistory> productPriceHistoryStream = productRequestHandler.notifyProductPriceChange(1L);

        Mono<ProductPriceHistory> productPriceHistory = productPriceHistoryStream.next();

        assertThat(productPriceHistory).isNotNull();

        System.out.println(productPriceHistory);

    }

}
