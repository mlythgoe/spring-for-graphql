package com.mike.springforgraphql.repository;

import com.mike.springforgraphql.api.ProductSearchCriteria;
import com.mike.springforgraphql.model.Product;
import com.mike.springforgraphql.model.Product_;
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

    public List<Product> findUsingProductSearchCriteria(ProductSearchCriteria productSearchCriteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

        Root<Product> root = criteriaQuery.from(Product.class);

        if ( productSearchCriteria.title() != null ) {
            Predicate titlePredicate = criteriaBuilder.like(root.get(Product_.TITLE),
                    productSearchCriteria.title());
            criteriaQuery.where(titlePredicate);
        }

        if ( productSearchCriteria.lowerPrice() != null ) {
            Predicate lowerPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(Product_.PRICE),
                    productSearchCriteria.lowerPrice());
            criteriaQuery.where(lowerPricePredicate);
        }

        if ( productSearchCriteria.upperPrice() != null ) {
            Predicate upperPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(Product_.PRICE),
                    productSearchCriteria.upperPrice());
            criteriaQuery.where(upperPricePredicate);
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

}