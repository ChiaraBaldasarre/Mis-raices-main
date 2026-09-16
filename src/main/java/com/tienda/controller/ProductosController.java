package com.tienda.controller;

import com.tienda.model.Producto;
import com.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public String listarProductos(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "precioMax", required = false) Double precioMax,
            @RequestParam(value = "color", required = false) List<String> colores,
            Model model) {

        if (categoria != null && (categoria.isEmpty() || "Todas".equalsIgnoreCase(categoria))) {
            categoria = null;
        }

        if (colores != null && colores.isEmpty()) {
            colores = null;
        }

        List<Producto> productos = productoService.buscarPorFiltros(categoria, precioMax, colores);
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("precioMaxSeleccionado", precioMax);
        model.addAttribute("coloresSeleccionados", colores);
        return "productos";
    }
}