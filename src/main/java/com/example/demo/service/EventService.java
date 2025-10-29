package com.example.demo.service;

import com.example.demo.dto.response.EventDTO;
import com.example.demo.mapper.EventMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.EventOwner;
import com.example.demo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<EventDTO> getAllEventsInRange(EventOwner eventOwner, Instant startInstant, Instant endInstant) {
        return eventRepository.findByEventOwnerAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(eventOwner, startInstant, endInstant).stream()
                .map(eventMapper::toDTO)
                .toList();
    }
}
