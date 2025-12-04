package com.example.demo.mapper;

import com.example.demo.dto.response.GroupMembershipDTO;
import com.example.demo.model.GroupMembership;
import org.springframework.stereotype.Component;

@Component
public class GroupMembershipMapper implements Mapper<GroupMembership, GroupMembershipDTO> {
    @Override
    public GroupMembership toEntity(GroupMembershipDTO groupMembershipDTO) {
        return null;
    }

    @Override
    public GroupMembershipDTO toDTO(GroupMembership groupMembership) {
        return new GroupMembershipDTO(
                groupMembership.getGroup().getId(),
                groupMembership.getGroup().getName(),
                groupMembership.getMembershipRole(),
                groupMembership.getColor().toString()
        );
    }

    @Override
    public GroupMembership updateEntity(GroupMembership groupMembership, GroupMembershipDTO groupMembershipDTO) {
        return null;
    }
}
