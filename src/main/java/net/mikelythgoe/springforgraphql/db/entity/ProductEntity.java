package net.mikelythgoe.springforgraphql.db.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @Tsid
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductPriceHistoryEntity> productPriceHistories = new ArrayList<>();

    public ProductEntity() {
    }

    public ProductEntity(String title, String description, Integer price) {
        this.title = title;
        this.description = description;
        this.price = price;
        productPriceHistories = new ArrayList<>();
    }

    public ProductEntity(Long id, String title, String description, Integer price) {
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

    public List<ProductPriceHistoryEntity> getProductPriceHistories() {
        return productPriceHistories;
    }

    public void setProductPriceHistories(List<ProductPriceHistoryEntity> productPriceHistories) {
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