package com.mike.springforgraphql;

import com.mike.springforgraphql.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductEntityControllerTests {

    @Autowired
    ProductController productController;

    @Test
    void testFindAllProducts() {

        List<Product> products = productController.findAllProducts();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(3);

    }

    @Test
    void testFindOneProductUsingIdThatExists() {

        Product product = productController.findProduct(1L);

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo(1L);

    }

    @Test
    void testFindOneProductUsingIdThatDoesNotExist() {

        Product product = productController.findProduct(99999999L);

        assertThat(product).isNull();

    }

    @Test
    void testFindUsingProductSearchCriteriaForBetweenLowerPriceAndUpperPrice() {

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria(null, null, 1, 500);

        List<Product> products = productController.searchProducts(productSearchCriteria);

        assertThat(products.size()).isEqualTo(2);

    }

    @Test
    void testFindUsingProductSearchCriteriaForTitle() {

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria("Phone", null, null, null);

        List<Product> products = productController.searchProducts(productSearchCriteria);

        assertThat(products.size()).isEqualTo(1);

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;
        ProductInput productInput = new ProductInput(productId, "testTitle", "testDescription", 9999, new ArrayList<>());

        Product product = productController.saveProduct(productInput);

        assertThat(product.id()).isNotNull();
        assertThat(product.id()).isEqualTo(productId);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductInput productInput = new ProductInput(null, "testTitle", "testDescription", 9999, new ArrayList<>());

        Product product = productController.saveProduct(productInput);

        assertThat(product.id()).isNotNull();

    }

    @Test
    void testDeleteProductThatExists() {

        Long idToDelete = 2L;

        Long deletedId = productController.deleteProduct(idToDelete);

        assertThat(deletedId).isEqualTo(idToDelete);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        Long deletedId = productController.deleteProduct(99999999L);

        assertThat(deletedId).isNull();

    }

}