package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.model.ProductPriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistoryEntity,Long> {

}
