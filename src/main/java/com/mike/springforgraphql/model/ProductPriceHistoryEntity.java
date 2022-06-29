package com.mike.springforgraphql.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity(name = "ProductPriceHistory")
public class ProductPriceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "startDate")
    private String startDate;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(
            name = "productId",
            updatable = false, insertable = false
    )
    @NotNull
    private ProductEntity product;


}
