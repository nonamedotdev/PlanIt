# PlanIt - Resource and Event Coordination System

A Java 21 + JavaFX desktop application for planning events, assigning
resources (venues/staff), detecting scheduling conflicts, and tracking
budgets.

## Opening the project in IntelliJ

1. Unzip this archive.
2. In IntelliJ: **File -> Open...** and select the `pom.xml` file
   (or the unzipped project folder).
3. Choose "Open as Project". IntelliJ will detect it as a Maven project
   and download the dependencies (JavaFX 21 and JUnit 5) automatically.

## Running the application

Either:

- Run `MainApp.main()` directly from IntelliJ (right-click
  `src/main/java/com/planit/ui/MainApp.java` -> Run), or
- Use the Maven JavaFX plugin from a terminal in the project folder:

  ```
  mvn javafx:run
  ```

If running `MainApp` directly from IntelliJ produces a JavaFX module
error, add the following VM option to the run configuration (IntelliJ
usually configures this automatically when JavaFX is a Maven
dependency, but if not):

```
--module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls
```

(Normally not needed when JavaFX is pulled in as a regular Maven
dependency, which is how this project is set up.)

## Running the tests

```
mvn test
```

or run the test classes directly from IntelliJ's built-in JUnit 5
runner.

## Project structure

```
src/main/java/com/planit/model/    - domain classes (Event, EventComponent,
                                      Presentation, Workshop, ServiceSlot,
                                      Resource, Venue, Staff, TimeSlot, Conflict)
src/main/java/com/planit/service/  - business logic (ConflictDetectionService,
                                      BudgetService, PersistenceService)
src/main/java/com/planit/ui/       - JavaFX UI (MainApp + dialogs + TimelinePanel)
src/test/java/com/planit/         - JUnit 5 tests
data/                              - default folder where saved events (.ser files)
                                      are stored at runtime
```

## Notes

- Persistence uses plain Java Serialization (`ObjectOutputStream` /
  `ObjectInputStream`), one `.ser` file per event, stored in `data/`.
- The UI is intentionally plain JavaFX (no CSS, no FXML) using standard
  layouts (`BorderPane`, `GridPane`, `TabPane`, etc.).
- The Weekly Calendar tab shows all event components for the currently
  selected week (Monday-Sunday), with Previous/Next Week navigation.
