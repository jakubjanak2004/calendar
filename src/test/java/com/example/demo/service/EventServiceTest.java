package com.example.demo.service;

import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.EventSecurity;
import com.example.demo.service.utils.Generator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@SpringBootTest
@WithMockUser
@Import({EventServiceTest.MethodSec.class, EventSecurity.class})
@Transactional
public class EventServiceTest {
    private static final String EVENT_OWNER_USERNAME = "test";
    private final Generator generator;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private CalendarUser calendarUser;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public EventServiceTest(Generator generator, UserRepository userRepository, EventRepository eventRepository, EventService eventService) {
        this.generator = generator;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @BeforeEach
    void initData() {
        if (calendarUser != null) {
            userRepository.delete(calendarUser);
        }
        calendarUser = userRepository.save(generator.createUser(EVENT_OWNER_USERNAME, "testPassword"));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateEventThrowsAuthorizationDeniedExceptionWhenTheEventWithEventIdDoesNotExist() {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                "title",
                "description",
                Instant.now().minus(1, ChronoUnit.HOURS),
                Instant.now().minus(30, ChronoUnit.MINUTES)
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.updateEvent(UUID.randomUUID(), eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateEventThrowsExceptionWhenUserIsNotOwnerOfThatEvent() {
        CalendarUser calendarUser = userRepository.save(generator.createUser());
        Event event = eventRepository.save(
                generator.createEvent(
                        calendarUser,
                        Instant.now().plus(30, ChronoUnit.MINUTES),
                        Instant.now().plus(1, ChronoUnit.HOURS)
                )
        );
        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                "updated title",
                "updated description",
                Instant.now().minus(1, ChronoUnit.HOURS),
                Instant.now().minus(30, ChronoUnit.MINUTES)
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.updateEvent(event.getUuid(), eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateEventThrowsExceptionWhenEndTimeIsBeforeStartTime() {
        Event event = eventRepository.save(
                generator.createEvent(
                        calendarUser,
                        Instant.now(),
                        Instant.now().plus(30, ChronoUnit.MINUTES)
                )
        );

        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                event.getTitle(),
                event.getDescription(),
                Instant.now(),
                Instant.now().minus(1, ChronoUnit.MINUTES)
        );
        Assertions.assertThrows(ConstraintViolationException.class, () -> eventService.updateEvent(event.getUuid(), eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteEventThrowsAuthorizationDeniedExceptionWhenTheEventWithEventIdDoesNotExist() {
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.deleteEvent(UUID.randomUUID()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteEventThrowsExceptionWhenUserIsNotOwnerOfThatEvent() {
        CalendarUser calendarUser = userRepository.save(generator.createUser());
        Event event = eventRepository.save(
                generator.createEvent(
                        calendarUser,
                        Instant.now().plus(30, ChronoUnit.MINUTES),
                        Instant.now().plus(1, ChronoUnit.HOURS)
                )
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.deleteEvent(event.getUuid()));
    }

    @Test
    public void getAllEventsDoesntReturnEventWithBothStartAndEndBeforeStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        Instant eventEndTime = startTime.minus(1, ChronoUnit.HOURS);
        Instant eventStartTime = eventEndTime.minus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(calendarUser, eventStartTime, eventEndTime));

        Assertions.assertTrue(eventService.getAllEventsInRangeForEventOwner(calendarUser, startTime, endTime).isEmpty());
    }

    @Test
    public void getAllEventsDoesntReturnEventWithBothStartAndEndAfterStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        Instant eventStartTime = endTime.plus(1, ChronoUnit.HOURS);
        Instant eventEndTime = eventStartTime.plus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(calendarUser, eventStartTime, eventEndTime));

        Assertions.assertTrue(eventService.getAllEventsInRangeForEventOwner(calendarUser, startTime, endTime).isEmpty());
    }

    @Test
    public void getAllEventsReturnsEventIfStartBeforeEndInstantAndEndIsAfterStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(calendarUser, startTime.plus(30, ChronoUnit.MINUTES), startTime.plus(2, ChronoUnit.HOURS)));
        eventRepository.save(generator.createEvent(calendarUser, startTime.minus(1, ChronoUnit.HOURS), startTime.plus(30, ChronoUnit.MINUTES)));
        eventRepository.save(generator.createEvent(calendarUser, startTime.plus(10, ChronoUnit.MINUTES), endTime.minus(10, ChronoUnit.MINUTES)));

        Assertions.assertEquals(3, eventService.getAllEventsInRangeForEventOwner(calendarUser, startTime, endTime).size());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSec {
    }
}
