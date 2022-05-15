package com.mike.springforgraphql;

import com.mike.springforgraphql.api.Product;
import com.mike.springforgraphql.api.ProductController;
import com.mike.springforgraphql.api.ProductInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
record ProductControllerTests(ProductController productController) {

    @Autowired
    ProductControllerTests {
    }

    @Test
    void testFindAllProducts() {

        List<Product> products = productController.findAllProducts();
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(3);

    }

    @Test
    void testFindOneProductThatExists() {

        Product product = productController.findProduct(10L);
        assertThat(product).isNotNull();

    }

    @Test
    void testFindOneProductThatDoesNotExist() {

        Product product = productController.findProduct(99999999L);
        assertThat(product).isNull();

    }

    @Test
    void testSaveProductThatDoesExist() {

        ProductInput productInput = new ProductInput(1L, "testTitle", "testDescription");

        Product product = productController.saveProduct(productInput);
        assertThat(product.id()).isNotNull();

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductInput productInput = new ProductInput(null, "testTitle", "testDescription");

        Product product = productController.saveProduct(productInput);
        assertThat(product.id()).isNotNull();

    }

    @Test
    void testDeleteProductThatExists() {

        Long deletedId = productController.deleteProduct(2L);
        assertThat(deletedId).isNotNull();

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        Long deletedId = productController.deleteProduct(99999999L);
        assertThat(deletedId).isNull();

    }

}
