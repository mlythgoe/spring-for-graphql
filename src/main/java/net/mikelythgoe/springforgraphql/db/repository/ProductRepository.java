package net.mikelythgoe.springforgraphql.db.repository;

import net.mikelythgoe.springforgraphql.db.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

}
