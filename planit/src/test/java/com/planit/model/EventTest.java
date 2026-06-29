package com.planit.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventTest {
    //
    @Test
    void totalCostSumsAllComponentCosts() {
        Event event = new Event("e1", "Tech Summit", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0), 1000.0, "Demo event");

        Workshop workshop = new Workshop("c1", "Cloud Native", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 11, 0), 10, 20.0, "Jonas Weber");
        ServiceSlot serviceSlot = new ServiceSlot("c2", "Registration", LocalDateTime.of(2026, 6, 1, 8, 0),
                LocalDateTime.of(2026, 6, 1, 9, 0), "Registration", 50.0, "EventCo");

        event.addComponent(workshop);
        event.addComponent(serviceSlot);

        assertEquals(250.0, event.getTotalCost());
    }

    @Test
    void remainingBudgetIsBudgetMinusTotalCost() {
        Event event = new Event("e2", "Small Workshop", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 12, 0), 500.0, "Demo event");

        Workshop workshop = new Workshop("c1", "Java Basics", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 11, 0), 5, 30.0, "Jonas Weber");
        event.addComponent(workshop);

        assertEquals(350.0, event.getRemainingBudget());
    }

    @Test
    void removeComponentRemovesItFromList() {
        Event event = new Event("e3", "Conference", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0), 1000.0, "Demo event");

        Presentation presentation = new Presentation("c1", "Opening", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 10, 0), "Anna Müller", "Welcome", 0.0);
        event.addComponent(presentation);
        event.removeComponent("c1");

        assertEquals(0, event.getComponents().size());
    }
}
