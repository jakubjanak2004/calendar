package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateGroupDTO {
    private String name;
    private List<UUID> usersIdsList;
}
