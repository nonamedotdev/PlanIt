package com.planit.ui;

import com.planit.model.Resource;
import com.planit.model.Staff;
import com.planit.model.Venue;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.UUID;

/**
 * Form dialog for adding a new Resource (Venue or Staff).
 */
public class AddResourceDialog extends Dialog<Resource> {

    public AddResourceDialog() {
        setTitle("Add Resource");

        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll("Venue", "Staff");
        typeChoice.setValue("Venue");

        TextField nameField = new TextField();

        // Venue-specific fields
        TextField locationField = new TextField();
        TextField capacityField = new TextField();

        // Staff-specific fields
        TextField roleField = new TextField();
        TextField hourlyRateField = new TextField();
        TextField emailField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.addRow(0, new Label("Type:"), typeChoice);
        grid.addRow(1, new Label("Name:"), nameField);
        grid.addRow(2, new Label("Location:"), locationField);
        grid.addRow(3, new Label("Capacity:"), capacityField);
        grid.addRow(4, new Label("Role:"), roleField);
        grid.addRow(5, new Label("Hourly rate:"), hourlyRateField);
        grid.addRow(6, new Label("Email:"), emailField);

        Runnable updateVisibility = () -> {
            boolean isVenue = "Venue".equals(typeChoice.getValue());
            setRowVisible(grid, locationField, isVenue);
            setRowVisible(grid, capacityField, isVenue);
            setRowVisible(grid, roleField, !isVenue);
            setRowVisible(grid, hourlyRateField, !isVenue);
            setRowVisible(grid, emailField, !isVenue);
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
                String name = nameField.getText();
                if ("Venue".equals(typeChoice.getValue())) {
                    return new Venue(id, name, locationField.getText(),
                            Integer.parseInt(capacityField.getText()));
                } else {
                    return new Staff(id, name, roleField.getText(),
                            Double.parseDouble(hourlyRateField.getText()),
                            emailField.getText());
                }
            } catch (Exception ex) {
                UiUtil.showError("Invalid input: " + ex.getMessage());
                return null;
            }
        });
    }

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
