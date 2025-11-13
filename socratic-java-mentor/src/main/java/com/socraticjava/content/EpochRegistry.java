package com.socraticjava.content;

import com.socraticjava.content.epoch0.Lesson01Content;
import com.socraticjava.content.epoch0.Lesson02Content;
import com.socraticjava.content.epoch0.Lesson03Content;
import com.socraticjava.content.epoch0.Lesson04Content;
import com.socraticjava.model.Epoch;

import java.util.ArrayList;
import java.util.List;

/**
 * Central registry of all epochs and lessons
 * This is where all course content is loaded
 */
public class EpochRegistry {

    private static List<Epoch> epochs;

    /**
     * Returns all available epochs in order
     */
    public static List<Epoch> getAllEpochs() {
        if (epochs == null) {
            initializeEpochs();
        }
        return new ArrayList<>(epochs);
    }

    /**
     * Gets a specific epoch by ID
     */
    public static Epoch getEpochById(String epochId) {
        return getAllEpochs().stream()
            .filter(e -> e.getId().equals(epochId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Initialize all epochs (lazy loading)
     */
    private static void initializeEpochs() {
        epochs = new ArrayList<>();
        epochs.add(createEpoch0());
        // Future epochs will be added here as they're built
        // epochs.add(createEpoch1());
        // epochs.add(createEpoch2());
        // ... etc
    }

    /**
     * Creates Epoch 0: The Foundation
     */
    private static Epoch createEpoch0() {
        Epoch epoch = new Epoch(
            "epoch-0",
            "Epoch 0: The Foundation",
            "What is a program? What is Java? Why does any of this matter?",
            5
        );

        // Add lessons
        epoch.addLesson(Lesson01Content.create());
        epoch.addLesson(Lesson02Content.create());
        epoch.addLesson(Lesson03Content.create());
        epoch.addLesson(Lesson04Content.create());

        return epoch;
    }

    /**
     * Placeholder for future epochs
     */
    private static Epoch createEpoch1() {
        Epoch epoch = new Epoch(
            "epoch-1",
            "Epoch 1: The Bare Essentials",
            "How do I make the computer remember things and make decisions?",
            15
        );
        // Lessons will be added here
        return epoch;
    }
}
