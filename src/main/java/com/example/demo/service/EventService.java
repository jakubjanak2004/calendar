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
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public ResponseEntity<List<EventDTO>> getAllEventsInRange(Instant startInstant, Instant endInstant, EventOwner eventOwner) {
        List<EventDTO> eventDTOList = eventRepository.findByEventOwnerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(eventOwner, endInstant, startInstant).stream()
                .map(eventMapper::toDTO)
                .toList();
        return ResponseEntity.ok(eventDTOList);
    }
}
