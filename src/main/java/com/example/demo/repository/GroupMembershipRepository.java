package com.example.demo.repository;

import com.example.demo.model.GroupMembership;
import com.example.demo.model.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    boolean existsByGroupIdAndUserIdAndMembershipTypeIn(UUID groupId, UUID userId, List<MembershipType> types);
}
