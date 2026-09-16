package com.tienda.service;

import com.tienda.model.ItemCarrito;
import com.tienda.model.Producto;
import com.tienda.model.Usuario;
import com.tienda.repository.CarritoItemRepository;
import com.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<ItemCarrito> obtenerCarritoPorUsuario(Usuario usuario) {
        return carritoItemRepository.findByUsuario(usuario);
    }

    @Transactional
    public void agregarProducto(Usuario usuario, Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        Optional<ItemCarrito> itemExistenteOpt = carritoItemRepository.findByUsuarioAndProductoId(usuario, productoId);

        if (itemExistenteOpt.isPresent()) {
            ItemCarrito item = itemExistenteOpt.get();
            int nuevaCantidad = item.getCantidad() + cantidad;

            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            item.setCantidad(nuevaCantidad);
            carritoItemRepository.save(item);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(usuario, producto, cantidad);
            carritoItemRepository.save(nuevoItem);
        }
    }

    @Transactional
    public void actualizarItem(Usuario usuario, Long productoId, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarItem(usuario, productoId);
            return;
        }

        ItemCarrito item = carritoItemRepository.findByUsuarioAndProductoId(usuario, productoId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        if (item.getProducto().getStock() < nuevaCantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + item.getProducto().getNombre());
        }

        item.setCantidad(nuevaCantidad);
        carritoItemRepository.save(item);
    }

    @Transactional
    public void eliminarItem(Usuario usuario, Long productoId) {
        Optional<ItemCarrito> itemOpt = carritoItemRepository.findByUsuarioAndProductoId(usuario, productoId);
        itemOpt.ifPresent(carritoItemRepository::delete);
    }

    @Transactional
    public void limpiarCarrito(Usuario usuario) {
        carritoItemRepository.deleteByUsuario(usuario);
    }

    public Double getTotal(List<ItemCarrito> items) {
        return items.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public int getCantidadTotalProductos(List<ItemCarrito> items) {
        return items.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}