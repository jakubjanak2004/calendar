package com.example.demo.controller;

import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> logIn(@Validated @RequestBody LoginDTO loginDTO) {
        AuthResponseDTO authResponseDTO = authService.login(loginDTO);
        LOGGER.info("User {} logged in.", loginDTO.getUsername());
        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signUp(@Validated @RequestBody SignUpDTO signupDTO) {
        AuthResponseDTO authResponseDTO = authService.signup(signupDTO);
        LOGGER.info("User {} singed up.", signupDTO.getUsername());
        return ResponseEntity.ok(authResponseDTO);
    }

    // todo add a refresh token
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name="REFRESH_TOKEN") String refreshToken) {
        LOGGER.info("Received refresh token: {}", refreshToken);
        return ResponseEntity.ok("TO BE IMPLEMENTED");
    }

    @GetMapping("/usernameTaken")
    public ResponseEntity<Boolean> usernameTaken(@RequestParam("username") String username) {
        LOGGER.info("Received username taken: {}", username);
        return ResponseEntity.ok(authService.usernameTaken(username));
    }
}
