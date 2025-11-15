package com.example.demo.service;

import com.example.demo.mapper.CalendarUserMapper;
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

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final CalendarUserRepository calendarUserRepository;
    private final CalendarUserMapper calendarUserMapper;
    private final GroupMembershipRepository groupMembershipRepository;

    public Page<CalendarUserDTO> findAllPageable(Pageable pageable) {
        return calendarUserRepository.findAll(pageable).map(calendarUserMapper::toDTO);
    }

    // todo add tests
    @PreAuthorize("@userSecurity.isUser(#username, authentication)")
    public boolean hasAnyInvitations(String username) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(username).orElseThrow();
        return groupMembershipRepository.existsByUserIdAndMembershipRoleIn(calendarUser.getId(), List.of(MembershipRole.INVITED));
    }
}
