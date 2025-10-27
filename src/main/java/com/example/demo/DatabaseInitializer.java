package com.example.demo;

import com.example.demo.dto.request.SignUpDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.utils.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final Generator generator;

    @Override
    public void run(String... args) {
        // create admin user
        CalendarUser calendarUser = generator.createUser("admin", "admin");
        userRepository.save(calendarUser);
        // create events for admin user
        List<Event> events = generator.createEvents(
                calendarUser,
                List.of(2, 3, 4, 5),
                List.of(45, 60, 120, 80)
        );
        eventRepository.saveAll(events);
    }
}
