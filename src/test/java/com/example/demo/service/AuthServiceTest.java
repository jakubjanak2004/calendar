package com.example.demo.service;

import com.example.demo.config.JwtProps;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.exception.UsernameExistsException;
import com.example.demo.model.CalendarUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.utils.Generator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthServiceTest {
    private final String password = "password";
    private final JwtDecoder jwtDecoder;
    private final Generator generator;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtProps jwtProps;
    private CalendarUser calendarUser;

    @BeforeEach
    void setUp() {
        calendarUser = userRepository.save(generator.createUser(password));
    }

    @Test
    public void loginReturnsValidTokenForExistingUser() {
        AuthResponseDTO token = authService.login(new LoginDTO(
                calendarUser.getUsername(),
                password
        ));
        isValidToken(token, calendarUser.getUsername());
    }

    @Test
    public void loginThrowsAuthenticationExceptionForNonExistingUser() {
        assertThrows(BadCredentialsException.class,
                () -> authService.login(new LoginDTO("not existing", "not existing"))
        );
    }

    @Test
    public void signupReturnsValidTokenForNewUser() {
        String username = "username that is unique";
        AuthResponseDTO token = authService.signup(new SignUpDTO(
                "firstName",
                "lastName",
                username,
                "password"
        ));
        isValidToken(token, username);
    }


    @Test
    public void signupThrowsUsernameExistsExceptionIfNewUsernameIsUsed() {
        assertThrows(UsernameExistsException.class,
                () -> authService.signup(new SignUpDTO(
                        "firstName",
                        "secondName",
                        calendarUser.getUsername(),
                        "password"
                ))
        );
    }

    private void isValidToken(AuthResponseDTO token, String username) {
        // 1) Decode & verify signature using the app’s JwtDecoder
        Jwt jwt = jwtDecoder.decode(token.getToken());
        // 2) Assert header & algorithm
        assertThat(jwt.getHeaders().get("alg")).isEqualTo(jwtProps.algo());
        // 3) Assert core claims
        assertThat(jwt.getIssuer().toString()).isEqualTo(jwtProps.issuer());
        assertThat(jwt.getSubject()).isEqualTo(username);
        assertThat(jwt.getIssuedAt()).isNotNull();
        assertThat(jwt.getExpiresAt()).isAfter(jwt.getIssuedAt());
    }
}
