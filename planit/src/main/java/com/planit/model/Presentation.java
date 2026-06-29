package com.planit.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a presentation session inside an event.
 */
public class Presentation extends EventComponent {

    private static final long serialVersionUID = 1L;

    private String speakerName;
    private String topic;
    private List<String> requiredEquipment = new ArrayList<>();
    private double equipmentCost;

    public Presentation(String id, String title, LocalDateTime start, LocalDateTime end,
                         String speakerName, String topic, double equipmentCost) {
        super(id, title, start, end);
        this.speakerName = speakerName;
        this.topic = topic;
        this.equipmentCost = equipmentCost;
    }

    @Override
    public double calculateCost() {
        return equipmentCost;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getRequiredEquipment() {
        return requiredEquipment;
    }

    public double getEquipmentCost() {
        return equipmentCost;
    }

    public void setEquipmentCost(double equipmentCost) {
        this.equipmentCost = equipmentCost;
    }
}
