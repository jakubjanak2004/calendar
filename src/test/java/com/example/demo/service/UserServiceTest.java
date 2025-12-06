package com.example.demo.service;

import com.example.demo.SystemTest;
import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.request.UpdateUserDTO;
import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.mapper.ColorMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Color;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.service.utils.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UserServiceTest extends SystemTest {
    private final CalendarUserRepository userRepository;
    private final UserService userService;
    private final ColorMapper colorMapper;

    @Autowired
    public UserServiceTest(Generator generator, CalendarUserRepository userRepository, UserService userService, CalendarUserRepository calendarUserRepository, UserGroupRepository userGroupRepository, ColorMapper colorMapper) {
        super(calendarUserRepository, userGroupRepository, generator);
        this.userRepository = userRepository;
        this.userService = userService;
        this.colorMapper = colorMapper;
    }

    @Test
    public void findAllFindsAllPageableUsersPageable() {
        userRepository.saveAll(generator.createUsers("username", "password", 5));
        Page<CalendarUserDTO> calendarUserDTOPage = userService.findAllPageable(PageRequest.of(0, 10));
        // there are 5 generated users plus the calendarUser
        Assertions.assertEquals(6, calendarUserDTOPage.getTotalElements());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void hasAnyInvitationsThrowsAuthorizationDeniedExceptionWhenUsernameDoesntBelongToUser() {
        CalendarUser testUser = generator.createUser();
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> userService.hasAnyInvitations(testUser.getUsername()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void hasAnyInvitationsThrowsAuthorizationDeniedExceptionExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> userService.hasAnyInvitations("Non Existent Username"));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void hasAnyInvitationsReturnsTrueIfInvitationIsPresent() {
        CalendarUser testUser = generator.createUser();
        UserGroup userGroup = generator.createUserGroup("testGroup", testUser);
        userGroup.inviteUser(calendarUser);
        userGroupRepository.save(userGroup);
        Assertions.assertTrue(userService.hasAnyInvitations(calendarUser.getUsername()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void hasAnyInvitationsReturnsFalseIfInvitationIsNotPresent() {
        CalendarUser testUser = generator.createUser();
        UserGroup userGroup = generator.createUserGroup("testGroup", testUser);
        userGroupRepository.save(userGroup);
        Assertions.assertFalse(userService.hasAnyInvitations(calendarUser.getUsername()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateUserThrowsAuthorizationDeniedExceptionWhenUsernameDoesntBelongToUser() {
        CalendarUser testUser = generator.createUser();
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(
                "Updated first name",
                "Updated last name",
                ColorDTO.RED
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> userService.updateUser(testUser.getUsername(), updateUserDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateUserThrowsAuthenticationDeniedExceptionWhenUserDoesNotExist() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(
                "Updated first name",
                "Updated last name",
                ColorDTO.RED
        );
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> userService.updateUser("non existing username", updateUserDTO));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateUserUpdatesUser() {
        String newFirstName = "Updated first name";
        String newLastName = "Updated last name";
        ColorDTO newColor = ColorDTO.RED;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(
                newFirstName,
                newLastName,
                newColor
        );
        userService.updateUser(calendarUser.getUsername(), updateUserDTO);
        CalendarUser updatedUser = calendarUserRepository.findByUsername(calendarUser.getUsername()).orElseThrow();
        Assertions.assertEquals(calendarUser.getId(), updatedUser.getId());
        Assertions.assertEquals(newFirstName, updatedUser.getFirstName());
        Assertions.assertEquals(newLastName, updatedUser.getLastName());
        Assertions.assertEquals(newColor.getColor(), updatedUser.getColor().getColor());
    }
}
