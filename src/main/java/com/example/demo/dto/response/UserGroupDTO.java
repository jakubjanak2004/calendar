package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserGroupDTO {
    private UUID id;
    private String name;
}
