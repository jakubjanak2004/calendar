package com.example.demo.repository;

import com.example.demo.model.Event;
import com.example.demo.model.EventOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByEventOwnerIdAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
            UUID eventOwnerId, Instant rangeStartInclusive, Instant rangeEndInclusive);

    @Query("select e.eventOwner.id from Event e where e.id = :id")
    Optional<UUID> findOwnerIdById(@Param("id") UUID id);

    @Query("select e.eventOwner from Event e where e.id = :id")
    Optional<EventOwner> findOwnerById(@Param("id") UUID id);
}
