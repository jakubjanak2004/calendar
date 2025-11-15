package com.example.demo.dto.response;

import com.example.demo.model.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GroupCalendarUserDTO {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private MembershipRole membershipRole;
}
