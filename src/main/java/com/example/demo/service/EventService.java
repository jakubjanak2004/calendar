package com.example.demo.service;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.dto.response.EventDTO;
import com.example.demo.mapper.EventMapper;
import com.example.demo.mapper.EventRequestMapper;
import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import com.example.demo.repository.EventOwnerRepository;
import com.example.demo.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventRequestMapper eventRequestMapper;
    private final EventOwnerRepository eventOwnerRepository;

    @PreAuthorize("@eventSecurity.canAccessEventOwner(#eventOwnerId, authentication)")
    public List<EventDTO> getAllEventsInRangeForEventOwner(UUID eventOwnerId, Instant startInstant, Instant endInstant) {
        return eventRepository.findByEventOwnerIdAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(eventOwnerId, startInstant, endInstant).stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    @PreAuthorize("@eventSecurity.isOwner(#eventId, authentication)")
    public void updateEvent(UUID eventId, @Valid EventRequestDTO eventDTO) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        eventRequestMapper.updateEntity(event, eventDTO);
        eventRepository.save(event);
    }

    @PreAuthorize("@eventSecurity.canEditEventOwner(#eventOwnerId, authentication)")
    public EventDTO createEvent(UUID eventOwnerId, @Valid EventRequestDTO eventRequestDTO) {
        EventOwner owner = eventOwnerRepository.findById(eventOwnerId).orElseThrow();
        Event event = eventRequestMapper.toEntity(eventRequestDTO);
        event.setEventOwner(owner);
        Event saved = eventRepository.save(event);
        return eventMapper.toDTO(saved);
    }

    @PreAuthorize("@eventSecurity.isOwner(#eventId, authentication)")
    public void deleteEvent(UUID eventId) {
        eventRepository.deleteById(eventId);
    }

    @PreAuthorize("@eventSecurity.isOwner(#eventId, authentication)")
    public EventDTO getEvent(UUID eventId) {
        return eventRepository.findById(eventId)
                .map(eventMapper::toDTO)
                .orElseThrow();
    }
}
