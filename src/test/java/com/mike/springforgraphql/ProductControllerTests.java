package com.mike.springforgraphql;

import com.mike.springforgraphql.api.Product;
import com.mike.springforgraphql.api.ProductController;
import com.mike.springforgraphql.api.ProductInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductControllerTests {

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
        System.out.println("testFindOneProductUsingIdThatExists" +  product);
        assertThat(product).isNotNull();

    }

    @Test
    void testFindOneProductUsingIdThatDoesNotExist() {

        Product product = productController.findProduct(99999999L);
        assertThat(product).isNull();

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;
        ProductInput productInput = new ProductInput(productId, "testTitle", "testDescription");

        System.out.println("*****testSaveProductUsingIdThatDoesExist 1" +  productInput);

        Product product = productController.saveProduct(productInput);

        System.out.println("*****testSaveProductUsingIdThatDoesExist 2" +  product);


        assertThat(product.id()).isNotNull();
        assertThat(product.id()).isEqualTo(productId);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        Long productId = null;
        ProductInput productInput = new ProductInput(productId, "testTitle", "testDescription");

        System.out.println("******testSaveProductUsingIdThatDoesNotExist 1" +  productInput);

        Product product = productController.saveProduct(productInput);

        System.out.println("******testSaveProductUsingIdThatDoesNotExist" +  product);

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
