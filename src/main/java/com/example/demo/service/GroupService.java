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

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public Page<UserGroupDTO> getAllGroupsForUserPageable(String username, Pageable pageable) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupRepository.findParticipatingGroupsExcludingInvitations(calendarUser.getId(), pageable)
                .map(userGroupMapper::toDTO);
    }

    @PreAuthorize("@groupSecurity.belongsToGroup(#groupId, authentication)")
    public List<CalendarUserDTO> getAllUsersForGroupExcludingInvited(UUID groupId) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        return groupMembershipRepository.findAllByGroupAndMembershipRoleNot(userGroup, MembershipRole.INVITED).stream()
                .map(GroupMembership::getUser)
                .map(calendarUserMapper::toDTO)
                .toList();
    }

    @PreAuthorize("@groupSecurity.belongsToGroup(#groupId, authentication)")
    public List<CalendarUserDTO> getAllInvitedUsersForGroup(UUID groupId) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        return groupMembershipRepository.findAllByGroupAndMembershipRole(userGroup, MembershipRole.INVITED).stream()
                .map(GroupMembership::getUser)
                .map(calendarUserMapper::toDTO)
                .toList();
    }

    @PreAuthorize("@groupSecurity.belongsToGroup(#groupId, authentication)")
    public MembershipRole getMembershipRoleForUserAndGroup(String username, UUID groupId) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        GroupMembership groupMembership = groupMembershipRepository.findGroupMembershipByGroupAndUser(groupRepository.findById(groupId).orElseThrow(), calendarUser).orElseThrow();
        return groupMembership.getMembershipRole();
    }

    @PreAuthorize("@groupSecurity.canManageGroupMembers(#groupId, authentication)")
    public void deleteUserFromGroup(String username, UUID groupId) {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow();
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        deleteUserFromGroup(calendarUser, userGroup);
    }

    @PreAuthorize("@groupSecurity.canManageGroupMembers(#groupId, authentication)")
    public void deleteUserFromGroup(UUID userId, UUID groupId) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        CalendarUser calendarUser = calendarUserRepository.findById(userId).orElseThrow();
        deleteUserFromGroup(calendarUser, userGroup);
    }

    private void deleteUserFromGroup(CalendarUser calendarUser, UserGroup userGroup) {
        groupMembershipRepository.deleteByGroupAndUser(userGroup, calendarUser);
    }

    @PreAuthorize("@groupSecurity.canManageGroupMembers(#groupId, authentication)")
    public void inviteUserToGroup(UUID userId, UUID groupId) {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow();
        CalendarUser calendarUser = calendarUserRepository.findById(userId).orElseThrow();
        userGroup.inviteUser(calendarUser);
        userGroupRepository.save(userGroup);
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public List<UserGroupDTO> getGroupInvitationsForUser(String username) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupMembershipRepository.findAllByUserAndMembershipRole(calendarUser, MembershipRole.INVITED).stream()
                .map(GroupMembership::getGroup)
                .map(userGroupMapper::toDTO)
                .toList();
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public void acceptInvitationForUserAndGroup(String username, UUID groupId) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow();
        GroupMembership groupMembership = groupMembershipRepository.findByGroupAndUser(userGroup, calendarUser).orElseThrow();
        groupMembership.setMembershipRole(MembershipRole.MEMBER);
        groupMembershipRepository.save(groupMembership);
    }
}
