package com.example.demo.service;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.security.UserSecurity;
import com.example.demo.service.utils.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@WithMockUser
@Import({GroupServiceTest.MethodSec.class, UserSecurity.class})
@Transactional
public class GroupServiceTest {
    private static final String EVENT_OWNER_USERNAME = "test";
    private final Generator generator;
    private final CalendarUserRepository calendarUserRepository;
    private UserGroup userGroup;
    private CalendarUser calendarUser;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private GroupService groupService;

    @Autowired
    public GroupServiceTest(Generator generator, CalendarUserRepository calendarUserRepository) {
        this.generator = generator;
        this.calendarUserRepository = calendarUserRepository;
    }

    @BeforeEach
    void initData() {
        if (calendarUser != null) {
            calendarUserRepository.delete(calendarUser);
            userGroupRepository.delete(userGroup);
        }
        calendarUser = calendarUserRepository.save(generator.createUser(EVENT_OWNER_USERNAME, "testPassword"));
        userGroup = userGroupRepository.save(new UserGroup(List.of(calendarUser), "testGroup"));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllGroupsForUserPageableThrowsAuthorizationDeniedExceptionWhenUserAccessingIsNotUser() {
        CalendarUser nextCalendarUser = calendarUserRepository.save(generator.createUser("next user", "testPassword"));
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.getAllGroupsForUserPageable(nextCalendarUser.getId(), PageRequest.of(0, 10)));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllGroupsForUserPageableReturnsAllGroupsForUserPageable() {
        Assertions.assertEquals(1, groupService.getAllGroupsForUserPageable(calendarUser.getId(), PageRequest.of(0, 10)).getTotalElements());
    }


    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSec {
    }
}
