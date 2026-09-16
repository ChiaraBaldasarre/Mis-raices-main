package com.tienda.repository;

import com.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // FILTROS DINÁMICOS
    @Query("SELECT p FROM Producto p " +
            "WHERE (:categoria IS NULL OR p.categoria = :categoria) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax) " +
            "AND (:colores IS NULL OR p.color IN :colores)")
    List<Producto> buscarPorFiltros(
            @Param("categoria") String categoria,
            @Param("precioMax") Double precioMax,
            @Param("colores") List<String> colores);

    // Busca el primer producto (Top1) cuya categoría contenga el string, ignorando mayúsculas/minúsculas.
    Optional<Producto> findTop1ByCategoriaContainingIgnoreCase(String categoria);
}