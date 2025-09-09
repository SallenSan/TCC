package com.tcc.consultas;

import com.tcc.consultas.model.UsuarioAdmin;
import com.tcc.consultas.repository.UsuarioAdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ConsultasPsicologicasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultasPsicologicasApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(UsuarioAdminRepository usuarioRepo,
								PasswordEncoder encoder) {
		return args -> {
			// cria usuário admin caso não exista
			final String adminEmail = "admin@system.com";
			final String senhaPadrao = "123456";

			if (usuarioRepo.findByEmail(adminEmail).isEmpty()) {
				UsuarioAdmin admin = new UsuarioAdmin();
				admin.setNome("Administrador do Sistema");
				admin.setEmail(adminEmail);
				admin.setSenhaHash(encoder.encode(senhaPadrao)); // hash da senha
				usuarioRepo.save(admin);

				System.out.println("✅ Usuário ADMIN criado: " + adminEmail + " / " + senhaPadrao);
			}
		};
	}
}
