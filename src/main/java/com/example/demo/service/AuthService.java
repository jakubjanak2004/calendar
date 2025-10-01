package com.example.demo.service;

import com.example.demo.config.JwtProps;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.UsernameExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtProps jwtProps;

    public AuthResponseDTO login(LoginDTO loginDTO) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        CalendarUser user = (CalendarUser) auth.getPrincipal();
        String token = generateToken(user);
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO signup(SignUpDTO signupDTO) {
        if (userRepository.existsByUsername(signupDTO.getUsername())) {
            throw new UsernameExistsException("Username already taken");
        }
        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        CalendarUser calendarUser = CalendarUserMapper.toEntity(signupDTO);
        userRepository.save(calendarUser);
        String token = generateToken(calendarUser);
        return new AuthResponseDTO(token);
    }

    private String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProps.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProps.accessTtl()))
                .subject(userDetails.getUsername()).build();

        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
    }
}
