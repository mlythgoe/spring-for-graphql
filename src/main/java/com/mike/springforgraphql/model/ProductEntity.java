package com.mike.springforgraphql.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Product")
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Integer price;

    @OneToMany
    @JoinColumn(
            name = "ProductId",
            nullable = false
    )

    private List<ProductPriceHistoryEntity> productPriceHistoryEntityList = new ArrayList<>();

    public ProductEntity() {
    }

    public ProductEntity(String title, String description, Integer price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public ProductEntity(Long id, String title, String description, Integer price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;

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

    public List<ProductPriceHistoryEntity> getProductPriceHistoryEntityList() {
        return productPriceHistoryEntityList;
    }

    public void setProductPriceHistoryEntityList(List<ProductPriceHistoryEntity> productPriceHistoryEntityList) {
        this.productPriceHistoryEntityList = productPriceHistoryEntityList;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}