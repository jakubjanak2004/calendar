package com.example.demo.controller;

import com.example.demo.dto.response.UserGroupDTO;
import com.example.demo.model.CalendarUser;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final CalendarUserRepository calendarUserRepository;
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<Page<UserGroupDTO>> getPagesUserGroups(Pageable pageable, Principal principal) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(groupService.getAllGroupsForUserPageable(calendarUser.getId(), pageable));
    }
}
