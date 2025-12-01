package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "group_membership",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_group_membership_user_group",
                columnNames = {"user_id", "group_id"}
        ),
        indexes = {
                @Index(name = "idx_group_membership_user", columnList = "user_id"),
                @Index(name = "idx_group_membership_group", columnList = "group_id")
        }
)
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

    @Column(nullable = false)
    private Color color = new Color("#bada55");

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipRole membershipRole = MembershipRole.MEMBER;

    public GroupMembership(CalendarUser user, UserGroup group, MembershipRole membershipRole) {
        this.user = user;
        this.group = group;
        this.membershipRole = membershipRole;
    }
}
