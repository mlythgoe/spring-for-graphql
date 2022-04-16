package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.model.Product;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepository {

    private final List<Product> products = new ArrayList<>();

    public List<Product> findAll() {
        return products;
    }

    public Product findOne(Integer id) {
        return products.stream().filter(product -> Objects.equals(product.id(), id)).findFirst().orElse(null);
    }

    public void save(Product product) {

       for  (int i = 0; i < products.size(); i++) {
           if (products.get(i).id() == product.id()) {
               products.set(i,product);
               return;
           }
       }
        products.add(product);
    }

    @PostConstruct
    private void init() {
        products.add(new Product(1, "Yummy Waffles", "These are the best waffles ever"));
        products.add(new Product(2, "Shit Sandwiches", "These are the shittiest shit filled sandwiches ever"));

    }
}
