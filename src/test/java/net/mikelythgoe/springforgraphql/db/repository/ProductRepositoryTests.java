package net.mikelythgoe.springforgraphql.db.repository;

import net.mikelythgoe.springforgraphql.api.input.ProductSearchCriteriaInput;
import net.mikelythgoe.springforgraphql.db.entity.ProductEntity;
import net.mikelythgoe.springforgraphql.db.entity.ProductPriceHistoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

        var productEntityEntities = productRepository.findAll();

        assertThat(productEntityEntities).isNotNull().hasSize(13);

    }

    @Test
    void testFindProductUsingIdThatExists() {

        var productId = "01994ebf-796c-78e7-a092-ee42145d4c7a";

        var optionalProductEntity = productRepository.findById(
                UUID.fromString(productId));

        assertThat(optionalProductEntity).isPresent();
        assertThat(optionalProductEntity.get().getId().toString()).isEqualTo(productId);
        assertThat(optionalProductEntity.get().getTitle()).isEqualTo("Microwave");
        assertThat(optionalProductEntity.get().getDescription()).isEqualTo("Goes PING!!!!");
        assertThat(optionalProductEntity.get().getPrice()).isEqualTo(333);

    }

    @Test
    void testFindProductUsingIdThatDoesNotExist() {

        var productId = "01994ebf-796c-78da-953f-16bee52e57ca";

        var optionalProductEntity = productRepository.findById(
                UUID.fromString(productId));

        assertThat(optionalProductEntity).isEmpty();

    }

    @Test
    void testFindUsingProductSearchCriteriaForBetweenLowerPriceAndUpperPrice() {

        var productSearchCriteriaInput = new ProductSearchCriteriaInput(null, null, 1, 500);

        var productEntities = productCustomRepository
                .findUsingProductSearchCriteria(productSearchCriteriaInput);

        assertThat(productEntities).hasSize(4);

    }

    @Test
    void testFindUsingProductSearchCriteriaForTitle() {

        var productSearchCriteriaInput = new ProductSearchCriteriaInput("Phone", null, null,
                null);

        var productEntities = productCustomRepository
                .findUsingProductSearchCriteria(productSearchCriteriaInput);

        assertThat(productEntities).hasSize(1);

    }

    @Test
    void testSaveProductUsingIdThatDoesExist() {

        var productId = UUID.fromString("01994ebf-796c-7b66-b495-0d2a187a0c57");

        var countBefore = productRepository.count();

        var optionalProductEntity = productRepository.findById(productId);

        if (optionalProductEntity.isPresent()) {

            ProductEntity productEntity = new ProductEntity(optionalProductEntity.get().getId(), "testTitle",
                    "testDescription", 9999);

            ProductEntity savedProductEntity = productRepository.save(productEntity);

            assertThat(savedProductEntity.getId()).isEqualTo(productId);

        } else {

            fail("Product did not already exist");

        }

        var countAfter = productRepository.count();

        assertThat(countBefore).isEqualTo(countAfter);

    }

    @Test
    void testSaveProductThatDoesNotExist() {

        var countBefore = productRepository.count();

        var productEntity = new ProductEntity(null, "testTitle", "testDescription", 9999);

        var productPriceHistoryEntity1 = new ProductPriceHistoryEntity(
                Instant.now(), 14, productEntity);
        productEntity.getProductPriceHistories().add(productPriceHistoryEntity1);

        var productPriceHistoryEntity2 = new ProductPriceHistoryEntity(
                Instant.now(), 20, productEntity);
        productEntity.getProductPriceHistories().add(productPriceHistoryEntity2);

        productRepository.save(productEntity);

        assertThat(productEntity.getId()).isNotNull();

        var countAfter = productRepository.count();

        assertThat(countBefore + 1).isEqualTo(countAfter);

        var getProductEntity = productRepository.findById(productEntity.getId());

        assertThat(getProductEntity).isPresent();
        assertThat(getProductEntity.get().getProductPriceHistories()).hasSize(2);

    }

    @Test
    void testDeleteProductThatExists() {

        var countBefore = productRepository.count();

        productRepository.deleteById(UUID.fromString("01994ebf-796c-74eb-b603-b24c5cc1e265"));

        var countAfter = productRepository.count();

        assertThat(countBefore - 1).isEqualTo(countAfter);

    }

    @Test
    void testDeleteProductThatDoesNotExist() {

        var countBefore = productRepository.count();

        assertDoesNotThrow(
                () -> productRepository.deleteById(UUID.fromString("01994ebf-796c-7aea-8f55-37a92c66e626")));

        var countAfter = productRepository.count();

        assertThat(countBefore).isEqualTo(countAfter);

    }

}
