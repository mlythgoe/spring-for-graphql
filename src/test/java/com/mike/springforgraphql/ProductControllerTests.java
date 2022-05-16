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
    void testFindOneProductUsingIdThatExists() {

        Product product = productController.findProduct(1L);
        assertThat(product).isNotNull();

    }

    @Test
    void testFindOneProductUsingIdThatDoesNotExist() {

        Product product = productController.findProduct(99999999L);
        assertThat(product).isNull();

    }

    @Test
    void testSaveProductThatDoesExist() {

        Long productId = 1L;
        ProductInput productInput = new ProductInput(productId, "testTitle", "testDescription");

        Product product = productController.saveProduct(productInput);
        assertThat(product.id()).isNotNull();
        assertThat(product.id()).isEqualTo(productId);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductInput productInput = new ProductInput(null, "testTitle", "testDescription");

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
