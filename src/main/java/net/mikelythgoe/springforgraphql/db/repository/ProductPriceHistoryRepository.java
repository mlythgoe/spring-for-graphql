package net.mikelythgoe.springforgraphql.db.repository;

import net.mikelythgoe.springforgraphql.db.entity.ProductPriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistoryEntity, Long> {

}
