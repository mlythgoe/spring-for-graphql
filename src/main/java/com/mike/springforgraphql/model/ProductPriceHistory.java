package com.mike.springforgraphql.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "productpricehistory")
public class ProductPriceHistory {

    public ProductPriceHistory() {
    }

    public ProductPriceHistory(Date startDate, int price, ProductEntity productEntity) {
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;

    }

    public ProductPriceHistory(Long id, Date startDate, int price, ProductEntity productEntity) {
        this.id = id;
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "startDate")
    private java.sql.Date startDate;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private ProductEntity productEntity;

}
