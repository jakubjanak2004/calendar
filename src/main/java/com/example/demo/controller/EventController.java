package com.example.demo.controller;

import com.example.demo.dto.response.EventDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.EventOwner;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final UserRepository userRepository;

    @GetMapping("/{startInstant}/{endInstant}")
    public ResponseEntity<List<EventDTO>> getAllEventsInRange(
            @PathVariable Instant startInstant,
            @PathVariable Instant endInstant,
            Principal principal) {
        CalendarUser calendarUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        return eventService.getAllEventsInRange(startInstant, endInstant, calendarUser);
    }
}
