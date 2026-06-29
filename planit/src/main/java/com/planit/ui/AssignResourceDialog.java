package com.planit.ui;

import com.planit.model.Resource;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Lets the user pick one resource from the list of all resources
 * to assign it to a selected event component.
 */
public class AssignResourceDialog extends Dialog<Resource> {

    public AssignResourceDialog(List<Resource> availableResources) {
        setTitle("Assign Resource");

        ListView<Resource> listView = new ListView<>();
        listView.getItems().addAll(availableResources);

        VBox box = new VBox(10, new Label("Select a resource to assign:"), listView);
        box.setPadding(new Insets(20));

        getDialogPane().setContent(box);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }
}
