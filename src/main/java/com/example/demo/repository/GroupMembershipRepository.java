package com.example.demo.repository;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    boolean existsByGroupIdAndUserIdAndMembershipRoleIn(UUID groupId, UUID userId, List<MembershipRole> types);

    boolean existsByGroupIdAndUserIdAndMembershipRoleNotIn(UUID groupId, UUID userId, List<MembershipRole> types);

    boolean existsByUserIdAndMembershipRoleIn(UUID userId, List<MembershipRole> types);

    Optional<GroupMembership> findGroupMembershipByGroupAndUser(UserGroup group, CalendarUser user);

    List<GroupMembership> findAllByGroupAndMembershipRoleNot(UserGroup group, MembershipRole role);

    List<GroupMembership> findAllByGroupAndUserNotAndMembershipRoleNot(UserGroup group, CalendarUser exceptUser, MembershipRole role);

    List<GroupMembership> findAllByGroupAndMembershipRole(UserGroup userGroup, MembershipRole membershipRole);

    List<GroupMembership> findAllByUserAndMembershipRoleNot(CalendarUser user, MembershipRole role);

    long countByGroup_Id(UUID groupId);

    Page<GroupMembership> findAllByUserAndMembershipRoleNot(CalendarUser user, MembershipRole role, Pageable pageable);

    Collection<GroupMembership> findAllByUserAndMembershipRole(CalendarUser calendarUser, MembershipRole membershipRole);

    Optional<GroupMembership> findByGroupAndUser(UserGroup group, CalendarUser user);
}
