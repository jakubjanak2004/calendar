package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class EventOwner {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "eventOwner", cascade = CascadeType.PERSIST)
    private List<Event> events;
}
