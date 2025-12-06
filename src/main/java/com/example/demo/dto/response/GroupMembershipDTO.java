package com.example.demo.dto.response;

import com.example.demo.dto.ColorDTO;
import com.example.demo.enumeration.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GroupMembershipDTO {
    private UUID groupId;
    private String groupName;
    private MembershipRole membershipRole;
    private ColorDTO color;
}
