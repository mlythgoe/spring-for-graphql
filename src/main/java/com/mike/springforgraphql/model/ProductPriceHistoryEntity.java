package com.mike.springforgraphql.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "ProductPriceHistory")
@Table(name = "productpricehistory")
public class ProductPriceHistoryEntity {

    public ProductPriceHistoryEntity() {
    }

    public ProductPriceHistoryEntity(Date startDate, int price, ProductEntity productEntity) {
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;

    }

    public ProductPriceHistoryEntity(Long id, Date startDate, int price, ProductEntity productEntity) {
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
    @JoinColumn(
            name = "productId",
            updatable = false, insertable = false
    )
    @NotNull
    private ProductEntity productEntity;


}
