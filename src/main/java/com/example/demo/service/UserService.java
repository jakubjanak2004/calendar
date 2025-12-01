package com.example.demo.service;

import com.example.demo.dto.request.UpdateUserDTO;
import com.example.demo.mapper.CalendarUserMapper;
import com.example.demo.mapper.UpdateUserMapper;
import com.example.demo.model.CalendarUser;
import com.example.demo.model.MembershipRole;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.example.demo.dto.response.CalendarUserDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Transactional
public class UserService {
    private final CalendarUserRepository calendarUserRepository;
    private final CalendarUserMapper calendarUserMapper;
    private final GroupMembershipRepository groupMembershipRepository;
    private final UpdateUserMapper updateUserMapper;

    public Page<CalendarUserDTO> findAllPageable(Pageable pageable) {
        return calendarUserRepository.findAll(pageable).map(calendarUserMapper::toDTO);
    }

    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public boolean hasAnyInvitations(String username) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupMembershipRepository.existsByUserIdAndMembershipRoleIn(calendarUser.getId(), List.of(MembershipRole.INVITED));
    }

    // todo add tests, determine if mapper is needed
    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public CalendarUserDTO updateUser(String username, UpdateUserDTO updateUserDTO) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        updateUserMapper.updateEntity(calendarUser, updateUserDTO);
        CalendarUser updatedUser = calendarUserRepository.save(calendarUser);
        return calendarUserMapper.toDTO(updatedUser);
    }
}
