package com.example.demo.service;

import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.mapper.UserGroupMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import com.example.demo.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final UserGroupRepository groupRepository;
    private final UserGroupMapper userGroupMapper;
    private final CalendarUserMapper calendarUserMapper;
    private final GroupMembershipRepository groupMembershipRepository;
    private final CalendarUserRepository calendarUserRepository;
    private final UserGroupRepository userGroupRepository;

    @PreAuthorize("@userSecurity.isUser(#calendarUserId, authentication)")
    public Page<UserGroupDTO> getAllGroupsForUserPageable(UUID calendarUserId, Pageable pageable) {
        return groupRepository.findParticipatingGroupsExcludingInvitations(calendarUserId, pageable)
                .map(userGroupMapper::toDTO);
    }

    // todo add tests, add preauthorize
    public List<CalendarUserDTO> getAllUsersForGroupExcludingInvited(UUID groupId) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        return groupMembershipRepository.findAllByGroupAndMembershipRoleNot(userGroup, MembershipRole.INVITED).stream()
                .map(GroupMembership::getUser)
                .map(calendarUserMapper::toDTO)
                .toList();
    }

    // todo add test, add preauthorize
    public List<CalendarUserDTO> getAllInvitedUsersForGroup(UUID groupId) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        return groupMembershipRepository.findAllByGroupAndMembershipRole(userGroup, MembershipRole.INVITED).stream()
                .map(GroupMembership::getUser)
                .map(calendarUserMapper::toDTO)
                .toList();
    }

    // todo add tests, add preauthorize
    public MembershipRole getMembershipRoleForUser(UUID groupId, CalendarUser userId) {
        GroupMembership groupMembership = groupMembershipRepository.findGroupMembershipByGroupAndUser(groupRepository.findById(groupId).orElseThrow(), userId).orElseThrow();
        return groupMembership.getMembershipRole();
    }

    // todo add tests, add pre authorize
    public void deleteUserFromGroup(UUID groupId, UUID userId) {
        groupMembershipRepository.deleteByGroupAndUser(groupRepository.findById(groupId).orElseThrow(), calendarUserRepository.findById(userId).orElseThrow());
    }

    // todo add tests, add pre authorize
    public void inviteUserToGroup(UUID groupId, UUID userId) {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow();
        CalendarUser calendarUser = calendarUserRepository.findById(userId).orElseThrow();
        userGroup.inviteUser(calendarUser);
        userGroupRepository.save(userGroup);
    }
}
