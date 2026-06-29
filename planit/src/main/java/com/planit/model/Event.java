package com.planit.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The event that is happening (NOT COMPONENTS).
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double budget;
    private String description;
    private List<EventComponent> components = new ArrayList<>();

    public Event(String id, String title, LocalDateTime startDate, LocalDateTime endDate, double budget, String description) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.description = description;
    }

    public void addComponent(EventComponent c) {
        components.add(c);
    }

    public void removeComponent(String id) {
        components.removeIf(c -> c.getId().equals(id));
    }

    public double getTotalCost() {
        double total = 0;
        for (EventComponent c : components) {
            total += c.calculateCost();
        }
        return total;
    }

    public double getRemainingBudget() {
        return budget - getTotalCost();
    }

    public List<EventComponent> getComponents() {
        return components;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title;
    }
}
