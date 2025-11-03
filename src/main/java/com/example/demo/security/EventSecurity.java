package com.example.demo.security;

import com.example.demo.model.CalendarUser;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component("eventSecurity")
@RequiredArgsConstructor
public class EventSecurity {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public boolean isOwner(UUID eventId, Authentication auth) {
        CalendarUser calendarUser = userRepository.findByUsername(auth.getName()).orElseThrow();
        return eventRepository.findOwnerIdById(eventId)
                .map(ownerId -> ownerId.equals(calendarUser.getId()))
                .orElse(false);
    }
}
