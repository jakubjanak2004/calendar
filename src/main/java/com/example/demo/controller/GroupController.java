package com.example.demo.controller;

import com.example.demo.dto.request.CreateGroupDTO;
import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.dto.response.GroupCalendarUserDTO;
import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.model.MembershipRole;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<Page<UserGroupDTO>> getGroupsForUser(Pageable pageable, Principal principal) {
        return ResponseEntity.ok(groupService.getAllGroupsForUserPageable(principal.getName(), pageable));
    }

    @PostMapping
    public ResponseEntity<UserGroupDTO> createGroup(@RequestBody CreateGroupDTO createGroupDTO, Principal principal) {
        return ResponseEntity.ok(groupService.createNewGroup(createGroupDTO, principal.getName()));
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<UserGroupDTO>> getGroupInvitationsForUser(Principal principal) {
        return ResponseEntity.ok(groupService.getGroupInvitationsForUser(principal.getName()));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<UserGroupDTO> getUserGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getUserGroup(groupId));
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<GroupCalendarUserDTO>> getAllUsersForGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getAllUsersForGroupExcludingInvited(groupId));
    }

    @GetMapping("/{groupId}/users/invited")
    public ResponseEntity<List<CalendarUserDTO>> getAllInvitedUsersForGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getAllInvitedUsersForGroup(groupId));
    }

    @GetMapping("/{groupId}/users/me/membershipRole")
    public ResponseEntity<MembershipRole> getUserMembershipRole(@PathVariable UUID groupId, Principal principal) {
        return ResponseEntity.ok(groupService.getMembershipRoleForUserAndGroup(principal.getName(), groupId));
    }

    @DeleteMapping("/{groupId}/users/me")
    public ResponseEntity<Void> leaveGroup(@PathVariable UUID groupId, Principal principal) {
        groupService.leaveGroup(principal.getName(), groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/users/me")
    public ResponseEntity<Void> acceptInviteToGroup(@PathVariable UUID groupId, Principal principal) {
        groupService.acceptInvitationForUserAndGroup(principal.getName(), groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> inviteUser(@PathVariable UUID groupId, @PathVariable UUID userId) {
        groupService.inviteUserToGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID groupId, @PathVariable UUID userId) {
        groupService.deleteUserFromGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
}
