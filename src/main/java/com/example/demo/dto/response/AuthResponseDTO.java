package com.example.demo.dto.response;

import com.example.demo.dto.ColorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private UUID userId;
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private ColorDTO color;
}
