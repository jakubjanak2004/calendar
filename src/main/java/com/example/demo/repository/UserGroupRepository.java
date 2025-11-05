package com.example.demo.repository;

import com.example.demo.model.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
    @Query(
            value = """
            select distinct g
            from UserGroup g
            join g.groupMembershipList gm
            where gm.user.id = :userId
              and gm.membershipType <> MembershipType.INVITED
        """,
            countQuery = """
            select count(distinct g)
            from UserGroup g
            join g.groupMembershipList gm
            where gm.user.id = :userId
              and gm.membershipType <> MembershipType.INVITED
        """
    )
    Page<UserGroup> findParticipatingGroupsExcludingInvitations(
            @Param("userId") UUID userId,
            Pageable pageable
    );
}
