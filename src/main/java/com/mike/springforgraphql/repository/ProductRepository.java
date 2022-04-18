package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.model.persistence.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
