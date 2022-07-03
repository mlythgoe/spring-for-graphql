package com.mike.springforgraphql.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "productpricehistory")
public class ProductPriceHistory {

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


    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "ProductPriceHistory{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", price=" + price +
                ", product=" + product +
                '}';
    }
}
