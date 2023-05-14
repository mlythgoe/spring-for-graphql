package com.mike.springforgraphql.db.entity;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "productpricehistory")
public class ProductPriceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startDate")
    private java.sql.Timestamp startDate;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    public ProductPriceHistoryEntity() {
    }

    public ProductPriceHistoryEntity(Timestamp startDate, int price, ProductEntity productEntity) {
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;

    }

    public ProductPriceHistoryEntity(Long id, Timestamp startDate, int price, ProductEntity productEntity) {
        this.id = id;
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;

    }

    public Long getId() {
        return id;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public int getPrice() {
        return price;
    }

    public ProductEntity getProduct() {
        return productEntity;
    }

    @Override
    public String toString() {
        return "ProductPriceHistory{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", price=" + price +
                '}';
    }
}
