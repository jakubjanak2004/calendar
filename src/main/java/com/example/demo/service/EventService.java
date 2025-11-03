package com.example.demo.service;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.dto.response.EventDTO;
import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.mapper.EventMapper;
import com.example.demo.mapper.EventRequestMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import com.example.demo.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CalendarUserMapper calendarUserMapper;
    private final EventRequestMapper eventRequestMapper;

    public List<EventDTO> getAllEventsInRangeForEventOwner(EventOwner eventOwner, Instant startInstant, Instant endInstant) {
        return eventRepository.findByEventOwnerAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(eventOwner, startInstant, endInstant).stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    @PreAuthorize("@eventSecurity.isOwner(#eventId, authentication)")
    public void updateEvent(UUID eventId, @Valid EventRequestDTO eventDTO) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        eventRequestMapper.updateEntity(event, eventDTO);
        eventRepository.save(event);
    }

    public EventDTO createEvent(EventOwner eventOwner, @Valid EventRequestDTO eventRequestDTO) {
        Event event = eventRequestMapper.toEntity(eventRequestDTO);
        event.setEventOwner(eventOwner);
        return eventMapper.toDTO(eventRepository.save(event));
    }

    @PreAuthorize("@eventSecurity.isOwner(#eventId, authentication)")
    public void deleteEvent(UUID eventId) {
        eventRepository.deleteById(eventId);
    }
}
