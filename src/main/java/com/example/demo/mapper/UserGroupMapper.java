package com.example.demo.mapper;

import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.model.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class UserGroupMapper implements Mapper<UserGroup, UserGroupDTO>{
    @Override
    public UserGroup toEntity(UserGroupDTO userGroupDTO) {
        return null;
    }

    @Override
    public UserGroupDTO toDTO(UserGroup userGroup) {
        return new UserGroupDTO(
                userGroup.getId(),
                userGroup.getName()
        );
    }

    @Override
    public UserGroup updateEntity(UserGroup userGroup, UserGroupDTO userGroupDTO) {
        return null;
    }
}
