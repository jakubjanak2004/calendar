package com.example.demo.controller;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.dto.response.EventDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventRequestDTO eventRequestDTO, Principal principal) {
        CalendarUser calendarUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(eventService.createEvent(calendarUser, eventRequestDTO));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventRequestDTO> updateEvent(@PathVariable UUID eventId, @RequestBody EventRequestDTO eventDTO) {
        eventService.updateEvent(eventId, eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{startInstant}/{endInstant}")
    public ResponseEntity<List<EventDTO>> getAllEventsInRange(
            @PathVariable Instant startInstant,
            @PathVariable Instant endInstant,
            Principal principal) {
        CalendarUser calendarUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(eventService.getAllEventsInRangeForEventOwner(calendarUser, startInstant, endInstant));
    }
}
