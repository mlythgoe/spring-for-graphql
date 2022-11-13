package com.mike.springforgraphql.service;

import com.mike.springforgraphql.api.input.ProductInput;
import com.mike.springforgraphql.api.input.ProductPriceHistoryInput;
import com.mike.springforgraphql.api.input.ProductSearchCriteriaInput;
import com.mike.springforgraphql.db.entity.ProductEntity;
import com.mike.springforgraphql.db.entity.ProductPriceHistoryEntity;
import com.mike.springforgraphql.db.repository.ProductCustomRepository;
import com.mike.springforgraphql.db.repository.ProductPriceHistoryRepository;
import com.mike.springforgraphql.db.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    ProductRepository productRepository;

    ProductCustomRepository productCustomRepository;

    ProductPriceHistoryRepository productPriceHistoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCustomRepository productCustomRepository,
            ProductPriceHistoryRepository productPriceHistoryRepository) {
        this.productRepository = productRepository;
        this.productCustomRepository = productCustomRepository;
        this.productPriceHistoryRepository = productPriceHistoryRepository;
    }

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

        return productRepository.findById(id).orElse(null);
    }

    public List<ProductEntity> findAllProducts() {

        return productRepository.findAll();

    }

    public List<ProductEntity> searchProducts(ProductSearchCriteriaInput productSearchCriteriaInput) {

        List<ProductEntity> productEntities;

        productEntities = productCustomRepository.findUsingProductSearchCriteria(productSearchCriteriaInput);

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
