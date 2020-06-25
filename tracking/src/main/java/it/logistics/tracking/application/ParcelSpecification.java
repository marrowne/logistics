package it.logistics.tracking.application;

import it.logistics.tracking.domain.model.parcel.Parcel;
import it.logistics.tracking.domain.model.parcel.ParcelStatus;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParcelSpecification implements Specification<Parcel> {

    String query;
    ParcelStatus[] parcelStatuses;
    Boolean filterAvailableOnly;

    public ParcelSpecification(String query, Boolean filterAvailableOnly, ParcelStatus[] parcelStatuses) {
        this.query = query;
        this.filterAvailableOnly = filterAvailableOnly;
        this.parcelStatuses = parcelStatuses;
    }

    @Override
    public Predicate toPredicate(Root<Parcel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate searchString = criteriaBuilder.like(criteriaBuilder.upper(root.get("searchString")), "%" + query.toUpperCase() + "%");
        if (filterAvailableOnly) {
            List<Predicate> availableStatuses = Arrays.stream(parcelStatuses)
                    .map(parcelStatus -> criteriaBuilder.equal(root.get("status"), parcelStatus))
                    .collect(Collectors.toList());
            Predicate availableStatusesPredicate = criteriaBuilder.or(availableStatuses.toArray((new Predicate[availableStatuses.size()])));
            return criteriaBuilder.and(searchString, availableStatusesPredicate);
        } else {
            return criteriaBuilder.and(searchString);
        }
    }
}