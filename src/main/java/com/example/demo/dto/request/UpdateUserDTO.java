package com.example.demo.dto.request;

import com.example.demo.model.Color;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private Color color;
}
