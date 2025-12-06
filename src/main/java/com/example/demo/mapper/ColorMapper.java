package com.example.demo.mapper;

import com.example.demo.dto.ColorDTO;
import com.example.demo.model.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper implements Mapper<Color, ColorDTO>{
    @Override
    public Color toEntity(ColorDTO colorDTO) {
        return new Color(colorDTO.getColor());
    }

    @Override
    public ColorDTO toDTO(Color color) {
        return new ColorDTO(color.getColor());
    }

    @Override
    public Color updateEntity(Color color, ColorDTO colorDTO) {
        return null;
    }
}
