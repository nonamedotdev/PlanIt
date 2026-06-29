package com.planit.ui;

import com.planit.model.Event;
import com.planit.model.EventComponent;
import com.planit.model.Presentation;
import com.planit.model.ServiceSlot;
import com.planit.model.Workshop;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Form dialog for adding a new EventComponent
 */
public class AddComponentDialog extends Dialog<EventComponent> {

    public AddComponentDialog(Event parentEvent) {
        setTitle("Add Component");

        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll("Presentation", "Workshop", "ServiceSlot");
        typeChoice.setValue("Presentation");

        TextField titleField = new TextField();
        TextField startField = new TextField();
        startField.setPromptText("yyyy-MM-dd HH:mm");
        TextField endField = new TextField();
        endField.setPromptText("yyyy-MM-dd HH:mm");

        // Presentation-specific fields
        TextField speakerField = new TextField();
        TextField topicField = new TextField();
        TextField equipmentCostField = new TextField();

        // Workshop-specific fields
        TextField maxParticipantsField = new TextField();
        TextField costPerPersonField = new TextField();
        TextField facilitatorField = new TextField();

        // ServiceSlot-specific fields
        TextField serviceTypeField = new TextField();
        TextField flatRateField = new TextField();
        TextField providerNameField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.addRow(0, new Label("Type:"), typeChoice);
        grid.addRow(1, new Label("Title:"), titleField);
        grid.addRow(2, new Label("Start (yyyy-MM-dd HH:mm):"), startField);
        grid.addRow(3, new Label("End (yyyy-MM-dd HH:mm):"), endField);

        grid.addRow(4, new Label("Speaker:"), speakerField);
        grid.addRow(5, new Label("Topic:"), topicField);
        grid.addRow(6, new Label("Equipment cost:"), equipmentCostField);

        grid.addRow(7, new Label("Max participants:"), maxParticipantsField);
        grid.addRow(8, new Label("Cost per person:"), costPerPersonField);
        grid.addRow(9, new Label("Facilitator:"), facilitatorField);

        grid.addRow(10, new Label("Service type:"), serviceTypeField);
        grid.addRow(11, new Label("Flat rate:"), flatRateField);
        grid.addRow(12, new Label("Provider name:"), providerNameField);

        Runnable updateVisibility = () -> {
            String type = typeChoice.getValue();
            boolean isPresentation = "Presentation".equals(type);
            boolean isWorkshop = "Workshop".equals(type);
            boolean isServiceSlot = "ServiceSlot".equals(type);

            setRowVisible(grid, speakerField, isPresentation);
            setRowVisible(grid, topicField, isPresentation);
            setRowVisible(grid, equipmentCostField, isPresentation);

            setRowVisible(grid, maxParticipantsField, isWorkshop);
            setRowVisible(grid, costPerPersonField, isWorkshop);
            setRowVisible(grid, facilitatorField, isWorkshop);

            setRowVisible(grid, serviceTypeField, isServiceSlot);
            setRowVisible(grid, flatRateField, isServiceSlot);
            setRowVisible(grid, providerNameField, isServiceSlot);
        };

        typeChoice.setOnAction(e -> updateVisibility.run());
        updateVisibility.run();

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) {
                return null;
            }
            try {
                String id = UUID.randomUUID().toString();
                String title = titleField.getText();
                LocalDateTime start = LocalDateTime.parse(startField.getText(), UiUtil.DATE_TIME_FORMAT);
                LocalDateTime end = LocalDateTime.parse(endField.getText(), UiUtil.DATE_TIME_FORMAT);

                if (!end.isAfter(start)) {
                    UiUtil.showError("The end time must be after the start time.");
                    return null;
                }
                if (start.isBefore(parentEvent.getStartDate()) || end.isAfter(parentEvent.getEndDate())) {
                    UiUtil.showError("Component time (" + start.format(UiUtil.DATE_TIME_FORMAT) + " - "
                            + end.format(UiUtil.DATE_TIME_FORMAT) + ") must fit within the event's time range ("
                            + parentEvent.getStartDate().format(UiUtil.DATE_TIME_FORMAT) + " - "
                            + parentEvent.getEndDate().format(UiUtil.DATE_TIME_FORMAT) + ").");
                    return null;
                }

                switch (typeChoice.getValue()) {
                    case "Presentation":
                        return new Presentation(id, title, start, end,
                                speakerField.getText(), topicField.getText(),
                                Double.parseDouble(equipmentCostField.getText()));
                    case "Workshop":
                        return new Workshop(id, title, start, end,
                                Integer.parseInt(maxParticipantsField.getText()),
                                Double.parseDouble(costPerPersonField.getText()),
                                facilitatorField.getText());
                    case "ServiceSlot":
                        return new ServiceSlot(id, title, start, end,
                                serviceTypeField.getText(),
                                Double.parseDouble(flatRateField.getText()),
                                providerNameField.getText());
                    default:
                        return null;
                }
            } catch (Exception ex) {
                UiUtil.showError("Invalid input: " + ex.getMessage());
                return null;
            }
        });
    }

    // Shows or hides a field together with its label in the same grid row.
    private void setRowVisible(GridPane grid, Node field, boolean visible) {
        field.setVisible(visible);
        field.setManaged(visible);
        Integer rowIndex = GridPane.getRowIndex(field);
        int row = rowIndex == null ? 0 : rowIndex;
        for (Node n : grid.getChildren()) {
            Integer r = GridPane.getRowIndex(n);
            int nodeRow = r == null ? 0 : r;
            if (nodeRow == row && n instanceof Label) {
                n.setVisible(visible);
                n.setManaged(visible);
            }
        }
    }
}
