package net.mikelythgoe.springforgraphql.db.service;

import net.mikelythgoe.springforgraphql.api.input.ProductInput;
import net.mikelythgoe.springforgraphql.api.input.ProductPriceHistoryInput;
import net.mikelythgoe.springforgraphql.api.input.ProductSearchCriteriaInput;
import net.mikelythgoe.springforgraphql.db.entity.ProductEntity;
import net.mikelythgoe.springforgraphql.db.entity.ProductPriceHistoryEntity;
import net.mikelythgoe.springforgraphql.db.repository.ProductCustomRepository;
import net.mikelythgoe.springforgraphql.db.repository.ProductPriceHistoryRepository;
import net.mikelythgoe.springforgraphql.db.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    ProductRepository productRepository;

    ProductPriceHistoryRepository productPriceHistoryRepository;

    ProductCustomRepository productCustomRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCustomRepository productCustomRepository,
            ProductPriceHistoryRepository productPriceHistoryRepository) {

        this.productRepository = productRepository;
        this.productPriceHistoryRepository = productPriceHistoryRepository;
        this.productCustomRepository = productCustomRepository;

    }

    public ProductEntity saveProduct(ProductInput productInput) {

        ProductEntity newProductEntity;

        if (productInput.id() == null) {
            newProductEntity = new ProductEntity(productInput.title(),
                    productInput.desc(), productInput.price());
        } else {
            newProductEntity = new ProductEntity(UUID.fromString(productInput.id()), productInput.title(),
                    productInput.desc(), productInput.price());
        }

        if (productInput.productPriceHistoryInputList() != null) {

            for (ProductPriceHistoryInput productPriceHistoryInput : productInput.productPriceHistoryInputList()) {

                Timestamp startDateAsSqlTimestamp = Timestamp.valueOf(productPriceHistoryInput.startDate());

                if (productPriceHistoryInput.id() == null) {
                    newProductEntity.getProductPriceHistories().add(
                            new ProductPriceHistoryEntity(
                                    startDateAsSqlTimestamp,
                                    productPriceHistoryInput.price(), newProductEntity));
                } else {
                    newProductEntity.getProductPriceHistories().add(
                            new ProductPriceHistoryEntity(UUID.fromString(productPriceHistoryInput.id()),
                                    startDateAsSqlTimestamp,
                                    productPriceHistoryInput.price(), newProductEntity));
                }

            }

        }

        ProductEntity savedProductEntity = productRepository.save(newProductEntity);

        productPriceHistoryRepository.saveAll(newProductEntity.getProductPriceHistories());

        return savedProductEntity;

    }

    public ProductEntity findProduct(UUID id) {

        return productRepository.findById(id).orElse(null);

    }

    public List<ProductEntity> findAllProducts() {

        return productRepository.findAll();

    }

    public List<ProductEntity> searchProducts(ProductSearchCriteriaInput productSearchCriteriaInput) {

        return productCustomRepository.findUsingProductSearchCriteria(productSearchCriteriaInput);

    }

    public UUID deleteProduct(UUID id) {

        if (productRepository.existsById(id)) {

            productRepository.deleteById(id);
            return id;

        }

        return null;

    }
}
