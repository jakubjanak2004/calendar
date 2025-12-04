package com.example.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Color {
    public static final Color RED = new Color("#ff0000");
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Color must be in hex format, e.g. #RRGGBB")
    private String color;

    @Override
    public String toString() {
        return color;
    }
}
