package com.example.demo.controller;

import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.request.ChangeMembershipRoleDTO;
import com.example.demo.dto.response.GroupMembershipDTO;
import com.example.demo.service.GroupMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groupMemberships")
@RequiredArgsConstructor
public class GroupMembershipController {
    private final GroupMembershipService groupMembershipService;

    @GetMapping(value = "/me", params = {"!page", "!size"})
    public ResponseEntity<List<GroupMembershipDTO>> getAllGroupMembershipsForMe(Principal principal) {
        return ResponseEntity.ok(groupMembershipService.getAllMembershipsForUser(principal.getName()));
    }

    @GetMapping(value = "/me", params = {"page", "size"})
    public ResponseEntity<Page<GroupMembershipDTO>> getAllGroupMembershipsForMePageable(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(groupMembershipService.getAllMembershipsForUserPageable(principal.getName(), pageable));
    }

    @GetMapping("/{groupId}/me")
    public ResponseEntity<GroupMembershipDTO> getAllGroupMembershipsForMe(@PathVariable UUID groupId, Principal principal) {
        return ResponseEntity.ok(groupMembershipService.getMembershipForUserAndGroup(principal.getName(), groupId));
    }

    @PutMapping("/{groupId}/me/color")
    public ResponseEntity<GroupMembershipDTO> updateGroupMembershipColor(@PathVariable UUID groupId, @RequestBody ColorDTO colorDTO, Principal principal) {
        return ResponseEntity.ok(groupMembershipService.updateMembershipColorForGroupAndUser(colorDTO, groupId, principal.getName()));
    }

    @PutMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> updateGroupMemberStatus(
            @PathVariable UUID groupId,
            @PathVariable UUID userId,
            @RequestBody ChangeMembershipRoleDTO membershipRoleRequestDTO
    ) {
        groupMembershipService.updateMembershipRoleForGroupAndUser(groupId, userId, membershipRoleRequestDTO);
        return ResponseEntity.noContent().build();
    }
}
