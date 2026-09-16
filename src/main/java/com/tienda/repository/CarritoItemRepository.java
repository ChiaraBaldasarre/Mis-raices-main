package com.tienda.repository;

import com.tienda.model.ItemCarrito;
import com.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByUsuario(Usuario usuario);

    Optional<ItemCarrito> findByUsuarioAndProductoId(Usuario usuario, Long productoId);

    void deleteByUsuario(Usuario usuario);
}
