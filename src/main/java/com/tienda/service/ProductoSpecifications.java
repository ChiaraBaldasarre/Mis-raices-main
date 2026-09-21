package com.tienda.service;

import com.tienda.model.Producto;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ProductoSpecifications {

    public static Specification<Producto> conFiltros(String categoria, Double precioMax, List<String> colores) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoria != null && !categoria.equalsIgnoreCase("Todas")) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%"));
            }
            if (precioMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precio"), precioMax));
            }
            if (colores != null && !colores.isEmpty()) {
                predicates.add(root.get("color").in(colores));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}