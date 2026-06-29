package com.planit.model;

import java.time.LocalDateTime;

/**
 * Represents workshop activities requiring resources and scheduling.
 */
public class Workshop extends EventComponent {

    private static final long serialVersionUID = 1L;

    private int maxParticipants;
    private double costPerPerson;
    private String facilitator;

    public Workshop(String id, String title, LocalDateTime start, LocalDateTime end,
                     int maxParticipants, double costPerPerson, String facilitator) {
        super(id, title, start, end);
        this.maxParticipants = maxParticipants;
        this.costPerPerson = costPerPerson;
        this.facilitator = facilitator;
    }

    @Override
    public double calculateCost() {
        return costPerPerson * maxParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public double getCostPerPerson() {
        return costPerPerson;
    }

    public void setCostPerPerson(double costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }
}
