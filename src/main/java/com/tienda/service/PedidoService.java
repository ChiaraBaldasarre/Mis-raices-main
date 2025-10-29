package com.tienda.service;

import com.tienda.model.*;
import com.tienda.repository.PedidoRepository;
import com.tienda.repository.ItemPedidoRepository;
import com.tienda.repository.ProductoRepository;
import com.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoService carritoService;

    @Transactional
    public Pedido crearPedidoDesdeCarrito(Usuario usuario, String direccion, String contacto) {

        if (carritoService.estaVacio()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccion(direccion);
        pedido.setContacto(contacto);

        // Convertir items del carrito a items de pedido
        List<ItemCarrito> itemsCarrito = carritoService.getItems();
        Double total = 0.0;

        for (ItemCarrito itemCarrito : itemsCarrito) {
            Producto producto = productoRepository.findById(itemCarrito.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemCarrito.getProducto().getId()));

            if (producto.getStock() < itemCarrito.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre() +
                        ". Stock disponible: " + producto.getStock() + ", solicitado: " + itemCarrito.getCantidad());
            }

            ItemPedido itemPedido = new ItemPedido(producto, itemCarrito.getCantidad());
            pedido.agregarItem(itemPedido);
            total += itemPedido.getSubtotal();

            producto.setStock(producto.getStock() - itemCarrito.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        if (!usuario.isEsCliente()) {
            usuario.setEsCliente(true);
            usuarioRepository.save(usuario);
        }

        carritoService.limpiarCarrito();

        return pedidoGuardado;
    }

    public List<Pedido> obtenerPedidosPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }
}