package com.mike.springforgraphql;

import com.mike.springforgraphql.api.input.ProductSearchCriteriaInput;
import com.mike.springforgraphql.db.entity.ProductEntity;
import com.mike.springforgraphql.db.entity.ProductPriceHistoryEntity;
import com.mike.springforgraphql.db.repository.ProductCustomRepository;
import com.mike.springforgraphql.db.repository.ProductPriceHistoryRepository;
import com.mike.springforgraphql.db.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
class ProductEntityRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCustomRepository productCustomRepository;

    @Autowired
    ProductPriceHistoryRepository productPriceHistoryRepository;

    @Test
    void testFindAllProducts() {

        List<ProductEntity> productEntityEntities = productRepository.findAll();

        assertThat(productEntityEntities).isNotNull().hasSize(13);

    }

    @Test
    void testFindProductUsingIdThatExists() {

        Long productId = 3L;

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(productId);

        assertThat(optionalProductEntity).isPresent();
        assertThat(optionalProductEntity.get().getId()).isEqualTo(productId);
        assertThat(optionalProductEntity.get().getTitle()).isEqualTo("Microwave");
        assertThat(optionalProductEntity.get().getDescription()).isEqualTo("Goes PING!!!!");
        assertThat(optionalProductEntity.get().getPrice()).isEqualTo(444);

    }

    @Test
    void testFindProductUsingIdThatDoesNotExist() {

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(99999999L);

        assertThat(optionalProductEntity).isEmpty();

    }

    @Test
    void testFindUsingProductSearchCriteriaForBetweenLowerPriceAndUpperPrice() {

        ProductSearchCriteriaInput productSearchCriteriaInput = new ProductSearchCriteriaInput(null, null, 1, 500);

        List<ProductEntity> productEntities = productCustomRepository
                .findUsingProductSearchCriteria(productSearchCriteriaInput);

        assertThat(productEntities).hasSize(3);

    }

    @Test
    void testFindUsingProductSearchCriteriaForTitle() {

        ProductSearchCriteriaInput productSearchCriteriaInput = new ProductSearchCriteriaInput("Phone", null, null,
                null);

        List<ProductEntity> productEntities = productCustomRepository
                .findUsingProductSearchCriteria(productSearchCriteriaInput);

        assertThat(productEntities).hasSize(1);

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        Long productId = 1L;

        long countBefore = productRepository.count();

        Optional<ProductEntity> optionalProductEntity = productRepository.findById(productId);

        if (optionalProductEntity.isPresent()) {

            ProductEntity productEntity = new ProductEntity(optionalProductEntity.get().getId(), "testTitle",
                    "testDescription", 9999);

            ProductEntity savedProductEntity = productRepository.save(productEntity);

            assertThat(savedProductEntity.getId()).isEqualTo(productId);

        } else {

            fail("Product did not already exist");

        }

        long countAfter = productRepository.count();

        assertThat(countBefore).isEqualTo(countAfter);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        long countBefore = productRepository.count();

        ProductEntity productEntity = new ProductEntity(null, "testTitle", "testDescription", 9999);

        ProductPriceHistoryEntity productPriceHistoryEntity1 = new ProductPriceHistoryEntity(
                new Timestamp(System.currentTimeMillis()), 14, productEntity);
        productEntity.getProductPriceHistories().add(productPriceHistoryEntity1);

        ProductPriceHistoryEntity productPriceHistoryEntity2 = new ProductPriceHistoryEntity(
                new Timestamp(System.currentTimeMillis()), 20, productEntity);
        productEntity.getProductPriceHistories().add(productPriceHistoryEntity2);

        productRepository.save(productEntity);

        assertThat(productEntity.getId()).isNotNull();

        long countAfter = productRepository.count();

        assertThat(countBefore + 1).isEqualTo(countAfter);

        Optional<ProductEntity> getProductEntity = productRepository.findById(productEntity.getId());

        assertThat(getProductEntity).isPresent();
        assertThat(getProductEntity.get().getProductPriceHistories()).hasSize(2);

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

        long countBefore = productRepository.count();

        assertDoesNotThrow(
                () -> productRepository.deleteById(99999999L)
        );

        long countAfter = productRepository.count();

        assertThat(countBefore).isEqualTo(countAfter);

    }

}
