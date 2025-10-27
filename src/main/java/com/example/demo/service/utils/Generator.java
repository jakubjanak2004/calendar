package com.example.demo.service.utils;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class Generator {
    private static final Random RANDOM = new Random();
    private final PasswordEncoder passwordEncoder;

    public CalendarUser createUser(String password) {
        return createUser(
                createStringAttribute("firstName"),
                createStringAttribute("lastName"),
                createStringAttribute("username"),
                password
        );
    }

    public CalendarUser createUser(String username, String password) {
        String firstName = createStringAttribute("firstName");
        String lastName = createStringAttribute("lastName");
        return createUser(firstName, lastName, username, password);
    }

    public CalendarUser createUser(String firstName, String lastName, String username, String password) {
        return new CalendarUser(
                firstName,
                lastName,
                username,
                passwordEncoder.encode(password)
        );
    }

    public Event createEvent(EventOwner eventOwner, int daysOffset, int eventDurationMinutes) {
        ZoneId zone = ZoneId.of("Europe/Prague");
        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime startDateTime = now.plusDays(daysOffset);
        ZonedDateTime endDateTime = startDateTime.plusMinutes(eventDurationMinutes);
        return new Event(
                eventOwner,
                createStringAttribute("title"),
                createStringAttribute("description"),
                zone,
                startDateTime.toInstant(),
                endDateTime.toInstant()
        );
    }

    public List<Event> createEvents(EventOwner eventOwner, List<Integer> daysOffsets, List<Integer> eventDurationMinutes) {
        if (daysOffsets.size() != eventDurationMinutes.size()) {
            throw new IllegalArgumentException("daysOffsets.size() != eventDurationMinutes.size()");
        }

        List<Event> events = new ArrayList<>();
        for (int i = 0; i < daysOffsets.size(); i++) {
            events.add(createEvent(eventOwner, daysOffsets.get(i), eventDurationMinutes.get(i)));
        }
        return events;
    }

    public String createStringAttribute(String attributeName) {
        return attributeName + " " + RANDOM.nextDouble(0, 10);
    }
}
