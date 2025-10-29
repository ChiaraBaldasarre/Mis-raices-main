package com.tienda.service;

import com.tienda.model.Usuario;
import com.tienda.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            System.err.println("Usuario no encontrado en la Base de Datos: " + username);
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Usuario usuario = usuarioOpt.get();
        System.out.println("Usuario encontrado: " + username + ", Rol: " + usuario.getRol());

        // Crear authorities (roles)
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol())
        );

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}