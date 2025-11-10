package com.example.demo.controller;

import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.MembershipRole;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    // todo remove repositories from controller
    private final CalendarUserRepository calendarUserRepository;
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<Page<UserGroupDTO>> getGroupsForUser(Pageable pageable, Principal principal) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(groupService.getAllGroupsForUserPageable(calendarUser.getId(), pageable));
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<CalendarUserDTO>> getAllUsersForGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getAllUsersForGroupExcludingInvited(groupId));
    }

    @GetMapping("/{groupId}/users/invited")
    public ResponseEntity<List<CalendarUserDTO>> getAllInvitedUsersForGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getAllInvitedUsersForGroup(groupId));
    }

    @GetMapping("/{groupId}/users/me/membershipRole")
    public ResponseEntity<MembershipRole> getUserMembershipRole(@PathVariable UUID groupId, Principal principal) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(groupService.getMembershipRoleForUser(groupId, calendarUser));
    }

    @DeleteMapping("/{groupId}/users/me")
    public ResponseEntity<Void> leaveGroup(@PathVariable UUID groupId, Principal principal) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(principal.getName()).orElseThrow();
        groupService.deleteUserFromGroup(groupId, calendarUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> inviteUser(@PathVariable UUID groupId, @PathVariable UUID userId) {
        groupService.inviteUserToGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID groupId, @PathVariable UUID userId) {
        groupService.deleteUserFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }
}
