package com.tienda.service;

import com.tienda.repository.ProductoRepository;
import com.tienda.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> buscarPorFiltros(String categoria, Double precioMax, List<String> colores) {

        if (categoria != null && categoria.equalsIgnoreCase("Todas")) {
            categoria = null;
        }

        if (colores == null || colores.isEmpty()) {
            colores = null;
        }

        Specification<Producto> spec = ProductoSpecifications.conFiltros(categoria, precioMax, colores);
        List<Producto> resultados = productoRepository.findAll(spec);

        if (resultados.isEmpty() && colores != null) {
            spec = ProductoSpecifications.conFiltros(categoria, precioMax, null);
            resultados = productoRepository.findAll(spec);
        }

        if (resultados.isEmpty() && precioMax != null) {
            Double precioFlexible = precioMax * 1.30;
            spec = ProductoSpecifications.conFiltros(categoria, precioFlexible, null);
            resultados = productoRepository.findAll(spec);
        }

        return resultados;
    }

    public List<Producto> getProductosDestacados() {

        List<Producto> destacados = new ArrayList<>();

        Optional<Producto> hombre = productoRepository.findTop1ByCategoriaContainingIgnoreCase("Hombres");
        hombre.ifPresent(destacados::add);

        Optional<Producto> mujer = productoRepository.findTop1ByCategoriaContainingIgnoreCase("Mujeres");
        mujer.ifPresent(destacados::add);

        Optional<Producto> nino = productoRepository.findTop1ByCategoriaContainingIgnoreCase("Niños");
        nino.ifPresent(destacados::add);
        return destacados;
    }

    public Map<String, Object> obtenerFiltrosDisponibles() {

        List<Producto> todos = productoRepository.findAll();

        double precioMin = todos.stream().mapToDouble(Producto::getPrecio).min().orElse(0.0);
        double precioMax = todos.stream().mapToDouble(Producto::getPrecio).max().orElse(20000.0);

        List<String> categorias = todos.stream().map(Producto::getCategoria).distinct().toList();
        List<String> colores = todos.stream().map(Producto::getColor).distinct().toList();

        Map<String, Object> filtros = new HashMap<>();
        filtros.put("precioMinimoReal", precioMin);
        filtros.put("precioMaximoReal", precioMax);
        filtros.put("categoriasDisponibles", categorias);
        filtros.put("coloresDisponibles", colores);

        return filtros;
    }
}