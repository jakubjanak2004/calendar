package com.example.demo.controller;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.dto.response.EventDTO;
import com.example.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
// todo determine if needed
public class EventController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable UUID eventId) {
        LOGGER.info("Get event: {}", eventId);
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventRequestDTO> updateEvent(@PathVariable UUID eventId, @RequestBody EventRequestDTO eventDTO) {
        LOGGER.info("Update event: {}", eventId);
        eventService.updateEvent(eventId, eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        LOGGER.info("Delete event: {}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
