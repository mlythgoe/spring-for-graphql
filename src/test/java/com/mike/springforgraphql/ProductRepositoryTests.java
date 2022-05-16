package com.mike.springforgraphql;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.repository.ProductRepository;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
record ProductRepositoryTests(ProductRepository productRepository) {

    @Autowired
    ProductRepositoryTests {
    }

    @Test
    void testFindAllProducts() {

        List<ProductEntity> productEntities = productRepository.findAll();
        assertThat(productEntities).isNotNull();
        assertThat(productEntities.size()).isEqualTo(3);


    }

    @Test
    void testFindProductUsingIdThatExists() {

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(1L);
        assertThat(optionalProductEntity).isNotNull();

    }

    @Test
    void testFindProductUsingIdThatDoesNotExist() {

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(99999999L);
        Assert.assertTrue(optionalProductEntity.isEmpty());

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity(productId, "testTitle", "testDescription");

        ProductEntity savedProduct = productRepository.save(productEntity);
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(productId);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        ProductEntity productEntity = new ProductEntity(null, "testTitle", "testDescription");

        ProductEntity savedProduct = productRepository.save(productEntity);
        assertThat(savedProduct.getId()).isNotNull();

    }

    @Test
    void testDeleteProductThatExists() {

        productRepository.deleteById(1L);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        EmptyResultDataAccessException thrown = assertThrows(
                EmptyResultDataAccessException.class,
                () -> productRepository.deleteById(99999999L),
                "Expected deleteById() to throw, but it didn't"
        );

        assertTrue(Objects.requireNonNull(thrown.getMessage())
                .contains("No class com.mike.springforgraphql.model.ProductEntity " +
                "entity with id 99999999 exists!"));

    }

}
