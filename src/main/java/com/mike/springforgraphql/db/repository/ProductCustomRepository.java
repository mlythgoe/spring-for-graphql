package com.mike.springforgraphql.db.repository;

import com.mike.springforgraphql.api.input.ProductSearchCriteriaInput;
import com.mike.springforgraphql.db.entity.ProductEntity;
import com.mike.springforgraphql.db.entity.ProductEntity_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductEntity> findUsingProductSearchCriteria(ProductSearchCriteriaInput productSearchCriteriaInput) {

        if (productSearchCriteriaInput.lowerPrice() == null &
                productSearchCriteriaInput.upperPrice() == null &
                productSearchCriteriaInput.title() == null &&
                productSearchCriteriaInput.desc() == null) {
            return new ArrayList<>();
        }

        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);

        var root = criteriaQuery.from(ProductEntity.class);

        if (productSearchCriteriaInput.title() != null) {
            var titlePredicate = criteriaBuilder.like(root.get(ProductEntity_.TITLE),
                    productSearchCriteriaInput.title());
            criteriaQuery.where(titlePredicate);
        }

        if ((productSearchCriteriaInput.lowerPrice() != null) &&
                (productSearchCriteriaInput.upperPrice() != null)) {
            var lowerPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(ProductEntity_.PRICE),
                    productSearchCriteriaInput.lowerPrice());

            var upperPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(ProductEntity_.PRICE),
                    productSearchCriteriaInput.upperPrice());

            var lowerAndUpperPricePredicate = criteriaBuilder.and(lowerPricePredicate, upperPricePredicate);

            criteriaQuery.where(lowerAndUpperPricePredicate);
        } else {
            if (productSearchCriteriaInput.lowerPrice() != null) {

                var lowerPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(ProductEntity_.PRICE),
                        productSearchCriteriaInput.lowerPrice());

                criteriaQuery.where(lowerPricePredicate);

            }

            if (productSearchCriteriaInput.upperPrice() != null) {
                var upperPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(ProductEntity_.PRICE),
                        productSearchCriteriaInput.upperPrice());
                criteriaQuery.where(upperPricePredicate);
            }

        }

        var typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

}