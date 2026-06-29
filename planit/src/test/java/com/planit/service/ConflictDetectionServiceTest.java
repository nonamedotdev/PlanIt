package com.planit.service;

import com.planit.model.Conflict;
import com.planit.model.Event;
import com.planit.model.Venue;
import com.planit.model.Workshop;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConflictDetectionServiceTest {

    @Test
    void detectsConflictWhenTwoComponentsShareVenueAtOverlappingTime() {
        Event event = new Event("e1", "Tech Summit", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0), 1000.0, "Demo event");

        Venue hallA = new Venue("v1", "Hall A", "Building 1", 100);

        Workshop w1 = new Workshop("c1", "Cloud Native", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 10, 0), 10, 20.0, "Jonas Weber");
        Workshop w2 = new Workshop("c2", "Java Basics", LocalDateTime.of(2026, 6, 1, 9, 30),
                LocalDateTime.of(2026, 6, 1, 11, 0), 10, 20.0, "Anna Müller");
        w1.assignResource(hallA);
        w2.assignResource(hallA);

        event.addComponent(w1);
        event.addComponent(w2);

        ConflictDetectionService service = new ConflictDetectionService();
        List<Conflict> conflicts = service.detectConflicts(event);

        assertEquals(1, conflicts.size());
    }

    @Test
    void noConflictWhenComponentsDoNotShareTime() {
        Event event = new Event("e2", "Conference", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0), 1000.0, "Demo event");

        Venue hallA = new Venue("v1", "Hall A", "Building 1", 100);

        Workshop w1 = new Workshop("c1", "Cloud Native", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 10, 0), 10, 20.0, "Jonas Weber");
        Workshop w2 = new Workshop("c2", "Java Basics", LocalDateTime.of(2026, 6, 1, 11, 0),
                LocalDateTime.of(2026, 6, 1, 12, 0), 10, 20.0, "Anna Müller");
        w1.assignResource(hallA);
        w2.assignResource(hallA);

        event.addComponent(w1);
        event.addComponent(w2);

        ConflictDetectionService service = new ConflictDetectionService();
        List<Conflict> conflicts = service.detectConflicts(event);

        assertTrue(conflicts.isEmpty());
    }
}
