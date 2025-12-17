package net.mikelythgoe.springforgraphql.db.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "productpricehistory")
public class ProductPriceHistoryEntity extends BaseEntity {

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    public ProductPriceHistoryEntity() {
    }

    public ProductPriceHistoryEntity(Instant startDate, int price, ProductEntity productEntity) {
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;

    }

    public ProductPriceHistoryEntity(UUID id, Instant startDate, int price, ProductEntity productEntity) {
        this.id = id;
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;
    }

    public Instant getStartDate() {
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
