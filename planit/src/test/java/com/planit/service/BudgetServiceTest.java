package com.planit.service;

import com.planit.model.Event;
import com.planit.model.Workshop;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BudgetServiceTest {

    private final BudgetService budgetService = new BudgetService();

    @Test
    void validateReturnsTrueWhenCostIsWithinBudget() {
        Event event = new Event("e1", "Small Workshop", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 12, 0), 500.0, "Demo event");
        Workshop w = new Workshop("c1", "Java Basics", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 11, 0), 5, 30.0, "Jonas Weber");
        event.addComponent(w);

        assertTrue(budgetService.validate(event));
    }

    @Test
    void validateReturnsFalseWhenCostExceedsBudget() {
        Event event = new Event("e2", "Big Conference", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0), 100.0, "Demo event");
        Workshop w = new Workshop("c1", "Cloud Native", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 11, 0), 10, 50.0, "Anna Müller");
        event.addComponent(w);

        assertFalse(budgetService.validate(event));
    }
}
