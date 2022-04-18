package com.mike.springforgraphql.model.persistence;

import javax.persistence.*;

@Entity
public class ProductEntity {

    public ProductEntity() {
    }

    public ProductEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProductEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Allow Persistence provider to manage id (Others are IDENTITY, SEQUENCE and TABLE).
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}