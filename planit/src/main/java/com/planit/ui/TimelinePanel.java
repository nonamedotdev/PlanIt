package com.planit.ui;

import com.planit.model.Conflict;
import com.planit.model.Event;
import com.planit.model.EventComponent;
import com.planit.model.Presentation;
import com.planit.model.ServiceSlot;
import com.planit.model.Workshop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Components that are currently involved in a
 * conflict are highlighted in bright red.
 * Use the Previous/Next buttons to move between weeks.
 */
public class TimelinePanel extends BorderPane {

    private static final String[] DAY_NAMES =
            {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private static final double HOUR_HEIGHT = 40;
    private static final double DAY_WIDTH = 130;
    private static final double GUTTER_WIDTH = 50;

    public static final Color EVENT_COLOR = Color.web("#4A90D2");        // blue
    public static final Color PRESENTATION_COLOR = Color.web("#43A047"); // green
    public static final Color WORKSHOP_COLOR = Color.web("#8E44AD");     // purple
    public static final Color SERVICE_SLOT_COLOR = Color.web("#16A085"); // teal
    public static final Color CONFLICT_COLOR = Color.web("#E53935");     // bright red

    private final Label weekLabel = new Label();
    private final HBox dayHeaders = new HBox();
    private final HBox bodyRow = new HBox();
    private final ScrollPane scrollPane = new ScrollPane(bodyRow);

    private Event event;
    private List<Conflict> conflicts = new ArrayList<>();
    private LocalDate weekStart;

    public TimelinePanel() {
        Button prevButton = new Button("< Previous Week");
        prevButton.setOnAction(e -> {
            weekStart = weekStart.minusWeeks(1);
            refresh();
        });

        Button nextButton = new Button("Next Week >");
        nextButton.setOnAction(e -> {
            weekStart = weekStart.plusWeeks(1);
            refresh();
        });

        HBox nav = new HBox(15, prevButton, weekLabel, nextButton);
        nav.setAlignment(Pos.CENTER);
        nav.setPadding(new Insets(10, 10, 5, 10));

        VBox top = new VBox(5, nav, buildLegend(), dayHeaders);
        top.setPadding(new Insets(0, 10, 0, 10));

        scrollPane.setFitToWidth(false);
        scrollPane.setPrefViewportHeight(480);

        weekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        setTop(top);
        setCenter(scrollPane);

        refresh();
    }

    // Sets the event whose components should be displayed, and jumps to its starting week.
    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            weekStart = event.getStartDate().toLocalDate().with(DayOfWeek.MONDAY);
        }
        refresh();
    }

    // Sets the currently known conflicts so affected components can be highlighted in red.
    public void setConflicts(List<Conflict> conflicts) {
        this.conflicts = conflicts != null ? conflicts : new ArrayList<>();
        refresh();
    }

    private HBox buildLegend() {
        HBox legend = new HBox(18,
                legendItem("Event", EVENT_COLOR),
                legendItem("Presentation", PRESENTATION_COLOR),
                legendItem("Workshop", WORKSHOP_COLOR),
                legendItem("Service Slot", SERVICE_SLOT_COLOR),
                legendItem("Conflict", CONFLICT_COLOR));
        legend.setAlignment(Pos.CENTER);
        return legend;
    }

    private HBox legendItem(String text, Color color) {
        Rectangle swatch = new Rectangle(14, 14);
        swatch.setFill(color);
        swatch.setArcWidth(4);
        swatch.setArcHeight(4);
        HBox item = new HBox(5, swatch, new Label(text));
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private void refresh() {
        dayHeaders.getChildren().clear();
        bodyRow.getChildren().clear();
        weekLabel.setText("Week of " + weekStart);

        Region headerGutterSpacer = new Region();
        headerGutterSpacer.setPrefWidth(GUTTER_WIDTH);
        dayHeaders.getChildren().add(headerGutterSpacer);

        // Hour shown once on the left of the scrollable body.
        Pane gutter = new Pane();
        gutter.setPrefWidth(GUTTER_WIDTH);
        gutter.setPrefHeight(24 * HOUR_HEIGHT);
        for (int h = 0; h <= 24; h++) {
            Label hourLabel = new Label(String.format("%02d:00", h % 24));
            hourLabel.setFont(Font.font(10));
            hourLabel.setLayoutX(2);
            hourLabel.setLayoutY(h * HOUR_HEIGHT - 6);
            gutter.getChildren().add(hourLabel);
        }
        bodyRow.getChildren().add(gutter);

        for (int i = 0; i < 7; i++) {
            LocalDate day = weekStart.plusDays(i);
            boolean inEventRange = event != null
                    && !day.isBefore(event.getStartDate().toLocalDate())
                    && !day.isAfter(event.getEndDate().toLocalDate());

            dayHeaders.getChildren().add(buildDayHeader(day, i, inEventRange));
            bodyRow.getChildren().add(buildDayColumn(day, inEventRange));
        }
    }

    private Label buildDayHeader(LocalDate day, int dayIndex, boolean inEventRange) {
        Label dayLabel = new Label(DAY_NAMES[dayIndex] + "\n" + day
                + (inEventRange && event != null ? "\n" + event.getTitle() : ""));
        dayLabel.setPrefWidth(DAY_WIDTH);
        dayLabel.setAlignment(Pos.CENTER);
        dayLabel.setPadding(new Insets(4));
        dayLabel.setFont(Font.font(11));
        if (inEventRange) {
            dayLabel.setTextFill(Color.WHITE);
            dayLabel.setBackground(new Background(new BackgroundFill(
                    EVENT_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        return dayLabel;
    }

    private Pane buildDayColumn(LocalDate day, boolean inEventRange) {
        Pane dayPane = new Pane();
        dayPane.setPrefWidth(DAY_WIDTH);
        dayPane.setPrefHeight(24 * HOUR_HEIGHT);
        dayPane.setBackground(new Background(new BackgroundFill(
                inEventRange ? EVENT_COLOR.deriveColor(0, 1, 1, 0.10) : Color.web("#FAFAFA"),
                CornerRadii.EMPTY, Insets.EMPTY)));

        for (int h = 0; h <= 24; h++) {
            Line line = new Line(0, h * HOUR_HEIGHT, DAY_WIDTH, h * HOUR_HEIGHT);
            line.setStroke(Color.web("#DDDDDD"));
            dayPane.getChildren().add(line);
        }

        if (event != null) {
            LocalDateTime dayStart = day.atStartOfDay();
            LocalDateTime dayEnd = day.plusDays(1).atStartOfDay();

            for (EventComponent c : event.getComponents()) {
                if (!c.getStart().isBefore(dayEnd) || !c.getEnd().isAfter(dayStart)) {
                    continue; // component does not touch this day
                }
                LocalDateTime sliceStart = c.getStart().isAfter(dayStart) ? c.getStart() : dayStart;
                LocalDateTime sliceEnd = c.getEnd().isBefore(dayEnd) ? c.getEnd() : dayEnd;

                double y = (sliceStart.getHour() * 60 + sliceStart.getMinute()) / 60.0 * HOUR_HEIGHT;
                double height = Math.max(16,
                        Duration.between(sliceStart, sliceEnd).toMinutes() / 60.0 * HOUR_HEIGHT);

                boolean conflicted = isConflicting(c);
                Color color = conflicted ? CONFLICT_COLOR : colorFor(c);

                Rectangle rect = new Rectangle(DAY_WIDTH - 8, height);
                rect.setLayoutX(4);
                rect.setLayoutY(y);
                rect.setFill(color);
                rect.setArcWidth(6);
                rect.setArcHeight(6);
                rect.setStroke(conflicted ? Color.web("#7A0000") : color.darker());

                Label label = new Label(c.getStart().toLocalTime() + "-" + c.getEnd().toLocalTime()
                        + "\n" + c.getClass().getSimpleName() + ": " + c.getTitle());
                label.setFont(Font.font(10));
                label.setTextFill(Color.WHITE);
                label.setWrapText(true);
                label.setLayoutX(6);
                label.setLayoutY(y + 2);
                label.setMaxWidth(DAY_WIDTH - 12);
                label.setMaxHeight(height - 4);

                Tooltip tooltip = new Tooltip(buildTooltipText(c, conflicted));
                Tooltip.install(rect, tooltip);
                Tooltip.install(label, tooltip);

                dayPane.getChildren().addAll(rect, label);
            }
        }

        return dayPane;
    }

    private Color colorFor(EventComponent c) {
        if (c instanceof Presentation) {
            return PRESENTATION_COLOR;
        }
        if (c instanceof Workshop) {
            return WORKSHOP_COLOR;
        }
        if (c instanceof ServiceSlot) {
            return SERVICE_SLOT_COLOR;
        }
        return Color.GRAY;
    }

    private boolean isConflicting(EventComponent c) {
        for (Conflict conflict : conflicts) {
            if (conflict.getComp1() == c || conflict.getComp2() == c) {
                return true;
            }
        }
        return false;
    }

    private String buildTooltipText(EventComponent c, boolean conflicted) {
        String text = c.getClass().getSimpleName() + ": " + c.getTitle() + "\n"
                + c.getStart().format(UiUtil.DATE_TIME_FORMAT) + " - "
                + c.getEnd().format(UiUtil.DATE_TIME_FORMAT) + "\n"
                + "Cost: " + c.calculateCost();
        if (conflicted) {
            text += "\n\u26A0 CONFLICT - resource double-booked";
        }
        return text;
    }
}
