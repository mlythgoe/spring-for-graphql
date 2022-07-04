package com.mike.springforgraphql.service;

import com.mike.springforgraphql.api.ProductInput;
import com.mike.springforgraphql.api.ProductPriceHistoryInput;
import com.mike.springforgraphql.api.ProductSearchCriteriaInput;
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

        ProductEntity productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            return null;
        }

        return productEntity;
    }

    public List<ProductEntity> findAllProducts() {

        List<ProductEntity> productEntities = productRepository.findAll();

        return productEntities;

    }

    public List<ProductEntity> searchProducts(ProductSearchCriteriaInput productSearchCriteriaInput) {

        List<ProductEntity> productEntities;

        productEntities = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteriaInput);

        if (productEntities == null) {
            return null;
        }

        return productEntities;
    }

    public Long deleteProduct(Long id) {

        if (productRepository.existsById(id)) {

            productRepository.deleteById(id);
            return id;

        }

        return null;

    }
}
