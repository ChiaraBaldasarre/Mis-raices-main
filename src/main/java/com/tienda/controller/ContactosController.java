package com.tienda.controller;

import com.tienda.model.Contacto;
import com.tienda.model.ItemCarrito;
import com.tienda.model.Usuario;
import com.tienda.service.ContactoService;
import com.tienda.service.CarritoService;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class ContactosController {

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/contacto")
    public String contactoForm(Model model, Authentication authentication) {

        model.addAttribute("contacto", new Contacto());

        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = usuarioService.findByUsername(authentication.getName()).orElse(null);

            if (usuario != null) {
                List<ItemCarrito> items = carritoService.obtenerCarritoPorUsuario(usuario);
                model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos(items));

            } else {
                model.addAttribute("cantidadTotal", 0);
            }

        } else {
            model.addAttribute("cantidadTotal", 0);
        }
        return "contactos";
    }

    @PostMapping("/contacto")
    public String procesarContacto(Contacto contacto, Model model, Authentication authentication) {

        try {
            contactoService.guardarMensaje(contacto);
            model.addAttribute("mensajeExito", "¡Gracias! Tu mensaje ha sido enviado correctamente.");
            model.addAttribute("contacto", new Contacto());

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Ocurrió un error al enviar el mensaje. Intenta de nuevo.");
            model.addAttribute("contacto", contacto);
        }

        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = usuarioService.findByUsername(authentication.getName()).orElse(null);
            if (usuario != null) {
                List<ItemCarrito> items = carritoService.obtenerCarritoPorUsuario(usuario);
                model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos(items));
            } else {
                model.addAttribute("cantidadTotal", 0);
            }

        } else {
            model.addAttribute("cantidadTotal", 0);
        }
        return "contactos";
    }
}