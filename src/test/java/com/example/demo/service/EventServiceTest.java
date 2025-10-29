package com.example.demo.service;

import com.example.demo.model.EventOwner;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.utils.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class EventServiceTest {
    private final Generator generator;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final EventOwner eventOwner;

    @Autowired
    public EventServiceTest(Generator generator, UserRepository userRepository, EventRepository eventRepository, EventService eventService) {
        this.generator = generator;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        eventOwner = userRepository.save(generator.createUser());
    }

    @Test
    public void getAllEventsDoesntReturnEventWithBothStartAndEndBeforeStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        Instant eventEndTime = startTime.minus(1, ChronoUnit.HOURS);
        Instant eventStartTime = endTime.minus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(eventOwner, eventStartTime, eventEndTime));

        Assertions.assertTrue(eventService.getAllEventsInRange(eventOwner, startTime, endTime).isEmpty());
    }

    @Test
    public void getAllEventsDoesntReturnEventWithBothStartAndEndAfterStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        Instant eventStartTime = endTime.plus(1, ChronoUnit.HOURS);
        Instant eventEndTime = eventStartTime.plus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(eventOwner, eventStartTime, eventEndTime));

        Assertions.assertTrue(eventService.getAllEventsInRange(eventOwner, startTime, endTime).isEmpty());
    }

    @Test
    public void getAllEventsReturnsEventIfStartBeforeEndInstantAndEndIsAfterStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(eventOwner, startTime.plus(30, ChronoUnit.MINUTES), startTime.plus(2, ChronoUnit.HOURS)));
        eventRepository.save(generator.createEvent(eventOwner, startTime.minus(1, ChronoUnit.HOURS), startTime.plus(30, ChronoUnit.MINUTES)));
        eventRepository.save(generator.createEvent(eventOwner, startTime.plus(10, ChronoUnit.MINUTES), endTime.minus(10, ChronoUnit.MINUTES)));

        Assertions.assertEquals(3, eventService.getAllEventsInRange(eventOwner, startTime, endTime).size());
    }
}
