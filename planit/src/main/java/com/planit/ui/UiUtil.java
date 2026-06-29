package com.planit.ui;

import javafx.scene.control.Alert;

import java.time.format.DateTimeFormatter;

/**
 * Alerts.
 */
public class UiUtil {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private UiUtil() {
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
