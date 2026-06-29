package com.planit.service;

import com.planit.model.Event;
import com.planit.model.EventComponent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Checks whether event costs remain within the specified budget
 * and calculates remaining budgets. Shows an error if not.
 */
public class BudgetService {

    private static final Logger LOGGER = Logger.getLogger(BudgetService.class.getName());

    public double calculateTotal(Event e) {
        return e.getTotalCost();
    }

    // Returns true if the event's total cost does not exceed its budget.
    public boolean validate(Event e) {
        boolean withinBudget = calculateTotal(e) <= e.getBudget();
        if (!withinBudget) {
            LOGGER.warning("Event " + e.getId() + " exceeds its budget!");
        }
        return withinBudget;
    }

    public double getRemainingBudget(Event e) {
        return e.getRemainingBudget();
    }

    // Returns the cost of each component.
    // Title is the key
    public Map<String, Double> getCostBreakdown(Event e) {
        Map<String, Double> breakdown = new LinkedHashMap<>(); // To preserve order
        for (EventComponent c : e.getComponents()) {
            breakdown.put(c.getTitle(), c.calculateCost());
        }
        return breakdown;
    }
}
