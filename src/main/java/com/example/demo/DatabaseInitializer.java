package com.example.demo;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.service.utils.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final CalendarUserRepository calendarUserRepository;
    private final EventRepository eventRepository;
    private final Generator generator;
    private final UserGroupRepository userGroupRepository;

    @Override
    public void run(String... args) {
        // create admin user
        CalendarUser calendarUser = generator.createUser("admin", "admin");
        List<CalendarUser> calendarUsers = generator.createUsers("user", "admin", 5);
        calendarUserRepository.save(calendarUser);
        calendarUserRepository.saveAll(calendarUsers);
        // create events for admin user
        List<Event> events = generator.createEvents(
                calendarUser,
                List.of(2, 3, 4, 5),
                List.of(45, 60, 120, 80)
        );
        eventRepository.saveAll(events);
        // create groups for users
        UserGroup userGroup = new UserGroup(List.of(calendarUser), "first group");
        userGroupRepository.save(userGroup);
        // add group events
        List<Event> eventsForUserGroup = generator.createEvents(
                userGroup,
                List.of(1, 2, 3, 4),
                List.of(30, 120, 45, 20)
        );
        eventRepository.saveAll(eventsForUserGroup);
    }
}
