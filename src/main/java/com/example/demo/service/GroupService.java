package com.example.demo.service;

import com.example.demo.dto.request.CreateGroupDTO;
import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.dto.response.GroupCalendarUserDTO;
import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.mapper.CreateGroupMapper;
import com.example.demo.mapper.GroupCalendarUserMapper;
import com.example.demo.mapper.UserGroupMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import com.example.demo.repository.UserGroupRepository;
import com.example.demo.security.UserSecurity;
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
@RequiredArgsConstructor
@Transactional
@Validated
public class GroupService {
    private final UserGroupRepository groupRepository;
    private final UserGroupMapper userGroupMapper;
    private final CalendarUserMapper calendarUserMapper;
    private final GroupCalendarUserMapper groupCalendarUserMapper;
    private final GroupMembershipRepository groupMembershipRepository;
    private final CalendarUserRepository calendarUserRepository;
    private final UserGroupRepository userGroupRepository;
    private final CreateGroupMapper createGroupMapper;

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public Page<UserGroupDTO> getAllGroupsForUserPageable(String username, Pageable pageable) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupRepository.findParticipatingGroupsExcludingInvitations(calendarUser.getId(), pageable)
                .map(userGroupMapper::toDTO);
    }

    @PreAuthorize("@groupSecurity.belongsToGroup(#groupId, authentication)")
    public List<GroupCalendarUserDTO> getAllUsersForGroupExcludingInvited(UUID groupId) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        return groupMembershipRepository.findAllByGroupAndMembershipRoleNot(userGroup, MembershipRole.INVITED).stream()
                .map(groupCalendarUserMapper::toDTO)
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
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow();
        deleteUserFromGroup(calendarUser, userGroup);
    }

    @PreAuthorize("@groupSecurity.canManageGroupMembers(#groupId, authentication)")
    public void deleteUserFromGroup(UUID userId, UUID groupId) {
        CalendarUser calendarUser = calendarUserRepository.findById(userId).orElseThrow();
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        deleteUserFromGroup(calendarUser, userGroup);
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public void leaveGroup(String username, UUID groupId) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        deleteUserFromGroup(calendarUser, userGroup);
    }

    private void deleteUserFromGroup(CalendarUser calendarUser, UserGroup userGroup) {
        GroupMembership groupMembership = groupMembershipRepository.findByGroupAndUser(userGroup, calendarUser).orElseThrow();
        MembershipRole membershipRole = groupMembership.getMembershipRole();
        groupMembershipRepository.delete(groupMembership);
        if (membershipRole == MembershipRole.ADMIN) {
            // removing ADMIN user
            List<GroupMembership> groupMembershipList = groupMembershipRepository.findAllByGroupAndUserNotAndMembershipRoleNot(userGroup, calendarUser, MembershipRole.INVITED);
            if (groupMembershipList.isEmpty()) {
                // remove group if the last user being ADMIN left
                groupRepository.delete(userGroup);
            } else {
                // pick new ADMIN from remaining users
                GroupMembership newAdminMembership = groupMembershipList.getFirst();
                newAdminMembership.setMembershipRole(MembershipRole.ADMIN);
                groupMembershipRepository.save(newAdminMembership);
            }
        }
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

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public UserGroupDTO createNewGroup(@Valid CreateGroupDTO createGroupDTO, String username) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        UserGroup userGroup = createGroupMapper.toEntity(createGroupDTO);
        userGroup.addCalendarUsersToGroup(List.of(calendarUser), MembershipRole.ADMIN);
        userGroupRepository.save(userGroup);
        return userGroupMapper.toDTO(userGroup);
    }

    public UserGroupDTO getUserGroup(UUID groupId) {
        return userGroupRepository.findById(groupId)
                .map(userGroupMapper::toDTO)
                .orElseThrow();
    }
}
