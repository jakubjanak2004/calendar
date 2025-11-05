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
    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<GroupMembership> groupMembershipList = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    public UserGroup(List<CalendarUser> calendarUsers, String name) {
        this.name = name;
        addCalendarUsersToGroup(calendarUsers, MembershipType.EDITOR);
    }

    public void addCalendarUsersToGroup(List<CalendarUser> calendarUsers, MembershipType membershipType) {
        for (CalendarUser calendarUser : calendarUsers) {
            groupMembershipList.add(new GroupMembership(calendarUser, this, membershipType));
        }
    }
}
