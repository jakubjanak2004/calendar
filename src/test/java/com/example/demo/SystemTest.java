package com.example.demo;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.security.EventSecurity;
import com.example.demo.service.EventServiceTest;
import com.example.demo.service.utils.Generator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@WithMockUser
@Import({EventServiceTest.MethodSec.class, EventSecurity.class})
public abstract class SystemTest {
    protected static final String EVENT_OWNER_USERNAME = "test_username";
    protected static final String EVENT_OWNER_PASSWORD = "test_password";
    protected final CalendarUserRepository calendarUserRepository;
    protected final UserGroupRepository userGroupRepository;
    protected final Generator generator;
    protected CalendarUser calendarUser;
    protected UserGroup userGroup;

    public SystemTest(CalendarUserRepository calendarUserRepository, UserGroupRepository userGroupRepository, Generator generator) {
        this.calendarUserRepository = calendarUserRepository;
        this.userGroupRepository = userGroupRepository;
        this.generator = generator;
    }

    @BeforeEach
    void initData() {
        if (calendarUser != null) {
            calendarUserRepository.delete(calendarUser);
            userGroupRepository.delete(userGroup);
        }
        calendarUser = calendarUserRepository.save(generator.createUser(EVENT_OWNER_USERNAME, EVENT_OWNER_PASSWORD));
        userGroup = userGroupRepository.save(UserGroup.initGroupWithAdminUsers(List.of(calendarUser), "testUserGroup"));
    }

    @TestConfiguration
    @EnableMethodSecurity
    protected static class MethodSec {
    }
}
