package net.mikelythgoe.springforgraphql.api;

import net.mikelythgoe.springforgraphql.api.input.ProductInput;
import net.mikelythgoe.springforgraphql.api.input.ProductSearchCriteriaInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductRequestHandlerTests {

    @Autowired
    ProductRequestHandler productRequestHandler;

    @Test
    void testFindAllProducts() {

        var products = productRequestHandler.findAllProducts();

        assertThat(products).isNotNull().hasSize(13);

    }

    @Test
    void testFindOneProductUsingIdThatExists() {

        var product = productRequestHandler.findProduct("01994ebf-796c-74eb-b603-b24c5cc1e265");

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo("01994ebf-796c-74eb-b603-b24c5cc1e265");

    }

    @Test
    void testFindOneProductUsingIdThatDoesNotExist() {

        var product = productRequestHandler.findProduct("01994eec-a6cf-7b03-844c-6bbc7bec0609");

        assertThat(product).isNull();

    }

    @Test
    void testFindUsingProductSearchCriteriaForBetweenLowerPriceAndUpperPrice() {

        var productSearchCriteriaInput = new ProductSearchCriteriaInput(null, null, 1, 500);

        var products = productRequestHandler.searchProducts(productSearchCriteriaInput);

        assertThat(products).hasSize(4);

    }

    @Test
    void testFindUsingProductSearchCriteriaForTitle() {

        var productSearchCriteriaInput = new ProductSearchCriteriaInput("Phone", null, null,
                null);

        var products = productRequestHandler.searchProducts(productSearchCriteriaInput);

        assertThat(products).hasSize(1);

    }

    @Test
    void testFindUsingProductSearchCriteriaForProductThatDoesNotExist() {

        var productSearchCriteriaInput = new ProductSearchCriteriaInput("ProductThatDoesNotExist", null,
                null,
                null);

        var products = productRequestHandler.searchProducts(productSearchCriteriaInput);

        assertThat(products).isEmpty();

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        var productId = "01994ebf-796c-7d3f-9851-fda7646e831a";
        var productInput = new ProductInput(productId, "testTitle", "testDescription", 9999,
                new ArrayList<>());

        var product = productRequestHandler.saveProduct(productInput);

        assertThat(product.id()).isNotNull();
        assertThat(product.id()).isEqualTo(productId);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        var productInput = new ProductInput(null, "testTitle", "testDescription", 9999, new ArrayList<>());

        var product = productRequestHandler.saveProduct(productInput);

        assertThat(product.id()).isNotNull();

    }

    @Test
    void testDeleteProductThatExists() {

        var idToDelete = "01994ebf-796c-7b66-b495-0d2a187a0c57";

        var deletedId = productRequestHandler.deleteProduct(idToDelete);

        assertThat(deletedId).isEqualTo(idToDelete);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        var idToDelete = UUID.randomUUID().toString();

        var deletedId = productRequestHandler.deleteProduct(idToDelete);

        assertThat(deletedId).isNull();

    }

}
