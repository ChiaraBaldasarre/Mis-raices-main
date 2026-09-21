package com.tienda.controller;

import com.tienda.model.ItemCarrito;
import com.tienda.model.Usuario;
import com.tienda.service.CarritoService;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class NosotrosController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/nosotros")
    public String nosotros(Model model, Authentication authentication) {

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
        return "nosotros";
    }
}