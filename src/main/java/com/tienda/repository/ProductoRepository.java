package com.tienda.repository;

import com.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    Optional<Producto> findTop1ByCategoriaContainingIgnoreCase(String categoria);
}