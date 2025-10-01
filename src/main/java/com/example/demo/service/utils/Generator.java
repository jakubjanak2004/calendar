package com.example.demo.service.utils;

import com.example.demo.model.CalendarUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class Generator {
    private static final Random RANDOM = new Random();
    private final PasswordEncoder passwordEncoder;

    public CalendarUser createUser(String password) {
        return new CalendarUser(
                "firstName " + RANDOM.nextDouble(0, 10),
                "lastName " + RANDOM.nextDouble(0, 10),
                "username " + RANDOM.nextDouble(0, 10),
                passwordEncoder.encode(password)
        );
    }
}
