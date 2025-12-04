package com.example.demo.controller;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.dto.response.EventDTO;
import com.example.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventOwners")
@RequiredArgsConstructor
public class EventOwnerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventOwnerController.class);
    private final EventService eventService;

    @PostMapping("/{eventOwnerId}/events")
    public ResponseEntity<EventDTO> createEventForEventOwner(
            @PathVariable UUID eventOwnerId,
            @RequestBody EventRequestDTO eventRequestDTO) {
        LOGGER.info("Create event for event owner id {}", eventOwnerId);
        return ResponseEntity.ok(eventService.createEvent(eventOwnerId, eventRequestDTO));
    }

    @GetMapping("/{eventOwnerId}/events/{startInstant}/{endInstant}")
    public ResponseEntity<List<EventDTO>> getAllEventsInRangeForOwner(
            @PathVariable UUID eventOwnerId,
            @PathVariable Instant startInstant,
            @PathVariable Instant endInstant) {
        LOGGER.info("Get all events for event owner id {}, startInstant {} and endInstant {}", eventOwnerId, startInstant, endInstant);
        return ResponseEntity.ok(
                eventService.getAllEventsInRangeForEventOwner(eventOwnerId, startInstant, endInstant)
        );
    }
}
