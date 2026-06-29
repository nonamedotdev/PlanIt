package com.planit.ui;

import com.planit.model.Conflict;
import com.planit.model.Event;
import com.planit.model.EventComponent;
import com.planit.model.Resource;
import com.planit.model.Staff;
import com.planit.model.TimeSlot;
import com.planit.model.Venue;
import com.planit.service.BudgetService;
import com.planit.service.ConflictDetectionService;
import com.planit.service.PersistenceService;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());

    private final PersistenceService persistenceService = new PersistenceService("data");
    private final BudgetService budgetService = new BudgetService();
    private final ConflictDetectionService conflictService = new ConflictDetectionService();

    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObservableList<Resource> resources = FXCollections.observableArrayList();

    private ListView<Event> eventListView;
    private Event selectedEvent;
    private List<Conflict> currentConflicts = new ArrayList<>();

    // Overview tab
    private final Label titleLabel = new Label();
    private final Label datesLabel = new Label();
    private final Label budgetLabel = new Label();
    private final Label remainingLabel = new Label();
    private final TextArea descriptionArea = new TextArea();

    // Components tab
    private final ListView<EventComponent> componentListView = new ListView<>();

    // Resources tab
    private final ListView<Resource> resourceListView = new ListView<>();

    // Weekly calendar tab
    private final TimelinePanel timelinePanel = new TimelinePanel();

    // Budget and conflicts tab
    private final Label totalCostLabel = new Label();
    private final Label remainingBudgetLabel = new Label();
    private final Label validBudgetLabel = new Label();
    private final ListView<String> breakdownListView = new ListView<>();
    private final ListView<Conflict> conflictListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) {
        events.addAll(persistenceService.loadAll());

        BorderPane root = new BorderPane();
        root.setTop(buildToolbar());
        root.setLeft(buildEventList());
        root.setCenter(buildTabs());

        Scene scene = new Scene(root, 1050, 650);
        primaryStage.setTitle("PlanIt");
        primaryStage.setScene(scene);
        primaryStage.show();

        LOGGER.info("PlanIt started with " + events.size() + " event(s) loaded.");
        refreshAll();
    }

    private HBox buildToolbar() {
        Button newEventBtn = new Button("New Event");
        newEventBtn.setOnAction(e -> onNewEvent());

        Button editEventBtn = new Button("Edit Event");
        editEventBtn.setOnAction(e -> onEditEvent());

        Button deleteEventBtn = new Button("Delete Event");
        deleteEventBtn.setOnAction(e -> onDeleteEvent());

        Button saveBtn = new Button("Save All");
        saveBtn.setOnAction(e -> onSave());

        HBox box = new HBox(10, newEventBtn, editEventBtn, deleteEventBtn, saveBtn);
        box.setPadding(new Insets(10));
        return box;
    }

    private VBox buildEventList() {
        eventListView = new ListView<>(events);
        eventListView.setPrefWidth(220);
        eventListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEvent = newVal;
            refreshAll();
        });

        VBox box = new VBox(5, new Label("Events"), eventListView);
        box.setPadding(new Insets(10));
        return box;
    }

    private TabPane buildTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().add(new Tab("Overview", buildOverviewTab()));
        tabPane.getTabs().add(new Tab("Components", buildComponentsTab()));
        tabPane.getTabs().add(new Tab("Resources", buildResourcesTab()));
        tabPane.getTabs().add(new Tab("Weekly Calendar", timelinePanel));
        tabPane.getTabs().add(new Tab("Budget & Conflicts", buildBudgetTab()));

        return tabPane;
    }

    private VBox buildOverviewTab() {
        descriptionArea.setEditable(false);
        descriptionArea.setPrefRowCount(5);
        VBox box = new VBox(10, titleLabel, datesLabel, budgetLabel, remainingLabel,
                new Label("Description:"), descriptionArea);
        box.setPadding(new Insets(15));
        return box;
    }

    private VBox buildComponentsTab() {
        Button addBtn = new Button("Add Component");
        addBtn.setOnAction(e -> onAddComponent());

        Button assignBtn = new Button("Assign Resource");
        assignBtn.setOnAction(e -> onAssignResource());

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> onRemoveComponent());

        HBox buttons = new HBox(10, addBtn, assignBtn, removeBtn);

        VBox box = new VBox(10, buttons, componentListView);
        box.setPadding(new Insets(15));
        return box;
    }

    private VBox buildResourcesTab() {
        Button addResourceBtn = new Button("Add Resource");
        addResourceBtn.setOnAction(e -> onAddResource());

        resourceListView.setItems(resources);

        VBox box = new VBox(10, addResourceBtn, resourceListView);
        box.setPadding(new Insets(15));
        return box;
    }

    private VBox buildBudgetTab() {
        Button detectConflictsBtn = new Button("Detect Conflicts");
        detectConflictsBtn.setOnAction(e -> onDetectConflicts());

        VBox box = new VBox(10,
                totalCostLabel, remainingBudgetLabel, validBudgetLabel,
                new Label("Cost Breakdown:"), breakdownListView,
                detectConflictsBtn,
                new Label("Conflicts:"), conflictListView);
        box.setPadding(new Insets(15));
        return box;
    }

    // Different actions on events

    private void onNewEvent() {
        NewEventDialog dialog = new NewEventDialog();
        Optional<Event> result = dialog.showAndWait();
        result.ifPresent(event -> {
            events.add(event);
            persistenceService.save(event);
            eventListView.getSelectionModel().select(event);
        });
    }

    private void onEditEvent() {
        if (selectedEvent == null) {
            UiUtil.showError("Please select an event first.");
            return;
        }
        NewEventDialog dialog = new NewEventDialog(selectedEvent);
        Optional<Event> result = dialog.showAndWait();
        result.ifPresent(updated -> {
            selectedEvent.setTitle(updated.getTitle());
            selectedEvent.setStartDate(updated.getStartDate());
            selectedEvent.setEndDate(updated.getEndDate());
            selectedEvent.setBudget(updated.getBudget());
            selectedEvent.setDescription(updated.getDescription());
            persistenceService.save(selectedEvent);
            eventListView.refresh();
            refreshAll();
        });
    }

    private void onDeleteEvent() {
        if (selectedEvent == null) {
            UiUtil.showError("Please select an event first.");
            return;
        }
        persistenceService.delete(selectedEvent.getId());
        events.remove(selectedEvent);
        selectedEvent = null;
        refreshAll();
    }

    private void onSave() {
        for (Event event : events) {
            persistenceService.save(event);
        }
        UiUtil.showInfo("All events saved.");
    }

    private void onAddComponent() {
        if (selectedEvent == null) {
            UiUtil.showError("Please select an event first.");
            return;
        }
        AddComponentDialog dialog = new AddComponentDialog(selectedEvent);
        Optional<EventComponent> result = dialog.showAndWait();
        result.ifPresent(component -> {
            selectedEvent.addComponent(component);
            refreshAll();
        });
    }

    private void onRemoveComponent() {
        EventComponent selected = componentListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null || selected == null) {
            UiUtil.showError("Please select a component first.");
            return;
        }
        selectedEvent.removeComponent(selected.getId());
        refreshAll();
    }

    private void onAssignResource() {
        EventComponent selected = componentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtil.showError("Please select a component first.");
            return;
        }
        if (resources.isEmpty()) {
            UiUtil.showError("No resources available. Please add a resource first.");
            return;
        }
        AssignResourceDialog dialog = new AssignResourceDialog(new ArrayList<>(resources));
        Optional<Resource> result = dialog.showAndWait();
        result.ifPresent(resource -> {
            selected.assignResource(resource);
            TimeSlot slot = new TimeSlot(selected.getStart(), selected.getEnd(), selected.getId());
            if (resource instanceof Venue venue) {
                venue.addBooking(slot);
            } else if (resource instanceof Staff staff) {
                staff.addShift(slot);
            }
            refreshAll();

            boolean involvesSelected = currentConflicts.stream()
                    .anyMatch(c -> c.getComp1() == selected || c.getComp2() == selected);
            if (involvesSelected) {
                UiUtil.showError("Assigning \"" + resource.getName() + "\" to \"" + selected.getTitle()
                        + "\" creates a scheduling conflict. It is now highlighted in red on the "
                        + "Weekly Calendar, with details in the Budget & Conflicts tab.");
            }
        });
    }

    private void onAddResource() {
        AddResourceDialog dialog = new AddResourceDialog();
        Optional<Resource> result = dialog.showAndWait();
        result.ifPresent(resources::add);
    }

    private void onDetectConflicts() {
        if (selectedEvent == null) {
            UiUtil.showError("Please select an event first.");
            return;
        }
        refreshAll();
        if (currentConflicts.isEmpty()) {
            UiUtil.showInfo("No conflicts detected.");
        }
    }


    private void refreshAll() {
        if (selectedEvent == null) {
            titleLabel.setText("No event selected.");
            datesLabel.setText("");
            budgetLabel.setText("");
            remainingLabel.setText("");
            descriptionArea.setText("");
            componentListView.setItems(FXCollections.observableArrayList());
            currentConflicts = new ArrayList<>();
            timelinePanel.setEvent(null);
            timelinePanel.setConflicts(currentConflicts);
            totalCostLabel.setText("");
            remainingBudgetLabel.setText("");
            validBudgetLabel.setText("");
            breakdownListView.setItems(FXCollections.observableArrayList());
            conflictListView.setItems(FXCollections.observableArrayList());
            return;
        }

        titleLabel.setText("Title: " + selectedEvent.getTitle());
        datesLabel.setText("From " + selectedEvent.getStartDate().format(UiUtil.DATE_TIME_FORMAT)
                + " to " + selectedEvent.getEndDate().format(UiUtil.DATE_TIME_FORMAT));
        budgetLabel.setText("Budget: " + selectedEvent.getBudget());
        remainingLabel.setText("Remaining budget: " + selectedEvent.getRemainingBudget());
        descriptionArea.setText(selectedEvent.getDescription());

        componentListView.setItems(FXCollections.observableArrayList(selectedEvent.getComponents()));
        componentListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(EventComponent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("[" + item.getClass().getSimpleName() + "] " + item.getTitle()
                            + " (" + item.getStart().format(UiUtil.DATE_TIME_FORMAT) + " - "
                            + item.getEnd().format(UiUtil.DATE_TIME_FORMAT) + ") - Cost: "
                            + item.calculateCost());
                }
            }
        });

        currentConflicts = conflictService.detectConflicts(selectedEvent);

        timelinePanel.setEvent(selectedEvent);
        timelinePanel.setConflicts(currentConflicts);

        totalCostLabel.setText("Total cost: " + budgetService.calculateTotal(selectedEvent));
        remainingBudgetLabel.setText("Remaining budget: " + budgetService.getRemainingBudget(selectedEvent));
        validBudgetLabel.setText("Within budget: " + budgetService.validate(selectedEvent));

        Map<String, Double> breakdown = budgetService.getCostBreakdown(selectedEvent);
        List<String> breakdownLines = new ArrayList<>();
        for (Map.Entry<String, Double> entry : breakdown.entrySet()) {
            breakdownLines.add(entry.getKey() + ": " + entry.getValue());
        }
        breakdownListView.setItems(FXCollections.observableArrayList(breakdownLines));

        conflictListView.setItems(FXCollections.observableArrayList(currentConflicts));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
