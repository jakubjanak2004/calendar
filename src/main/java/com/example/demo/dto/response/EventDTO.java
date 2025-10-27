package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;

@Getter
@AllArgsConstructor
public class EventDTO {
    private String title;
    private String description;
    private ZoneId timeZone;
    private Instant startTime;
    private Instant endTime;
}
