package com.example.demo.controller;

import com.example.demo.dto.request.UpdateUserDTO;
import com.example.demo.dto.response.CalendarUserDTO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<CalendarUserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllPageable(pageable));
    }

    @PutMapping("/me")
    public ResponseEntity<CalendarUserDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO, Principal principal) {
        return ResponseEntity.ok(userService.updateUser(principal.getName(), updateUserDTO));
    }

    @GetMapping("/me/invitations/exists")
    public ResponseEntity<Boolean> getInvitationsExist(Principal principal) {
        return ResponseEntity.ok(userService.hasAnyInvitations(principal.getName()));
    }
}
