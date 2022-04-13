package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.model.Product;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    private List<Product> products = new ArrayList<>();

    public List<Product> findAll() {
        return products;
    }

    public Product findOne(Integer id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    @PostConstruct
    private void init() {
        products.add(new Product(1, "Yummy Waffles", "These are the best waffles ever"));
        products.add(new Product(2, "Shit Sandwiches", "These are the shittiest shit filled sandwiches ever"));

    }
}
