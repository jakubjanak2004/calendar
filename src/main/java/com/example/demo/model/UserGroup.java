package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserGroup extends EventOwner {
    @Size(min = 1, message = "Group must have at least one member")
    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<GroupMembership> groupMembershipList = new ArrayList<>();

    @NotBlank
    @Column(nullable = false)
    private String name;

    private UserGroup(List<CalendarUser> calendarUsers, String name, MembershipRole role) {
        this.name = name;
        addCalendarUsersToGroup(calendarUsers, role);
    }

    public static UserGroup initGroupWithInvitedUsers(List<CalendarUser> calendarUserList, String name) {
        return new UserGroup(calendarUserList, name, MembershipRole.INVITED);
    }

    public static UserGroup initGroupWithAdminUsers(List<CalendarUser> calendarUserList, String name) {
        return new UserGroup(calendarUserList, name, MembershipRole.ADMIN);
    }

    public void addCalendarUsersToGroup(List<CalendarUser> calendarUsers, MembershipRole membershipRole) {
        for (CalendarUser calendarUser : calendarUsers) {
            groupMembershipList.add(new GroupMembership(calendarUser, this, membershipRole));
        }
    }

    public void inviteUser(CalendarUser calendarUser) {
        groupMembershipList.add(new GroupMembership(calendarUser, this, MembershipRole.INVITED));
    }

    public void removeMembership(GroupMembership membership) {
        groupMembershipList.remove(membership);
        membership.setGroup(null);
    }

    /**
     * Check for one ADMIN presence, note when changing the UserGroupMembership we can violate this
     * constraint therefore that needs to get checked in the Service layer
     */
    @PrePersist
    @PreUpdate
    private void validateAdminPresence() {
        long adminCount = groupMembershipList.stream()
                .filter(m -> m.getMembershipRole() == MembershipRole.ADMIN)
                .count();

        if (adminCount != 1) {
            throw new IllegalStateException(
                    "Group must have exactly one admin, but found " + adminCount + "."
            );
        }
    }
}
