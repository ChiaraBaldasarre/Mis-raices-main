package com.tienda.controller;

import com.tienda.model.Contacto;
import com.tienda.model.ItemCarrito;
import com.tienda.service.ContactoService;
import com.tienda.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ContactosController {

    @Autowired
    private ContactoService contactoService;

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

    @GetMapping("/contacto")
    public String contactoForm(Model model, Authentication authentication, HttpSession session) {
        // Enlazar el formulario con una nueva instancia vacía de Contacto
        model.addAttribute("contacto", new Contacto());

        if (authentication != null && authentication.isAuthenticated()) {
            List<ItemCarrito> items = obtenerCarritoSesion(session);
            model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos(items));
        } else {
            model.addAttribute("cantidadTotal", 0);
        }
        return "contactos";
    }

    @PostMapping("/contacto")
    public String procesarContacto(Contacto contacto, Model model, Authentication authentication, HttpSession session) {

        try {
            contactoService.guardarMensaje(contacto);
            model.addAttribute("mensajeExito", "¡Gracias! Tu mensaje ha sido enviado correctamente.");
            model.addAttribute("contacto", new Contacto());

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Ocurrió un error al enviar el mensaje. Intenta de nuevo.");
            model.addAttribute("contacto", contacto);
        }

        if (authentication != null && authentication.isAuthenticated()) {
            List<ItemCarrito> items = obtenerCarritoSesion(session);
            model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos(items));
        } else {
            model.addAttribute("cantidadTotal", 0);
        }
        return "contactos";
    }
}