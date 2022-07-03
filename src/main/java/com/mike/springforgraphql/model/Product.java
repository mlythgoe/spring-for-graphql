package com.mike.springforgraphql.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<ProductPriceHistory> productPriceHistories = new ArrayList<>();

    public Product() {
    }

    public Product(String title, String description, Integer price) {
        this.title = title;
        this.description = description;
        this.price = price;
        productPriceHistories = new ArrayList<>();
    }

    public Product(Long id, String title, String description, Integer price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        productPriceHistories = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public List<ProductPriceHistory> getProductPriceHistories() {
        return productPriceHistories;
    }

    public void setProductPriceHistories(List<ProductPriceHistory> productPriceHistories) {
        this.productPriceHistories = productPriceHistories;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", productPriceHistories=" + productPriceHistories +
                '}';
    }
}