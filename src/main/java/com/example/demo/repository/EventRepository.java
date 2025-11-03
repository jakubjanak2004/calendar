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
    List<Event> findByEventOwnerAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
            EventOwner eventOwner, Instant rangeStartInclusive, Instant rangeEndInclusive);

    @Query("select e.eventOwner.id from Event e where e.uuid = :id")
    Optional<Long> findOwnerIdById(@Param("id") UUID id);
}
