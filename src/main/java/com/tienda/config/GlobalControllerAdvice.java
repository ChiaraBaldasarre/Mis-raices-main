package com.tienda.config;

import com.tienda.model.Usuario;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("usuarioActual")
    public Usuario getUsuarioActual(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Usuario> usuarioOptional = usuarioService.findByUsername(authentication.getName());
            return usuarioOptional.orElse(null); // Devuelve el usuario si existe, o null si no
        }
        return null;
    }
}