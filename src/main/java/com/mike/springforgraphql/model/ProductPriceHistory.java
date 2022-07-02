package com.mike.springforgraphql.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "productpricehistory")
public class ProductPriceHistory {

    public ProductPriceHistory() {
    }

    public ProductPriceHistory(Date startDate, int price, Product product) {
        this.startDate = startDate;
        this.price = price;
        this.product = product;

    }

    public ProductPriceHistory(Long id, Date startDate, int price, Product product) {
        this.id = id;
        this.startDate = startDate;
        this.price = price;
        this.product = product;

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
    private Product product;

}
