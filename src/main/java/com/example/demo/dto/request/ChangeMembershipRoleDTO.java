package com.example.demo.dto.request;

import com.example.demo.model.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeMembershipRoleDTO {
    private MembershipRole newMembershipRole;
}
