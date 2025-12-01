package com.example.demo.service;

import com.example.demo.SystemTest;
import com.example.demo.dto.request.EventRequestDTO;
import com.example.demo.dto.response.EventDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.security.EventSecurity;
import com.example.demo.service.utils.Generator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@WithMockUser
@Import({EventServiceTest.MethodSec.class, EventSecurity.class})
public class EventServiceTest extends SystemTest {
    private final Generator generator;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private CalendarUser calendarUser;
    @Autowired
    private CalendarUserRepository calendarUserRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    public EventServiceTest(Generator generator, CalendarUserRepository calendarUserRepository, EventRepository eventRepository, EventService eventService) {
        this.generator = generator;
        this.calendarUserRepository = calendarUserRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @BeforeEach
    void initData() {
        if (calendarUser != null) {
            calendarUserRepository.delete(calendarUser);
        }
        calendarUser = calendarUserRepository.save(generator.createUser(EVENT_OWNER_USERNAME, "testPassword"));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateEventThrowsNoSuchElementExceptionWhenTheEventWithEventIdDoesNotExist() {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                "title",
                "description",
                Instant.now().minus(1, ChronoUnit.HOURS),
                Instant.now().minus(30, ChronoUnit.MINUTES)
        );
        Assertions.assertThrows(NoSuchElementException.class, () -> eventService.updateEvent(UUID.randomUUID(), eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateEventThrowsAuthorizationDeniedExceptionWhenUserIsNotOwnerOfThatEvent() {
        CalendarUser calendarUser = calendarUserRepository.save(generator.createUser());
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
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.updateEvent(event.getId(), eventRequestDTO));
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
        Assertions.assertThrows(ConstraintViolationException.class, () -> eventService.updateEvent(event.getId(), eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void createEventThrowsAuthorizationDeniedExceptionWhenUserIsNotAuthorizedToEditEventOwner() {
        EventOwner eventOwner =  userGroupRepository.save(generator.createUserGroup("Group"));
        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                "title",
                "description",
                Instant.now(),
                Instant.now().plus(30, ChronoUnit.MINUTES)
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.createEvent(eventOwner.getId(), eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void createEventThrowsConstraintViolationExceptionWhenEndTimeIsBeforeStartTime() {
        UserGroup userGroup = userGroupRepository.save(generator.createUserGroup("Group"));
        userGroup.addCalendarUsersToGroup(List.of(calendarUser), MembershipRole.EDITOR);
        UUID groupId = userGroupRepository.save(userGroup).getId();
        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                "title",
                "description",
                Instant.now(),
                Instant.now().minus(30, ChronoUnit.MINUTES)
        );
        Assertions.assertThrows(ConstraintViolationException.class, () -> eventService.createEvent(groupId, eventRequestDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void createEventCreatesNewEventForEventOwner() {
        UserGroup userGroup = userGroupRepository.save(generator.createUserGroup("Group"));
        userGroup.addCalendarUsersToGroup(List.of(calendarUser), MembershipRole.EDITOR);
        userGroupRepository.save(userGroup);
        EventRequestDTO eventRequestDTO = new EventRequestDTO(
                "title",
                "description",
                Instant.now(),
                Instant.now().plus(30, ChronoUnit.MINUTES)
        );
        EventDTO eventDTO = eventService.createEvent(userGroup.getId(), eventRequestDTO);
        Assertions.assertTrue(eventRepository.existsById(eventDTO.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteEventThrowsAuthorizationDeniedExceptionWhenTheEventWithEventIdDoesNotExist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> eventService.deleteEvent(UUID.randomUUID()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteEventThrowsAuthorizationDeniedExceptionWhenUserIsNotOwnerOfThatEvent() {
        CalendarUser calendarUser = calendarUserRepository.save(generator.createUser());
        Event event = eventRepository.save(
                generator.createEvent(
                        calendarUser,
                        Instant.now().plus(30, ChronoUnit.MINUTES),
                        Instant.now().plus(1, ChronoUnit.HOURS)
                )
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.deleteEvent(event.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllEventsDoesntReturnEventWithBothStartAndEndBeforeStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        Instant eventEndTime = startTime.minus(1, ChronoUnit.HOURS);
        Instant eventStartTime = eventEndTime.minus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(calendarUser, eventStartTime, eventEndTime));

        Assertions.assertTrue(eventService.getAllEventsInRangeForEventOwner(calendarUser.getId(), startTime, endTime).isEmpty());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllEventsDoesntReturnEventWithBothStartAndEndAfterStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        Instant eventStartTime = endTime.plus(1, ChronoUnit.HOURS);
        Instant eventEndTime = eventStartTime.plus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(calendarUser, eventStartTime, eventEndTime));

        Assertions.assertTrue(eventService.getAllEventsInRangeForEventOwner(calendarUser.getId(), startTime, endTime).isEmpty());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllEventsReturnsEventIfStartBeforeEndInstantAndEndIsAfterStartInstant() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(1, ChronoUnit.HOURS);
        eventRepository.save(generator.createEvent(calendarUser, startTime.plus(30, ChronoUnit.MINUTES), startTime.plus(2, ChronoUnit.HOURS)));
        eventRepository.save(generator.createEvent(calendarUser, startTime.minus(1, ChronoUnit.HOURS), startTime.plus(30, ChronoUnit.MINUTES)));
        eventRepository.save(generator.createEvent(calendarUser, startTime.plus(10, ChronoUnit.MINUTES), endTime.minus(10, ChronoUnit.MINUTES)));

        Assertions.assertEquals(3, eventService.getAllEventsInRangeForEventOwner(calendarUser.getId(), startTime, endTime).size());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getEventThrowsAuthorizationDeniedExceptionWhenUserIsNotOwnerOfThatEvent() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        Event event = generator.createEvent(testUser, 5, 60);
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> eventService.getEvent(event.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getEventThrowsNoSuchElementExceptionWhenEventDoesNotExist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> eventService.getEvent(UUID.randomUUID()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getEventReturnsEventForUserThatIsOwnerOfThatEvent() {
        Event event = generator.createEvent(calendarUser, 5, 60);
        EventDTO eventDTO = eventService.getEvent(event.getId());
        Assertions.assertEquals(event.getId(), eventDTO.getId());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSec {
    }
}
