package com.example.demo.mapper;

import com.example.demo.dto.response.GroupCalendarUserDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.GroupMembership;
import org.springframework.stereotype.Component;

@Component
public class GroupCalendarUserMapper implements Mapper<GroupMembership, GroupCalendarUserDTO> {

    @Override
    public GroupMembership toEntity(GroupCalendarUserDTO groupCalendarUserDTO) {
        return null;
    }

    @Override
    public GroupCalendarUserDTO toDTO(GroupMembership groupMembership) {
        CalendarUser calendarUser = groupMembership.getUser();
        return new GroupCalendarUserDTO(
                calendarUser.getId(),
                calendarUser.getUsername(),
                calendarUser.getFirstName(),
                calendarUser.getLastName(),
                groupMembership.getMembershipRole()
        );
    }

    @Override
    public GroupMembership updateEntity(GroupMembership groupMembership, GroupCalendarUserDTO groupCalendarUserDTO) {
        return null;
    }
}
