package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GroupMembership {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn
    private CalendarUser user;

    @ManyToOne(optional = false)
    @JoinColumn
    private UserGroup group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipRole membershipRole = MembershipRole.MEMBER;

    public GroupMembership(CalendarUser user, UserGroup group, MembershipRole membershipRole) {
        this.user = user;
        this.group = group;
        this.membershipRole = membershipRole;
    }
}
