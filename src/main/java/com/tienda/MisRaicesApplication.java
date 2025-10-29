package com.tienda;

import com.tienda.model.Usuario;
import com.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class MisRaicesApplication {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(MisRaicesApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData() {
		return args -> {
			// Verificar si el usuario admin ya existe
			Optional<Usuario> adminExistente = usuarioRepository.findByEmail("admin@tienda.com");
			if (adminExistente.isEmpty()) {
				Usuario admin = new Usuario();
				admin.setUsername("admin");
				admin.setEmail("admin@tienda.com");
				admin.setPassword(passwordEncoder.encode("123456")); // Asegúrate de cifrarla
				admin.setRol("ADMIN");
				admin.setEsCliente(true);
				usuarioRepository.save(admin);
				System.out.println("Usuario admin creado exitosamente.");
			} else {
				System.out.println("El usuario admin ya existe en la base de datos.");
				// Verifica si la contraseña está cifrada
				Usuario admin = adminExistente.get();
				System.out.println("Password del admin: " + admin.getPassword());
			}
		};
	}
}