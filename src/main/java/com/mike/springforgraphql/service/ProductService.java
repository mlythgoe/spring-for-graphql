package com.mike.springforgraphql.service;

import com.mike.springforgraphql.api.ProductInput;
import com.mike.springforgraphql.api.ProductPriceHistoryInput;
import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.model.ProductPriceHistoryEntity;
import com.mike.springforgraphql.repository.ProductPriceHistoryRepository;
import com.mike.springforgraphql.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceHistoryRepository productPriceHistoryRepository;

    public ProductEntity saveProduct(ProductInput productInput) {

        ProductEntity newProductEntity;

        if (productInput.id() == null) {

            newProductEntity = new ProductEntity(productInput.title(),
                    productInput.desc(), productInput.price());

            for (ProductPriceHistoryInput productPriceHistoryInput: productInput.productPriceHistoryInputList()) {
                Date startDateAsSqlDate = Date.valueOf(LocalDate.parse(productPriceHistoryInput.startDate()));
                newProductEntity.getProductPriceHistories().add(
                        new ProductPriceHistoryEntity(startDateAsSqlDate,  productPriceHistoryInput.price(), newProductEntity));

            }


        } else {

            newProductEntity = new ProductEntity(productInput.id(), productInput.title(),
                    productInput.desc(), productInput.price());

        }

        ProductEntity savedProductEntity = productRepository.save(newProductEntity);

        productPriceHistoryRepository.saveAll(newProductEntity.getProductPriceHistories());


        return savedProductEntity;

    }
}
