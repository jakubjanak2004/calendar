package com.example.demo.service;

import com.example.demo.config.JwtProps;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.exception.UsernameAlreadyExistsException;
import com.example.demo.mapper.ColorMapper;
import com.example.demo.mapper.SignUpMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.repository.CalendarUserRepository;
import jakarta.validation.Valid;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Validated
@Transactional
public class AuthService {
    private final AuthenticationManager authManager;
    private final CalendarUserRepository calendarUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtProps jwtProps;
    private final SignUpMapper signUpMapper;
    private final ColorMapper colorMapper;

    public AuthResponseDTO login(@Valid LoginDTO loginDTO) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        CalendarUser user = (CalendarUser) auth.getPrincipal();
        String token = generateToken(user);
        // todo move this to mapper
        return new AuthResponseDTO(
                user.getId(),
                token,
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                colorMapper.toDTO(user.getColor())
        );
    }

    public AuthResponseDTO signup(@Valid SignUpDTO signupDTO) {
        if (calendarUserRepository.existsByUsername(signupDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }
        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        CalendarUser calendarUser = signUpMapper.toEntity(signupDTO);
        calendarUserRepository.save(calendarUser);
        String token = generateToken(calendarUser);
        // todo move this to mapper
        return new AuthResponseDTO(
                calendarUser.getId(),
                token,
                calendarUser.getUsername(),
                calendarUser.getFirstName(),
                calendarUser.getLastName(),
                colorMapper.toDTO(calendarUser.getColor())
        );
    }

    private String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer(jwtProps.issuer()).issuedAt(now).expiresAt(now.plus(jwtProps.accessTtl())).subject(userDetails.getUsername()).build();
        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
    }

    public boolean usernameTaken(String username) {
        return calendarUserRepository.existsByUsername(username);
    }
}
