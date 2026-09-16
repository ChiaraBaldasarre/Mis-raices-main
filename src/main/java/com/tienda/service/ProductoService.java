package com.tienda.service;

import com.tienda.repository.ProductoRepository;
import com.tienda.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
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
        if (colores != null && colores.isEmpty()) {
            colores = null;
        }
        return productoRepository.buscarPorFiltros(categoria, precioMax, colores);
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
}