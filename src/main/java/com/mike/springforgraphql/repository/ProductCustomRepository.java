package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.api.ProductSearchCriteriaInput;
import com.mike.springforgraphql.model.ProductEntity;
import com.mike.springforgraphql.model.ProductEntity_;
import org.springframework.stereotype.Repository;

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

    public List<ProductEntity> findUsingProductSearchCriteria(ProductSearchCriteriaInput productSearchCriteriaInput) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);

        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);

        if ( productSearchCriteriaInput.title() != null ) {
            Predicate titlePredicate = criteriaBuilder.like(root.get(ProductEntity_.TITLE),
                    productSearchCriteriaInput.title());
            criteriaQuery.where(titlePredicate);
        }

        if ( productSearchCriteriaInput.lowerPrice() != null ) {
            Predicate lowerPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(ProductEntity_.PRICE),
                    productSearchCriteriaInput.lowerPrice());
            criteriaQuery.where(lowerPricePredicate);
        }

        if ( productSearchCriteriaInput.upperPrice() != null ) {
            Predicate upperPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(ProductEntity_.PRICE),
                    productSearchCriteriaInput.upperPrice());
            criteriaQuery.where(upperPricePredicate);
        }

        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

}