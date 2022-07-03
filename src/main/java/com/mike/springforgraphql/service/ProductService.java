package com.mike.springforgraphql.service;

import com.mike.springforgraphql.api.ProductInput;
import com.mike.springforgraphql.api.ProductPriceHistoryInput;
import com.mike.springforgraphql.api.ProductSearchCriteria;
import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.model.ProductPriceHistoryEntity;
import com.mike.springforgraphql.repository.ProductCustomRepository;
import com.mike.springforgraphql.repository.ProductPriceHistoryRepository;
import com.mike.springforgraphql.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCustomRepository productCustomRepository;

    @Autowired
    ProductPriceHistoryRepository productPriceHistoryRepository;

    Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductEntity saveProduct(ProductInput productInput) {

        ProductEntity newProductEntity;

        if (productInput.id() == null) {
            newProductEntity = new ProductEntity(productInput.title(),
                    productInput.desc(), productInput.price());
        } else {
            newProductEntity = new ProductEntity(productInput.id(), productInput.title(),
                    productInput.desc(), productInput.price());
        }

        if (productInput.productPriceHistoryInputList() != null) {

            for (ProductPriceHistoryInput productPriceHistoryInput : productInput.productPriceHistoryInputList()) {

                Date startDateAsSqlDate = Date.valueOf(LocalDate.parse(productPriceHistoryInput.startDate()));
                newProductEntity.getProductPriceHistories().add(
                        new ProductPriceHistoryEntity(productPriceHistoryInput.id(), startDateAsSqlDate,
                                productPriceHistoryInput.price(), newProductEntity));
            }

        }

        ProductEntity savedProductEntity = productRepository.save(newProductEntity);

        productPriceHistoryRepository.saveAll(newProductEntity.getProductPriceHistories());


        return savedProductEntity;

    }

    public ProductEntity findProduct(Long id) {

        logger.debug("Find Product for id {}", id);

        ProductEntity productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            logger.debug("No Product found for id {}", id);

            return null;
        }

        logger.debug("Found Product {} for id {}", productEntity, id);

        return productEntity;
    }

    public List<ProductEntity> findAllProducts() {

        logger.debug("Find All Products");

        List<ProductEntity> productEntities = productRepository.findAll();

        logger.debug("Found All Product {}", productEntities);

        return productEntities;

    }

    public List<ProductEntity> searchProducts(ProductSearchCriteria productSearchCriteria) {

        List<ProductEntity> productEntities;

        productEntities = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteria);

        if (productEntities == null) {
            logger.debug("No Products found for search criteria {}", productSearchCriteria);

            return null;
        }

        return productEntities;
    }

    public Long deleteProduct(Long id) {

        logger.debug("Delete Product for Id {}", id);

        if (productRepository.existsById(id)) {

            productRepository.deleteById(id);
            logger.debug("Deleted Product for id {}", id);
            return id;

        }

        logger.debug("Product for id {} did not exist so could not be deleted", id);

        return null;

    }
}
