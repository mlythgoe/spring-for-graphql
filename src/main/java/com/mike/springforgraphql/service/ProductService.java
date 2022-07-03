package com.mike.springforgraphql.service;

import com.mike.springforgraphql.api.Product;
import com.mike.springforgraphql.api.ProductInput;
import com.mike.springforgraphql.api.ProductPriceHistoryInput;
import com.mike.springforgraphql.model.ProductPriceHistory;
import com.mike.springforgraphql.repository.ProductPriceHistoryRepository;
import com.mike.springforgraphql.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceHistoryRepository productPriceHistoryRepository;

    public com.mike.springforgraphql.model.Product saveProduct(ProductInput productInput) {

        com.mike.springforgraphql.model.Product newProduct;

        if (productInput.id() == null) {

            newProduct = new com.mike.springforgraphql.model.Product(productInput.title(),
                    productInput.desc(), productInput.price());

            for (ProductPriceHistoryInput productPriceHistoryInput: productInput.productPriceHistoryInputList()) {
                var startDateAsSqlDate = Date.valueOf(LocalDate.parse(productPriceHistoryInput.startDate()));
                newProduct.getProductPriceHistories().add(
                        new ProductPriceHistory(startDateAsSqlDate,  productPriceHistoryInput.price(), newProduct));

            }


        } else {

            newProduct = new com.mike.springforgraphql.model.Product(productInput.id(), productInput.title(),
                    productInput.desc(), productInput.price());

        }

        com.mike.springforgraphql.model.Product savedProduct = productRepository.save(newProduct);

        productPriceHistoryRepository.saveAll(newProduct.getProductPriceHistories());


        return savedProduct;

    }
}
