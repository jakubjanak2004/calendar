package com.example.demo.dto.response;

import com.example.demo.model.MembershipRole;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GroupMembershipDTO {
    // todo this is a group id, rename it later to groupId to be explicit
    private UUID id;
    private String groupName;
    private MembershipRole membershipRole;
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Color must be in hex format, e.g. #RRGGBB")
    private String color;
}
