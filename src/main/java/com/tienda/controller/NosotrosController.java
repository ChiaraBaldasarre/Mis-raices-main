package com.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tienda.service.CarritoService;

@Controller
public class NosotrosController {
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/nosotros")
    public String nosotros(Model model) {
        model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos());
        return "nosotros";
    }
}
