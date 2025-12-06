package com.example.demo.service.utils;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import com.example.demo.enumeration.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Transactional
public class Generator {
    private static final Random RANDOM = new Random();
    private final PasswordEncoder passwordEncoder;
    private final CalendarUserRepository calendarUserRepository;
    private final UserGroupRepository userGroupRepository;
    private final EventRepository eventRepository;

    public static String createStringAttribute(String attributeName) {
        return attributeName + " " + RANDOM.nextDouble(0, 10);
    }

    public CalendarUser createUser() {
        return createUser(createStringAttribute("password"));
    }

    public CalendarUser createUser(String password) {
        return createUser(createStringAttribute("firstName"), createStringAttribute("lastName"), createStringAttribute("username"), password);
    }

    public CalendarUser createUser(String username, String password) {
        String firstName = createStringAttribute("firstName");
        String lastName = createStringAttribute("lastName");
        return createUser(firstName, lastName, username, password);
    }

    public CalendarUser createUser(String firstName, String lastName, String username, String password) {
        return calendarUserRepository.save(new CalendarUser(firstName, lastName, username, passwordEncoder.encode(password)));
    }

    public Event createEvent(EventOwner eventOwner, int daysOffset, int eventDurationMinutes) {
        ZoneId zone = ZoneId.of("Europe/Prague");
        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime startDateTime = now.plusDays(daysOffset);
        ZonedDateTime endDateTime = startDateTime.plusMinutes(eventDurationMinutes);
        return createEvent(eventOwner, startDateTime.toInstant(), endDateTime.toInstant());
    }

    public Event createEvent(EventOwner eventOwner, Instant startTime, Instant endTime) {
        return eventRepository.save(new Event(eventOwner, createStringAttribute("title"), createStringAttribute("description"), startTime, endTime));
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

    public List<CalendarUser> createUsers(String usernameStart, String password, int n) {
        return IntStream.rangeClosed(1, n).mapToObj(i -> createUser(createStringAttribute(usernameStart), password)).toList();
    }

    public UserGroup createUserGroup(String groupName) {
        CalendarUser adminCalendarUser = createUser();
        return createUserGroup(groupName, adminCalendarUser, List.of());
    }

    public UserGroup createUserGroup(String groupName, CalendarUser adminCalendarUser) {
        return createUserGroup(groupName, adminCalendarUser, List.of());
    }

    public UserGroup createUserGroup(String groupName, CalendarUser adminCalendarUser, List<CalendarUser> memberCalendarUsers) {
        UserGroup userGroup = UserGroup.initGroupWithAdminUsers(List.of(adminCalendarUser), groupName);
        userGroup.addCalendarUsersToGroup(memberCalendarUsers, MembershipRole.MEMBER);
        return userGroupRepository.save(userGroup);
    }
}
