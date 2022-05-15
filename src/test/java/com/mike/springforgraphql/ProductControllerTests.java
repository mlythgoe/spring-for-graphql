package com.mike.springforgraphql;

import com.mike.springforgraphql.api.Product;
import com.mike.springforgraphql.api.ProductController;
import com.mike.springforgraphql.api.ProductInput;
import graphql.Assert;
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
        Assert.assertNotNull(products);
        assertThat(products.size()).isEqualTo(3);

    }

    @Test
    void testFindOneProductThatExists() {

        Product product = productController.findProduct(10L);
        Assert.assertNotNull(product);

    }

    @Test
    void testFindOneProductThatDoesNotExist() {

        Product product = productController.findProduct(99999999L);
        Assert.assertNull(product);

    }

    @Test
    void testSaveProductThatDoesExist() {

        ProductInput productInput = new ProductInput(1L, "testTitle", "testDescription");

        Product product = productController.saveProduct(productInput);
        Assert.assertNotNull(product.id());

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductInput productInput = new ProductInput(null, "testTitle", "testDescription");

        Product product = productController.saveProduct(productInput);
        Assert.assertNotNull(product.id());

    }

    @Test
    void testDeleteProductThatExists() {

        Long deletedId = productController.deleteProduct(2L);
        Assert.assertNotNull(deletedId);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        Long deletedId = productController.deleteProduct(99999999L);
        Assert.assertNull(deletedId);

    }

}
