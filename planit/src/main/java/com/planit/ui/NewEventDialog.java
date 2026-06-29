package com.planit.ui;

import com.planit.model.Event;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Simple form dialog to create a new event
 */
public class NewEventDialog extends Dialog<Event> {

    public NewEventDialog() {
        this(null);
    }

    public NewEventDialog(Event existing) {
        setTitle(existing == null ? "New Event" : "Edit Event");

        TextField titleField = new TextField();
        TextField startField = new TextField();
        startField.setPromptText("yyyy-MM-dd HH:mm");
        TextField endField = new TextField();
        endField.setPromptText("yyyy-MM-dd HH:mm");
        TextField budgetField = new TextField();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);

        if (existing != null) {
            titleField.setText(existing.getTitle());
            startField.setText(existing.getStartDate().format(UiUtil.DATE_TIME_FORMAT));
            endField.setText(existing.getEndDate().format(UiUtil.DATE_TIME_FORMAT));
            budgetField.setText(String.valueOf(existing.getBudget()));
            descriptionArea.setText(existing.getDescription());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.addRow(0, new Label("Title:"), titleField);
        grid.addRow(1, new Label("Start (yyyy-MM-dd HH:mm):"), startField);
        grid.addRow(2, new Label("End (yyyy-MM-dd HH:mm):"), endField);
        grid.addRow(3, new Label("Budget:"), budgetField);
        grid.addRow(4, new Label("Description:"), descriptionArea);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) {
                return null;
            }
            try {
                String id = existing != null ? existing.getId() : UUID.randomUUID().toString();
                String title = titleField.getText();
                LocalDateTime start = LocalDateTime.parse(startField.getText(), UiUtil.DATE_TIME_FORMAT);
                LocalDateTime end = LocalDateTime.parse(endField.getText(), UiUtil.DATE_TIME_FORMAT);
                double budget = Double.parseDouble(budgetField.getText());
                String description = descriptionArea.getText();
                return new Event(id, title, start, end, budget, description);
            } catch (Exception ex) {
                UiUtil.showError("Invalid input: " + ex.getMessage());
                return null;
            }
        });
    }
}
