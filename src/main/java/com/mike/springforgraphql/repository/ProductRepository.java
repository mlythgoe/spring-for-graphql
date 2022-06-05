package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long>, JpaSpecificationExecutor<ProductEntity> {

    List<ProductEntity> findByPriceBetween(Integer lowerPrice, Integer upperPrice);

}
