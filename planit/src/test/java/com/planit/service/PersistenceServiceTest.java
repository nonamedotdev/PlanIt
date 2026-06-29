package com.planit.service;

import com.planit.model.Event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PersistenceServiceTest {

    @Test
    void savedEventCanBeLoadedAgain(@TempDir Path tempDir) {
        PersistenceService service = new PersistenceService(tempDir.toString());
        Event event = new Event("e1", "Tech Summit", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 18, 0), 1000.0, "Demo event");

        service.save(event);
        Event loaded = service.load("e1");

        assertEquals("Tech Summit", loaded.getTitle());
    }

    @Test
    void deletedEventCanNoLongerBeLoaded(@TempDir Path tempDir) {
        PersistenceService service = new PersistenceService(tempDir.toString());
        Event event = new Event("e2", "Workshop Day", LocalDateTime.of(2026, 6, 1, 9, 0),
                LocalDateTime.of(2026, 6, 1, 12, 0), 500.0, "Demo event");

        service.save(event);
        service.delete("e2");

        assertNull(service.load("e2"));
    }
}
