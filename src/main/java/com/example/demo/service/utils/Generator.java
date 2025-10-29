package com.example.demo.service.utils;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
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

    public static String createStringAttribute(String attributeName) {
        return attributeName + " " + RANDOM.nextDouble(0, 10);
    }

    public CalendarUser createUser() {
        return createUser(createStringAttribute("password"));
    }

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
        return createEvent(
                eventOwner,
                zone,
                startDateTime.toInstant(),
                endDateTime.toInstant()
        );
    }

    public Event createEvent(EventOwner eventOwner, Instant startTime, Instant endTime) {
        ZoneId zone = ZoneId.of("Europe/Prague");
        return createEvent(
                eventOwner,
                zone,
                startTime,
                endTime
        );
    }

    public Event createEvent(EventOwner eventOwner, ZoneId zone, Instant startTime, Instant endTime) {
        return new Event(
                eventOwner,
                createStringAttribute("title"),
                createStringAttribute("description"),
                zone,
                startTime,
                endTime
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
}
