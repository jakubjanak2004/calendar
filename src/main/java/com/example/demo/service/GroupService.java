package com.example.demo.service;

import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.mapper.UserGroupMapper;
import com.example.demo.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserGroupRepository groupRepository;
    private final UserGroupMapper userGroupMapper;

    public Page<UserGroupDTO> getAllGroupsForUserPageable(UUID calendarUserId, Pageable pageable) {
        return groupRepository.findParticipatingGroupsExcludingInvitations(calendarUserId, pageable)
                .map(userGroupMapper::toDTO);
    }
}
