package com.planit.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeSlotTest {

    @Test
    void overlappingSlotsAreDetected() {
        TimeSlot a = new TimeSlot(LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 10, 0), "c1");
        TimeSlot b = new TimeSlot(LocalDateTime.of(2026, 6, 1, 9, 30),
                LocalDateTime.of(2026, 6, 1, 11, 0), "c2");

        assertTrue(a.overlaps(b));
    }

    @Test
    void nonOverlappingSlotsAreNotDetected() {
        TimeSlot a = new TimeSlot(LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 10, 0), "c1");
        TimeSlot b = new TimeSlot(LocalDateTime.of(2026, 6, 1, 10, 0),
                LocalDateTime.of(2026, 6, 1, 11, 0), "c2");

        assertFalse(a.overlaps(b));
    }
}
