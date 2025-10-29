package com.tienda.controller;

import com.tienda.model.Contacto;
import com.tienda.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.tienda.service.CarritoService;

@Controller
public class ContactosController {

    @Autowired
    private ContactoService contactoService;
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/contacto")
    public String contactoForm(Model model) {
        // Enlazar el formulario con una nueva instancia vacía de Contacto
        model.addAttribute("contacto", new Contacto());
        model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos());
        return "contactos";
    }

    @PostMapping("/contacto")
    public String procesarContacto(Contacto contacto, Model model) {

        try {
            contactoService.guardarMensaje(contacto);
            model.addAttribute("mensajeExito", "¡Gracias! Tu mensaje ha sido enviado correctamente.");
            model.addAttribute("contacto", new Contacto());

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Ocurrió un error al enviar el mensaje. Intenta de nuevo.");
            model.addAttribute("contacto", contacto);
        }
        model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos()); // AGREGADO
        return "contactos";
    }
}