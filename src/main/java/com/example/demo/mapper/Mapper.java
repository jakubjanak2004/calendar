package com.example.demo.mapper;

public interface Mapper<Entity, DTO> {
    Entity toEntity(DTO dto);

    DTO toDTO(Entity entity);

    Entity updateEntity(Entity entity, DTO dto);
}
