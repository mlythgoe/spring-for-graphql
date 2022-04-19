package com.mike.springforgraphql.model.persistence;

import javax.persistence.*;

@Entity(name = "Product")
public class ProductEntity {

    public ProductEntity() {
    }

    public ProductEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public ProductEntity(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Allow Persistence provider to manage id (Others are IDENTITY, SEQUENCE and TABLE).
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name="description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}