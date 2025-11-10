package com.example.demo.security;

import com.example.demo.model.CalendarUser;
import com.example.demo.model.EventOwner;
import com.example.demo.model.MembershipRole;
import com.example.demo.model.UserGroup;
import com.example.demo.repository.EventOwnerRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.CalendarUserRepository;
import com.example.demo.repository.GroupMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component("eventSecurity")
@RequiredArgsConstructor
public class EventSecurity {
    private final CalendarUserRepository calendarUserRepository;
    private final EventRepository eventRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final EventOwnerRepository eventOwnerRepository;

    public boolean isOwner(UUID eventId, Authentication auth) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(auth.getName()).orElseThrow();
        EventOwner eventOwner = eventRepository.findOwnerById(eventId).orElseThrow();
        return canUserEditEventOwner(calendarUser, eventOwner);
    }

    public boolean canEditEventOwner(UUID eventOwnerId, Authentication auth) {
        CalendarUser calendarUser = calendarUserRepository.findByUsername(auth.getName()).orElseThrow();
        EventOwner eventOwner = eventOwnerRepository.findById(eventOwnerId).orElseThrow();
        return canUserEditEventOwner(calendarUser, eventOwner);
    }

    private boolean canUserEditEventOwner(CalendarUser calendarUser, EventOwner eventOwner) {
        if (eventOwner instanceof CalendarUser) {
            return calendarUser.getId().equals(eventOwner.getId());
        }
        if (eventOwner instanceof UserGroup) {
            return groupMembershipRepository.existsByGroupIdAndUserIdAndMembershipRoleIn(
                    eventOwner.getId(), calendarUser.getId(), List.of(MembershipRole.ADMIN, MembershipRole.EDITOR)
            );
        }
        return false;
    }
}
