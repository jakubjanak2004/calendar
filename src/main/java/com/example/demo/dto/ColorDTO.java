package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ColorDTO {
    public static final ColorDTO RED = new ColorDTO("#ff0000");
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Color must be in hex format, e.g. #RRGGBB")
    private String color;

    @JsonValue
    public String toJson() {
        return color;
    }

    @JsonCreator
    public static ColorDTO fromJson(String value) {
        return new ColorDTO(value);
    }
}
