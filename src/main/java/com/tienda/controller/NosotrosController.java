package com.tienda.controller;

import com.tienda.model.ItemCarrito;
import com.tienda.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NosotrosController {

    @Autowired
    private CarritoService carritoService;

    private static final String SESSION_CARRITO_KEY = "carritoItems";

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> obtenerCarritoSesion(HttpSession session) {
        List<ItemCarrito> items = (List<ItemCarrito>) session.getAttribute(SESSION_CARRITO_KEY);
        if (items == null) {
            items = new ArrayList<>();
            session.setAttribute(SESSION_CARRITO_KEY, items);
        }
        return items;
    }

    @GetMapping("/nosotros")
    public String nosotros(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            List<ItemCarrito> items = obtenerCarritoSesion(session);
            model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos(items));
        } else {
            model.addAttribute("cantidadTotal", 0);
        }
        return "nosotros";
    }
}