package com.example.demo.dto.request;

import com.example.demo.model.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private Color color;
}
