package com.mike.springforgraphql;

import com.mike.springforgraphql.api.ProductSearchCriteria;
import com.mike.springforgraphql.model.Product;
import com.mike.springforgraphql.model.ProductPriceHistory;
import com.mike.springforgraphql.repository.ProductCustomRepository;
import com.mike.springforgraphql.repository.ProductPriceHistoryRepository;
import com.mike.springforgraphql.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCustomRepository productCustomRepository;

    @Autowired
    ProductPriceHistoryRepository productPriceHistoryRepository;

    @Test
    void testFindAllProducts() {

        List<Product> productEntities = productRepository.findAll();

        assertThat(productEntities).isNotNull();
        assertThat(productEntities.size()).isEqualTo(3);

    }

    @Test
    void testFindProductUsingIdThatExists() {

        Long productId = 1L;

        Optional<Product> optionalProductEntity = productRepository.findById(productId);

        assertThat(optionalProductEntity).isPresent();
        assertThat(optionalProductEntity.get().getId()).isEqualTo(productId);

    }

    @Test
    void testFindProductUsingIdThatDoesNotExist() {

        Optional<Product> optionalProductEntity = productRepository.findById(99999999L);

        assertThat(optionalProductEntity).isEmpty();

    }

    @Test
    void testFindUsingProductSearchCriteriaForBetweenLowerPriceAndUpperPrice() {

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria(null, null, 1, 500);

        List<Product> products = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteria);

        assertThat(products.size()).isEqualTo(2);

    }

    @Test
    void testFindUsingProductSearchCriteriaForTitle() {

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria("Phone", null, null, null);

        List<Product> products = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteria);

        assertThat(products.size()).isEqualTo(1);

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;

        long countBefore = productRepository.count();

        Optional<Product> optionalProductEntity = productRepository.findById(productId);

        if (optionalProductEntity.isPresent()) {

            Product product = new Product(optionalProductEntity.get().getId(), "testTitle", "testDescription", 9999);

            Product savedProduct = productRepository.save(product);

            assertThat(savedProduct.getId()).isEqualTo(productId);

        } else {

            fail("Product did not already exist");

        }

        long countAfter = productRepository.count();

        assertThat(countBefore).isEqualTo(countAfter);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        long countBefore = productRepository.count();

        Product product = new Product(null, "testTitle", "testDescription", 9999);

        // Save Parent
        Product savedProduct = productRepository.save(product);

        ProductPriceHistory productPriceHistory = new ProductPriceHistory(Date.valueOf(LocalDate.now()), 11, savedProduct);

        // Save Child
        ProductPriceHistory savedProductPriceHistory = productPriceHistoryRepository.save(productPriceHistory);

        // Add Child - needed - if you don't do it, the child is not persisted
        savedProduct.getProductPriceHistories().add(productPriceHistory);

        assertThat(savedProduct.getId()).isNotNull();

        long countAfter = productRepository.count();

        assertThat(countBefore + 1).isEqualTo(countAfter);

        Optional<Product> getProductEntity = productRepository.findById(savedProduct.getId());

        assertThat(getProductEntity.isPresent());

    }

    @Test
    void testDeleteProductThatExists() {

        long countBefore = productRepository.count();

        productRepository.deleteById(1L);

        long countAfter = productRepository.count();

        assertThat(countBefore - 1).isEqualTo(countAfter);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        EmptyResultDataAccessException thrown =
                assertThrows(EmptyResultDataAccessException.class, () ->
                        productRepository.deleteById(99999999L),
                        "Expected deleteById() to throw, but it didn't");

        assertThat(Objects.requireNonNull(thrown.getMessage())
                .contains("No class com.mike.springforgraphql.model.Product entity with id 99999999 exists!"))
                .isTrue();

    }


}
