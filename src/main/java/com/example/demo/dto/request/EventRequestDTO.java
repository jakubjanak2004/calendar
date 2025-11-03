package com.example.demo.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class EventRequestDTO {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private Instant startTime;
    @NotNull
    private Instant endTime;

    @AssertTrue(message = "endTime must be after startTime")
    public boolean isChronological() {
        return startTime != null && endTime != null && endTime.isAfter(startTime);
    }
}
