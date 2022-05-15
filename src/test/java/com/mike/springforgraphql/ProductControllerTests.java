package com.mike.springforgraphql;

import com.mike.springforgraphql.api.Product;
import com.mike.springforgraphql.api.ProductController;
import com.mike.springforgraphql.api.ProductInput;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
record ProductControllerTests(ProductController productController) {

    @Autowired
    ProductControllerTests {
    }

    @Test
    void testControllerFindAllProducts() {

        List<Product> products = productController.findAllProducts();
        Assert.assertNotNull(products);

    }

    @Test
    void testFindOneProductThatExists() {

        Product product = productController.findProduct(1L);
        Assert.assertNotNull(product);

    }

    @Test
    void testFindOneProductThatDoesNotExist() {

        Product product = productController.findProduct(99999999L);
        Assert.assertNull(product);

    }

    @Test
    void testSaveProductThatDoesExist() {

        ProductInput productInput = new ProductInput(1L, "testTitle", "testDescriptiopn");

        Product product = productController.saveProduct(productInput);
        Assert.assertNotNull(product.id());

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductInput productInput = new ProductInput(null, "testTitle", "testDescriptiopn");

        Product product = productController.saveProduct(productInput);
        Assert.assertNotNull(product);

    }


}
