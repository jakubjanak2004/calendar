package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserGroup extends EventOwner {
    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<GroupMembership> groupMembershipList = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    public UserGroup(String name) {
        this.name = name;
    }

    public UserGroup(List<CalendarUser> calendarUsers, String name) {
        this.name = name;
        addCalendarUsersToGroup(calendarUsers, MembershipRole.ADMIN);
    }

    public void addCalendarUsersToGroup(List<CalendarUser> calendarUsers, MembershipRole membershipRole) {
        for (CalendarUser calendarUser : calendarUsers) {
            groupMembershipList.add(new GroupMembership(calendarUser, this, membershipRole));
        }
    }

    public void inviteUser(CalendarUser calendarUser) {
        groupMembershipList.add(new GroupMembership(calendarUser, this, MembershipRole.INVITED));
    }
}
