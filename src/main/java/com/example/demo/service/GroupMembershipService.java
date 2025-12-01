package com.example.demo.service;

import com.example.demo.dto.request.ChangeMembershipRoleDTO;
import com.example.demo.dto.response.GroupMembershipDTO;
import com.example.demo.mapper.GroupMembershipMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.Color;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import com.example.demo.repository.UserGroupRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
// todo add tests
public class GroupMembershipService {
    private final GroupMembershipRepository groupMembershipRepository;
    private final CalendarUserRepository calendarUserRepository;
    private final GroupMembershipMapper groupMembershipMapper;
    private final UserGroupRepository userGroupRepository;

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public List<GroupMembershipDTO> getAllMembershipsForUser(String username) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupMembershipRepository.findAllByUserAndMembershipRoleNot(calendarUser, MembershipRole.INVITED).stream()
                .map(groupMembershipMapper::toDTO)
                .toList();
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public Page<GroupMembershipDTO> getAllMembershipsForUserPageable(String username, Pageable pageable) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupMembershipRepository.findAllByUserAndMembershipRoleNot(calendarUser, MembershipRole.INVITED, pageable)
                .map(groupMembershipMapper::toDTO);
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public GroupMembershipDTO getMembershipForUserAndGroup(String username, UUID groupId) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        // todo think about fetching with condition not invited here
        return groupMembershipRepository.findByGroupAndUser(userGroup, calendarUser)
                .map(groupMembershipMapper::toDTO)
                .orElseThrow();
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public GroupMembershipDTO updateMembershipColorForGroupAndUser(@Valid Color color, UUID groupId, String username) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        // todo think about fetching with condition not invited here
        GroupMembership groupMembership = groupMembershipRepository.findByGroupAndUser(userGroup, calendarUser).orElseThrow();
        groupMembership.setColor(color);
        groupMembershipRepository.save(groupMembership);
        return groupMembershipMapper.toDTO(groupMembership);
    }


    @PreAuthorize("@groupSecurity.canManageGroupMembers(#groupId, authentication)")
    public void updateMembershipRoleForGroupAndUser(UUID groupId, UUID userId, ChangeMembershipRoleDTO changeMembershipRoleDTO) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        CalendarUser calendarUser = calendarUserRepository.findById(userId).orElseThrow();
        GroupMembership groupMembership = groupMembershipRepository.findByGroupAndUser(userGroup, calendarUser).orElseThrow();
        groupMembership.setMembershipRole(changeMembershipRoleDTO.getNewMembershipRole());
    }
}
