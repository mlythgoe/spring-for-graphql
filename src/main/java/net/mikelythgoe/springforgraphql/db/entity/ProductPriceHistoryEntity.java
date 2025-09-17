package net.mikelythgoe.springforgraphql.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "productpricehistory")
public class ProductPriceHistoryEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(name = "uuidv7", strategy = "net.mikelythgoe.springforgraphql.db.idgenerator.UuidV7Generator")
    private UUID id;

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

    public ProductPriceHistoryEntity(UUID id, Timestamp startDate, int price, ProductEntity productEntity) {
        this.id = id;
        this.startDate = startDate;
        this.price = price;
        this.productEntity = productEntity;
    }

    public UUID getId() {
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
