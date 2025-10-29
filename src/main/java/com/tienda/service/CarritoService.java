package com.tienda.service;

import com.tienda.model.Producto;
import com.tienda.model.ItemCarrito;
import com.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoService {

    @Autowired
    private ProductoRepository productoRepository;

    private List<ItemCarrito> items = new ArrayList<>();

    public void agregarProducto(Long productoId, int cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            Optional<ItemCarrito> itemExistente = items.stream()
                    .filter(item -> item.getProducto().getId().equals(producto.getId()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                int nuevaCantidad = itemExistente.get().getCantidad() + cantidad;
                if (producto.getStock() < nuevaCantidad) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
                }
                itemExistente.get().setCantidad(nuevaCantidad);
            } else {
                items.add(new ItemCarrito(producto, cantidad));
            }
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + productoId);
        }
    }

    public void agregarProducto(Long productoId) {
        agregarProducto(productoId, 1);
    }

    public void actualizarItem(Long idProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarItem(idProducto);
            return;
        }

        Optional<ItemCarrito> itemOpt = items.stream()
                .filter(item -> item.getProducto().getId().equals(idProducto))
                .findFirst();

        if (itemOpt.isPresent()) {
            ItemCarrito item = itemOpt.get();
            if (item.getProducto().getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + item.getProducto().getNombre());
            }
            item.setCantidad(nuevaCantidad);
        }
    }

    public void eliminarItem(Long idProducto) {
        items.removeIf(item -> item.getProducto().getId().equals(idProducto));
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }

    public Double getTotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public int getCantidadTotalProductos() {
        return items.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    public void limpiarCarrito() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}