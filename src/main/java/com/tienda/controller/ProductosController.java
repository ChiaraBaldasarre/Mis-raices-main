package com.tienda.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tienda.service.ProductoService;
import com.tienda.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import com.tienda.service.CarritoService;
import java.util.List;

@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/productos")
    public String listarProductos(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "precioMax", required = false) Double precioMax,
            @RequestParam(value = "color", required = false) List<String> colores,
            Model model,
            Authentication authentication) {

        // Normalizar parámetros
        if (categoria != null && (categoria.isEmpty() || "Todas".equalsIgnoreCase(categoria))) {
            categoria = null;
        }

        if (colores != null && colores.isEmpty()) {
            colores = null;
        }

        // Obtener productos filtrados
        List<Producto> productos = productoService.buscarPorFiltros(categoria, precioMax, colores);

        // Agregar atributos al modelo
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("precioMaxSeleccionado", precioMax);
        model.addAttribute("coloresSeleccionados", colores);

        // Mostrar cantidad del carrito solo si el usuario está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos());
        } else {
            model.addAttribute("cantidadTotal", 0);
        }

        return "productos";
    }
}