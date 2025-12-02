package com.example.demo.service;

import com.example.demo.SystemTest;
import com.example.demo.dto.request.ChangeMembershipRoleDTO;
import com.example.demo.dto.response.GroupMembershipDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Color;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
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

public class GroupMembershipTest extends SystemTest {
    private final GroupMembershipService groupMembershipService;
    private final GroupMembershipRepository groupMembershipRepository;


    @Autowired
    public GroupMembershipTest(CalendarUserRepository calendarUserRepository,
                               Generator generator,
                               GroupMembershipService groupMembershipService,
                               UserGroupRepository userGroupRepository,
                               GroupMembershipRepository groupMembershipRepository) {
        super(calendarUserRepository, userGroupRepository, generator);
        this.groupMembershipService = groupMembershipService;
        this.groupMembershipRepository = groupMembershipRepository;
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllMembershipsForUserThrowsAuthorizationDeniedExceptionWhenUsernameDoesNotBelongToUser() {
        CalendarUser testUser = generator.createUser();
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupMembershipService.getAllMembershipsForUser(testUser.getUsername()));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllMembershipsForUserThrowsAuthorizationDeniedExceptionExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> groupMembershipService.getAllMembershipsForUser("non existing username"));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllMembershipsForUserGetsAllMembershipsForUser() {
        CalendarUser testUser = generator.createUser();
        UserGroup userGroup2 = generator.createUserGroup("group2", calendarUser);
        UserGroup userGroup3 = generator.createUserGroup("group3", testUser);
        // adding invite, that shouldn't be returned by get memberships
        userGroup3.inviteUser(calendarUser);
        userGroupRepository.saveAll(List.of(userGroup2, userGroup3));
        Assertions.assertEquals(2, groupMembershipService.getAllMembershipsForUser(calendarUser.getUsername()).size());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllMembershipsForUserPageableThrowsAuthorizationDeniedExceptionWhenUsernameDoesNotBelongToUser() {
        CalendarUser testUser = generator.createUser();
        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.getAllMembershipsForUserPageable(
                        testUser.getUsername(),
                        PageRequest.of(0, 10)
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllMembershipsForUserPageableThrowsAuthorizationDeniedExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.getAllMembershipsForUserPageable(
                        "non existing username",
                        PageRequest.of(0, 10)
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getAllMembershipsForUserPageableGetsPageOfMembershipsForUser() {
        CalendarUser testUser = generator.createUser();
        UserGroup userGroup2 = generator.createUserGroup("group2", calendarUser);
        UserGroup userGroup3 = generator.createUserGroup("group3", testUser);
        // invite current user to group3 – membership with INVITED must not be returned
        userGroup3.inviteUser(calendarUser);
        userGroupRepository.saveAll(List.of(userGroup2, userGroup3));

        Page<GroupMembershipDTO> page = groupMembershipService.getAllMembershipsForUserPageable(
                calendarUser.getUsername(),
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(2, page.getTotalElements());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getMembershipForUserAndGroupThrowsAuthorizationDeniedExceptionWhenUsernameDoesNotBelongToUser() {
        CalendarUser otherUser = generator.createUser();
        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.getMembershipForUserAndGroup(
                        otherUser.getUsername(),
                        userGroup.getId()
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getMembershipForUserAndGroupThrowsAuthorizationDeniedExceptionExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.getMembershipForUserAndGroup(
                        "non existing username",
                        userGroup.getId()
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void getMembershipForUserAndGroupReturnsMembershipForUserAndGroup() {
        GroupMembershipDTO dto = groupMembershipService.getMembershipForUserAndGroup(
                calendarUser.getUsername(),
                userGroup.getId()
        );

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(userGroup.getId(), dto.getId());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipColorForGroupAndUserThrowsAuthorizationDeniedExceptionWhenUsernameDoesNotBelongToUser() {
        CalendarUser otherUser = generator.createUser();
        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.updateMembershipColorForGroupAndUser(
                        Color.RED,
                        userGroup.getId(),
                        otherUser.getUsername()
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipColorForGroupAndUserThrowsAuthorizationDeniedExceptionExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.updateMembershipColorForGroupAndUser(
                        Color.RED,
                        userGroup.getId(),
                        "non existing username"
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipColorForGroupAndUserUpdatesColor() {
        groupMembershipService.updateMembershipColorForGroupAndUser(
                Color.RED,
                userGroup.getId(),
                calendarUser.getUsername()
        );

        GroupMembership membership = groupMembershipRepository
                .findByGroupAndUser(userGroup, calendarUser)
                .orElseThrow();

        Assertions.assertEquals(Color.RED, membership.getColor());
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipRoleForGroupAndUserThrowsAuthorizationDeniedExceptionWhenUserCannotManageGroup() {
        CalendarUser testUser = generator.createUser();
        UserGroup testGroup = generator.createUserGroup("group2", testUser, List.of(calendarUser));
        ChangeMembershipRoleDTO dto = new ChangeMembershipRoleDTO(MembershipRole.EDITOR);

        Assertions.assertThrows(AuthorizationDeniedException.class,
                () -> groupMembershipService.updateMembershipRoleForGroupAndUser(
                        testGroup.getId(),
                        calendarUser.getId(),
                        dto
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipRoleForGroupAndUserThrowsNoSuchElementExceptionWhenUserDoesNotExist() {
        ChangeMembershipRoleDTO dto = new ChangeMembershipRoleDTO(MembershipRole.MEMBER);

        Assertions.assertThrows(NoSuchElementException.class,
                () -> groupMembershipService.updateMembershipRoleForGroupAndUser(
                        userGroup.getId(),
                        UUID.randomUUID(),
                        dto
                ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipRoleForGroupAndUserThrowsIllegalStateExceptionWhenThereIsNoOtherUserToGetSelectedAsAdmin() {
        ChangeMembershipRoleDTO dto = new ChangeMembershipRoleDTO(MembershipRole.MEMBER);

        Assertions.assertThrows(IllegalStateException.class, () ->  groupMembershipService.updateMembershipRoleForGroupAndUser(
                userGroup.getId(),
                calendarUser.getId(),
                dto
        ));
    }

    @Test
    @WithMockUser(username = EVENT_OWNER_USERNAME)
    public void updateMembershipRoleForGroupAndUserUpdatesRoleWhenNewAdminIsAssignedDropsTheOldAdminsRoleToMember() {
        CalendarUser testUser = generator.createUser();
        userGroup.addCalendarUsersToGroup(List.of(testUser), MembershipRole.MEMBER);
        userGroupRepository.save(userGroup);
        ChangeMembershipRoleDTO dto = new ChangeMembershipRoleDTO(MembershipRole.ADMIN);

        groupMembershipService.updateMembershipRoleForGroupAndUser(
                userGroup.getId(),
                testUser.getId(),
                dto
        );

        GroupMembership calendarUserMembership = groupMembershipRepository
                .findByGroupAndUser(userGroup, calendarUser)
                .orElseThrow();

        GroupMembership testUserMembership = groupMembershipRepository
                .findByGroupAndUser(userGroup, testUser)
                        .orElseThrow();

        Assertions.assertEquals(MembershipRole.MEMBER, calendarUserMembership.getMembershipRole());
        Assertions.assertEquals(MembershipRole.ADMIN, testUserMembership.getMembershipRole());
    }
}
