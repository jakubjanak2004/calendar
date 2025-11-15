package com.example.demo.mapper;

import com.example.demo.dto.request.CreateGroupDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.CalendarUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateGroupMapper implements Mapper<UserGroup, CreateGroupDTO> {
    private final CalendarUserRepository calendarUserRepository;

    @Override
    public UserGroup toEntity(CreateGroupDTO createGroupDTO) {
        List<CalendarUser> usersList = calendarUserRepository.findAllById(createGroupDTO.getUsersIdsList());
        return UserGroup.initGroupWithInvitedUsers(
                usersList,
                createGroupDTO.getName()
        );
    }

    @Override
    public CreateGroupDTO toDTO(UserGroup userGroup) {
        return null;
    }

    @Override
    public UserGroup updateEntity(UserGroup userGroup, CreateGroupDTO createGroupDTO) {
        return null;
    }
}
