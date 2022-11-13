package com.mike.springforgraphql.db.repository;

import com.mike.springforgraphql.db.entity.ProductPriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistoryEntity, Long> {

}
