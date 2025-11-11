package com.example.demo.service;

import com.example.demo.SystemTest;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.security.UserSecurity;
import com.example.demo.service.utils.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.NoSuchElementException;

@WithMockUser
@Import({GroupSystemTest.MethodSec.class, UserSecurity.class})
public class GroupSystemTest extends SystemTest {
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
    private GroupMembershipRepository groupMembershipRepository;

    @Autowired
    public GroupSystemTest(Generator generator, CalendarUserRepository calendarUserRepository) {
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
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.getAllGroupsForUserPageable(nextCalendarUser.getUsername(), PageRequest.of(0, 10)));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllGroupsForUserPageableReturnsAllGroupsForUserPageable() {
        Assertions.assertEquals(1, groupService.getAllGroupsForUserPageable(calendarUser.getUsername(), PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllUsersForGroupExcludingInvitedThrowsAuthorizationDeniedExceptionWhenUserDoesNotBelongToGroup() {
        UserGroup testUserGroup = userGroupRepository.save(new UserGroup(List.of(), "testGroup"));
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.getAllUsersForGroupExcludingInvited(testUserGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllUsersForGroupReturnsAllUsersForGroupExcludingInvited() {
        CalendarUser invitedUser = calendarUserRepository.save(generator.createUser());
        userGroup.inviteUser(invitedUser);
        userGroupRepository.save(userGroup);
        Assertions.assertEquals(1, groupService.getAllUsersForGroupExcludingInvited(userGroup.getId()).size());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getMembershipRoleForUserAndGroupThrowsAuthenticationDeniedExceptionWhenUserDoesNotBelongToGroup() {
        UserGroup testUserGroup = userGroupRepository.save(new UserGroup(List.of(), "testGroup"));
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.getMembershipRoleForUserAndGroup(EVENT_OWNER_USERNAME, testUserGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getMembershipRoleForUserAndGroupReturnsMembershipRole() {
        Assertions.assertEquals(MembershipRole.ADMIN, groupService.getMembershipRoleForUserAndGroup(EVENT_OWNER_USERNAME, userGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteUserFromGroupThrowsAuthorizationDeniedExceptionWhenUserDoesNotBelongToGroup_Username() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        UserGroup testUserGroup = userGroupRepository.save(new UserGroup(List.of(testUser), "testGroup"));
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.deleteUserFromGroup(testUser.getUsername(), testUserGroup.getId()));
    }

    @Test
    @WithMockUser(username = "editorUser")
    public void deleteUserFromGroupThrowsAuthorizationDeniedExceptionWhenUserIsNotAdmin_Username() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser("editorUser", "password"));
        userGroup.addCalendarUsersToGroup(List.of(testUser), MembershipRole.EDITOR);
        userGroupRepository.save(userGroup);
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.deleteUserFromGroup(testUser.getUsername(), userGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteUserFromGroupDeletesUserFromGroup_Username() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        userGroup.addCalendarUsersToGroup(List.of(testUser), MembershipRole.EDITOR);
        userGroupRepository.save(userGroup);
        groupService.deleteUserFromGroup(calendarUser.getUsername(), userGroup.getId());
        Assertions.assertEquals(1, groupService.getAllGroupsForUserPageable(calendarUser.getUsername(), PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteUserFromGroupThrowsAuthorizationDeniedExceptionWhenUserDoesNotBelongToGroup_UserId() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        UserGroup testUserGroup = userGroupRepository.save(new UserGroup(List.of(testUser), "testGroup"));
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.deleteUserFromGroup(testUser.getId(), testUserGroup.getId()));
    }

    @Test
    @WithMockUser(username = "editorUser")
    public void deleteUserFromGroupThrowsAuthorizationDeniedExceptionWhenUserIsNotAdmin_UserId() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser("editorUser", "password"));
        userGroup.addCalendarUsersToGroup(List.of(testUser), MembershipRole.EDITOR);
        userGroupRepository.save(userGroup);
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.deleteUserFromGroup(testUser.getId(), userGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void deleteUserFromGroupDeletesUserFromGroup_UserID() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        userGroup.addCalendarUsersToGroup(List.of(testUser), MembershipRole.EDITOR);
        userGroupRepository.save(userGroup);
        groupService.deleteUserFromGroup(calendarUser.getId(), userGroup.getId());
        Assertions.assertEquals(1, groupService.getAllGroupsForUserPageable(calendarUser.getUsername(), PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void inviteUserToGroupThrowsAuthorizationDeniedExceptionWhenUserDoesNotBelongToGroup() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        CalendarUser toBeInvited = calendarUserRepository.save(generator.createUser());
        UserGroup testUserGroup = userGroupRepository.save(new UserGroup(List.of(testUser), "testGroup"));
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.inviteUserToGroup(toBeInvited.getId(), testUserGroup.getId()));
    }

    @Test
    @WithMockUser(username = "editorUser")
    public void inviteUserToGroupThrowsAuthorizationDeniedExceptionWhenUserIsNotAdmin() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser("editorUser", "password"));
        userGroup.addCalendarUsersToGroup(List.of(testUser), MembershipRole.EDITOR);
        userGroupRepository.save(userGroup);
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.inviteUserToGroup(calendarUser.getId(), userGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void inviteUserToGroupAddUserAsInvited() {
        CalendarUser toBeInvited = calendarUserRepository.save(generator.createUser());
        groupService.inviteUserToGroup(toBeInvited.getId(), userGroup.getId());
        GroupMembership groupMembership = groupMembershipRepository.findByGroupAndUser(userGroup, toBeInvited).orElseThrow();
        Assertions.assertEquals(MembershipRole.INVITED, groupMembership.getMembershipRole());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getGroupInvitationsForUserThrowsAuthorizationDeniedExceptionWhenUserIsNotUser() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.getGroupInvitationsForUser(testUser.getUsername()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getGroupInvitationsForUserReturnsAllInvitationsForUser() {
        UserGroup testUserGroup1 = userGroupRepository.save(new UserGroup(List.of(), "testGroup1"));
        UserGroup testUserGroup2 = userGroupRepository.save(new UserGroup(List.of(), "testGroup2"));
        testUserGroup1.inviteUser(calendarUser);
        testUserGroup2.inviteUser(calendarUser);
        userGroupRepository.save(testUserGroup1);
        userGroupRepository.save(testUserGroup2);
        Assertions.assertEquals(2, groupService.getGroupInvitationsForUser(EVENT_OWNER_USERNAME).size());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void acceptInvitationForUserAndGroupThrowsAuthorizationDeniedExceptionWhenUserIsNotUser() {
        CalendarUser testUser = calendarUserRepository.save(generator.createUser());
        UserGroup testGroup = userGroupRepository.save(new UserGroup(List.of(), "testGroup"));
        testGroup.inviteUser(testUser);
        userGroupRepository.save(testGroup);
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupService.acceptInvitationForUserAndGroup(testUser.getUsername(), testGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void acceptInvitationForUserAndGroupThrowsNoSuchElementWhenInvitationNotPresent() {
        UserGroup testGroup = userGroupRepository.save(new UserGroup(List.of(), "testGroup"));
        Assertions.assertThrows(NoSuchElementException.class, () -> groupService.acceptInvitationForUserAndGroup(calendarUser.getUsername(), testGroup.getId()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void acceptInvitationForUserAndGroupAddsUserAsMEMBERToTheGroup() {
        UserGroup testGroup = userGroupRepository.save(new UserGroup(List.of(), "testGroup"));
        testGroup.inviteUser(calendarUser);
        userGroupRepository.save(testGroup);
        groupService.acceptInvitationForUserAndGroup(calendarUser.getUsername(), testGroup.getId());
        GroupMembership groupMembership = groupMembershipRepository.findByGroupAndUser(testGroup, calendarUser).orElseThrow();
        Assertions.assertEquals(MembershipRole.MEMBER, groupMembership.getMembershipRole());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSec {
    }
}
