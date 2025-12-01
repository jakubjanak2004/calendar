package com.example.demo;

import com.example.demo.model.CalendarUser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public abstract class SystemTest {
    public static final String EVENT_OWNER_USERNAME = "test";
}
