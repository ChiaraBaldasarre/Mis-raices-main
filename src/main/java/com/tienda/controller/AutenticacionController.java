package com.tienda.controller;

import com.tienda.model.Usuario;
import com.tienda.repository.UsuarioRepository;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AutenticacionController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/registro")
    @SuppressWarnings("unused")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        System.out.println("GET /registro - Formulario cargado");
        return "registro";
    }

    @PostMapping("/registro")
    @Transactional
    @SuppressWarnings("unused")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        System.out.println("=== DEBUG REGISTRO ===");
        System.out.println("DATOS RECIBIDOS:");
        System.out.println("Username: '" + usuario.getUsername() + "'");
        System.out.println("Email: '" + usuario.getEmail() + "'");
        System.out.println("Password: '" + usuario.getPassword() + "'");
        System.out.println("Longitud password: " + (usuario.getPassword() != null ? usuario.getPassword().length() : "null"));

        // Errores de validación.
        if (result.hasErrors()) {
            System.out.println("ERRORES DE VALIDACIÓN ENCONTRADOS:");
            for (FieldError error : result.getFieldErrors()) {
                System.out.println("Campo: " + error.getField());
                System.out.println("Mensaje: " + error.getDefaultMessage());
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            return "registro";
        }

        System.out.println("TODAS LAS VALIDACIONES PASARON");

        // Verificar si usuario existe
        System.out.println("Verificando si usuario existe: " + usuario.getUsername());
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(usuario.getUsername());
        if (usuarioExistente.isPresent()) {
            System.out.println("Usuario ya existe: " + usuario.getUsername());
            model.addAttribute("usernameError", "El nombre de usuario ya está registrado");
            return "registro";
        }
        System.out.println("Usuario disponible: " + usuario.getUsername());

        // Verificar si email existe
        System.out.println("Verificando si email existe: " + usuario.getEmail());
        Optional<Usuario> emailExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (emailExistente.isPresent()) {
            System.out.println("Email ya existe: " + usuario.getEmail());
            model.addAttribute("emailError", "El email ya está registrado");
            return "registro";
        }
        System.out.println("Email disponible: " + usuario.getEmail());

        // Procesamos registro.
        try {
            System.out.println("Cifrando contraseña...");
            String passwordCifrada = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(passwordCifrada);
            System.out.println("Password cifrado: " + passwordCifrada.substring(0, 20) + "...");

            if (usuario.getRol() == null) {
                usuario.setRol("USER");
                System.out.println("Rol asignado: USER");
            }

            // Guardamos en la db.
            System.out.println("Intentando guardar usuario en la base de datos...");
            System.out.println("Datos del usuario:");
            System.out.println("Username: " + usuario.getUsername());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Rol: " + usuario.getRol());

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("USUARIO REGISTRADO EXITOSAMENTE: " + usuarioGuardado.getUsername());
            System.out.println("ID: " + usuarioGuardado.getId());
            System.out.println("Email: " + usuarioGuardado.getEmail());
            System.out.println("Rol: " + usuarioGuardado.getRol());

        } catch (Exception e) {
            System.out.println("ERROR AL GUARDAR EN BD: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al crear la cuenta: " + e.getMessage());
            return "registro";
        }

        // Verificación adicional.
        try {
            long totalUsuarios = usuarioRepository.countUsuarios();
            System.out.println("TOTAL DE USUARIOS EN BD: " + totalUsuarios);
        } catch (Exception e) {
            System.out.println("No se pudo contar usuarios: " + e.getMessage());
        }

        System.out.println("=== FIN DEBUG REGISTRO ===");
        System.out.println("Redirigiendo a login con mensaje de éxito");
        redirectAttributes.addFlashAttribute("registroExitoso", "¡Registro exitoso! Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }
}