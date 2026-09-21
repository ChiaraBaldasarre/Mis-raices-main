package com.tienda.controller;

import com.tienda.model.Producto;
import com.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/api/filtros-config")
    @ResponseBody
    public Map<String, Object> getConfigFiltros() {
        return productoService.obtenerFiltrosDisponibles();
    }

    @GetMapping("/productos")
    public String listarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) List<String> color,
            Model model) {

        List<Producto> productos = productoService.buscarPorFiltros(categoria, precioMax, color);

        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("precioMaxSeleccionado", precioMax);
        model.addAttribute("coloresSeleccionados", color);

        return "productos";
    }
}