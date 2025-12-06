package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateGroupDTO {
    @NotBlank
    private String name;
    @NotNull
    private List<@NotNull UUID> usersIdsList;
}
