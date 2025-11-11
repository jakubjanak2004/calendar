package com.example.demo.security;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import com.example.demo.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component("groupSecurity")
@RequiredArgsConstructor
public class GroupSecurity {
    private final GroupMembershipRepository groupMembershipRepository;
    private final UserGroupRepository userGroupRepository;
    private final CalendarUserRepository calendarUserRepository;

    public boolean belongsToGroup(UUID groupId, Authentication auth) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(auth.getName()).orElseThrow();
        return groupMembershipRepository.existsByGroupIdAndUserIdAndMembershipRoleNotIn(groupId, calendarUser.getId(), List.of(MembershipRole.INVITED));
    }

    public boolean canManageGroupMembers(UUID groupId, Authentication auth) {
        UserGroup userGroup = userGroupRepository.findById(groupId).orElseThrow();
        return canManageGroupMembers(userGroup, auth);
    }

    public boolean canManageGroupMembers(UserGroup group, Authentication auth) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(auth.getName()).orElseThrow();
        MembershipRole membershipRole = groupMembershipRepository.findByGroupAndUser(group, calendarUser)
                .map(GroupMembership::getMembershipRole)
                .orElseThrow(() -> new AuthorizationDeniedException("User does not belong to the userGroup."));
        return membershipRole == MembershipRole.ADMIN;
    }
}
