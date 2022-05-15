package com.mike.springforgraphql;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.repository.ProductRepository;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
record ProductRepositoryTests(ProductRepository productRepository) {

    @Autowired
    ProductRepositoryTests {
    }

    @Test
    void testyFindAllProducts() {

        List<ProductEntity> productEntities = productRepository.findAll();
        Assert.assertNotNull(productEntities);

    }

    @Test
    void testyFindProductUsingIdThatExists() {

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(1L);
        Assert.assertNotNull(optionalProductEntity);

    }

    @Test
    void testyFindProductUsingIdThatDoesNotExist() {

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(99999999L);
        Assert.assertTrue(optionalProductEntity.isEmpty());

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

        assertTrue(thrown.getMessage().contains("No class com.mike.springforgraphql.model.ProductEntity " +
                "entity with id 99999999 exists!"));

    }

}
