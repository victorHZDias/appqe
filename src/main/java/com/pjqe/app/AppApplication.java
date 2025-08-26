package com.pjqe.app;

import com.pjqe.app.entity.SystemUser;
import com.pjqe.app.repository.SystemUserRepository;
import com.pjqe.app.service.SystemUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    // Bean para criar um usuário SUPERADMIN na inicialização, se necessário
    @Bean
    public CommandLineRunner createSystemUser(SystemUserRepository systemUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (systemUserRepository.findByUsername("admin").isEmpty()) {
                SystemUser admin = new SystemUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("adminpass")); // A senha será 'adminpass'
                admin.setRole("SUPERADMIN");
                systemUserRepository.save(admin);
                System.out.println("Usuário SUPERADMIN 'admin' criado com sucesso!");
            }
        };
    }
}