package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.api.ProductSearchCriteria;
import com.mike.springforgraphql.model.ProductEntity;
import org.springframework.stereotype.Repository;
import com.mike.springforgraphql.model.ProductEntity_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductEntity> findUsingProductSearchCriteria(ProductSearchCriteria productSearchCriteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);

        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        if ( productSearchCriteria.title() != null ) {
            Predicate titlePredicate = criteriaBuilder.like(root.get(ProductEntity_.TITLE),
                    productSearchCriteria.title());
            criteriaQuery.where(titlePredicate);
        }

        if ( productSearchCriteria.lowerPrice() != null ) {
            Predicate lowerPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(ProductEntity_.PRICE),
                    productSearchCriteria.lowerPrice());
            criteriaQuery.where(lowerPricePredicate);
        }

        if ( productSearchCriteria.upperPrice() != null ) {
            Predicate upperPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(ProductEntity_.PRICE),
                    productSearchCriteria.upperPrice());
            criteriaQuery.where(upperPricePredicate);
        }

        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

}