package com.tienda.controller;

import com.tienda.model.ItemCarrito;
import com.tienda.model.Pedido;
import com.tienda.model.Usuario;
import com.tienda.service.CarritoService;
import com.tienda.service.PedidoService;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario obtenerUsuarioActual(Authentication authentication) {
        String username = authentication.getName();
        return usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping("/carrito")
    public String mostrarCarrito(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Usuario usuario = obtenerUsuarioActual(authentication);
        List<ItemCarrito> items = carritoService.obtenerCarritoPorUsuario(usuario);

        model.addAttribute("itemsCarrito", items);
        model.addAttribute("totalCarrito", carritoService.getTotal(items));
        model.addAttribute("cantidadTotal", carritoService.getCantidadTotalProductos(items));
        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("id") Long id,
                                   @RequestParam(value = "cantidad", defaultValue = "1") int cantidad,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para agregar productos al carrito");
            return "redirect:/login";
        }

        try {
            Usuario usuario = obtenerUsuarioActual(authentication);
            carritoService.agregarProducto(usuario, id, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam("id") Long id,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = obtenerUsuarioActual(authentication);
            carritoService.eliminarItem(usuario, id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizarCantidad(@RequestParam("id") Long id,
                                     @RequestParam("cantidad") int cantidad,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = obtenerUsuarioActual(authentication);
            carritoService.actualizarItem(usuario, id, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Cantidad actualizada.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/checkout")
    public String procesarCheckout(@RequestParam("direccion") String direccion,
                                   @RequestParam("contacto") String contacto,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para realizar una compra");
            return "redirect:/login";
        }

        try {
            Usuario usuario = obtenerUsuarioActual(authentication);
            List<ItemCarrito> items = carritoService.obtenerCarritoPorUsuario(usuario);

            if (items.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El carrito está vacío");
                return "redirect:/carrito";
            }

            if (direccion.trim().isEmpty() || contacto.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Por favor completa todos los campos");
                return "redirect:/carrito";
            }

            Pedido pedido = pedidoService.crearPedidoDesdeCarrito(usuario, direccion, contacto, items);

            // Limpiamos el carrito de la base de datos tras la compra exitosa
            carritoService.limpiarCarrito(usuario);

            boolean esPrimeraCompra = !usuario.isEsCliente();

            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Pedido procesado exitosamente! Te contactaremos pronto. " +
                            (esPrimeraCompra ? "¡Bienvenido como cliente!" : ""));
            return "redirect:/productos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pedido: " + e.getMessage());
            return "redirect:/carrito";
        }
    }
}