package com.planit.service;

import com.planit.model.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages permanent data storage and loading of events using plain
 * Java Serialization. Each event is stored as one file "<id>.ser"
 * inside the configured directory.
 */
public class PersistenceService {

    private static final Logger LOGGER = Logger.getLogger(PersistenceService.class.getName());

    private String filePath;

    public PersistenceService(String filePath) {
        this.filePath = filePath;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void save(Event e) {
        File file = new File(filePath, e.getId() + ".ser");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(e);
            LOGGER.info("Saved event " + e.getId() + " to " + file.getPath());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not save event " + e.getId(), ex);
        }
    }

    public Event load(String id) {
        File file = new File(filePath, id + ".ser");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Event) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Could not load event " + id, ex);
            return null;
        }
    }

    public List<Event> loadAll() {
        List<Event> events = new ArrayList<>();
        File dir = new File(filePath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
        if (files == null) {
            return events;
        }
        for (File file : files) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                events.add((Event) in.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                LOGGER.log(Level.WARNING, "Could not load file " + file.getName(), ex);
            }
        }
        LOGGER.info("Loaded " + events.size() + " event(s) from " + filePath);
        return events;
    }

    public void delete(String id) {
        File file = new File(filePath, id + ".ser");
        if (file.exists() && file.delete()) {
            LOGGER.info("Deleted event " + id);
        }
    }
}
