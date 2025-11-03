package com.example.demo;

import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final AuthService authService;

    @Override
    public void run(String... args) {
        SignUpDTO adminUser = new SignUpDTO("Admin", "Admin", "admin", "admin");
        authService.signup(adminUser);
    }
}
