package com.mike.springforgraphql;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.repository.ProductRepository;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductRepositoryTests {

    private final ProductRepository productRepository;

    @Autowired
    ProductRepositoryTests(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

}
