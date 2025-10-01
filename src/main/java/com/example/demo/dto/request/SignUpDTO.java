package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpDTO {
    @NotBlank
    @Size(max=100)
    private String firstName;

    @NotBlank
    @Size(max=100)
    private String lastName;

    @NotBlank
    @Size(min=5, max=20)
    private String username;

    @NotBlank
    @Size(min=10, max=50)
    private String password;
}