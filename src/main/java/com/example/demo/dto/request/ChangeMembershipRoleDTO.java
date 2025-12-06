package com.example.demo.dto.request;

import com.example.demo.enumeration.MembershipRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeMembershipRoleDTO {
    @NotNull
    private MembershipRole newMembershipRole;
}
