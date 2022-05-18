package com.mike.springforgraphql;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.repository.ProductRepository;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Test
    void testFindAllProducts() {

        List<ProductEntity> productEntities = productRepository.findAll();

        assertThat(productEntities).isNotNull();
        assertThat(productEntities.size()).isEqualTo(3);

    }

    @Test
    void testFindProductUsingIdThatExists() {

        Long productId = 1L;

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(productId);

        assertThat(optionalProductEntity).isPresent();
        assertThat(optionalProductEntity.get().getId()).isEqualTo(productId);

    }

    @Test
    void testFindProductUsingIdThatDoesNotExist() {

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(99999999L);

        Assert.assertTrue(optionalProductEntity.isEmpty());

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;

        long countBefore = productRepository.count();

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(productId);

        if (optionalProductEntity.isPresent()) {

            ProductEntity productEntity = new ProductEntity(optionalProductEntity.get().getId(), "testTitle", "testDescription");

            ProductEntity savedProduct = productRepository.save(productEntity);

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

        ProductEntity productEntity = new ProductEntity(null, "testTitle", "testDescription");

        ProductEntity savedProduct = productRepository.save(productEntity);

        assertThat(savedProduct.getId()).isNotNull();

        long countAfter = productRepository.count();

        assertThat(countBefore+1).isEqualTo(countAfter);

    }

    @Test
    void testDeleteProductThatExists() {

        long countBefore = productRepository.count();

        productRepository.deleteById(1L);

        long countAfter = productRepository.count();

        assertThat(countBefore-1).isEqualTo(countAfter);

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
