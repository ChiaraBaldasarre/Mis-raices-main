package com.tienda.controller;

import com.tienda.model.Producto;
import com.tienda.service.ProductoService;
import com.tienda.service.CarritoService; // Agregado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/")
    public String inicio(Model model) {
        List<Producto> destacados = productoService.getProductosDestacados();
        model.addAttribute("productosDestacados", destacados);
        model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos());
        return "index";
    }

    @GetMapping("/producto-card/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id);
        model.addAttribute("producto", producto);
        model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos());
        return "producto-card";
    }
}