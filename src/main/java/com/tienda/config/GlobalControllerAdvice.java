package com.tienda.config;

import com.tienda.model.ItemCarrito;
import com.tienda.model.Usuario;
import com.tienda.service.CarritoService;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoService carritoService;

    @ModelAttribute("usuarioActual")
    public Usuario getUsuarioActual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            return null;
        }
        try {
            Optional<Usuario> usuarioOptional = usuarioService.findByUsername(authentication.getName());
            return usuarioOptional.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @ModelAttribute("cantidadTotal")
    public int getCantidadTotalCarrito(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        Object principal = authentication.getPrincipal();
        if ("anonymousUser".equals(principal)) {
            return 0;
        }
        try {
            Optional<Usuario> usuarioOptional = usuarioService.findByUsername(authentication.getName());
            if (usuarioOptional.isPresent()) {
                List<ItemCarrito> items = carritoService.obtenerCarritoPorUsuario(usuarioOptional.get());
                return carritoService.getCantidadTotalProductos(items);
            }
        } catch (Exception e) { }
        return 0;
    }
}