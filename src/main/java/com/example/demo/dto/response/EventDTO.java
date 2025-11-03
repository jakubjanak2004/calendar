package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class EventDTO {
    private UUID uuid;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
}
