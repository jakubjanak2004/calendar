package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserGroup extends EventOwner {
    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<GroupMembership> groupMembershipList = new ArrayList<>();

    @Column(nullable = false)
    private String name;
}
