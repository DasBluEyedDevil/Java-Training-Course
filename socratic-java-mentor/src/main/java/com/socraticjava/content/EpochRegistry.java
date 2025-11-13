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
        epochs.add(createEpoch1());
        epochs.add(createEpoch2());
        epochs.add(createEpoch3());
        epochs.add(createEpoch4());
        epochs.add(createEpoch5());
        epochs.add(createEpoch6());
        epochs.add(createEpoch7());
        epochs.add(createEpoch8());
        epochs.add(createEpoch9());
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
     * Creates Epoch 1: The Bare Essentials
     */
    private static Epoch createEpoch1() {
        Epoch epoch = new Epoch(
            "epoch-1",
            "Epoch 1: The Bare Essentials",
            "Data types, operators, loops, and methods",
            8
        );

        // Add lessons using fully qualified class names
        epoch.addLesson(com.socraticjava.content.epoch1.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch1.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch1.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch1.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch1.Lesson05Content.create());
        epoch.addLesson(com.socraticjava.content.epoch1.Lesson06Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 2: Thinking in Objects
     */
    private static Epoch createEpoch2() {
        Epoch epoch = new Epoch(
            "epoch-2",
            "Epoch 2: Thinking in Objects",
            "Classes, objects, and object-oriented programming",
            10
        );

        epoch.addLesson(com.socraticjava.content.epoch2.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch2.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch2.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch2.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch2.Lesson05Content.create());
        epoch.addLesson(com.socraticjava.content.epoch2.Lesson06Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 3: Building Your Toolkit
     */
    private static Epoch createEpoch3() {
        Epoch epoch = new Epoch(
            "epoch-3",
            "Epoch 3: Building Your Toolkit",
            "Arrays, collections, and data structures",
            8
        );

        epoch.addLesson(com.socraticjava.content.epoch3.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch3.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch3.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch3.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch3.Lesson05Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 4: The Professional's Toolbox
     */
    private static Epoch createEpoch4() {
        Epoch epoch = new Epoch(
            "epoch-4",
            "Epoch 4: The Professional's Toolbox",
            "Testing, build tools, and professional practices",
            6
        );

        epoch.addLesson(com.socraticjava.content.epoch4.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch4.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch4.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch4.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch4.Lesson05Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 5: The Database
     */
    private static Epoch createEpoch5() {
        Epoch epoch = new Epoch(
            "epoch-5",
            "Epoch 5: The Database",
            "SQL, databases, and persistence",
            10
        );

        epoch.addLesson(com.socraticjava.content.epoch5.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch5.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch5.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch5.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch5.Lesson05Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 6: The Connected Web
     */
    private static Epoch createEpoch6() {
        Epoch epoch = new Epoch(
            "epoch-6",
            "Epoch 6: The Connected Web",
            "HTTP, REST APIs, and web services",
            8
        );

        epoch.addLesson(com.socraticjava.content.epoch6.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch6.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch6.Lesson03Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 7: The Modern Framework
     */
    private static Epoch createEpoch7() {
        Epoch epoch = new Epoch(
            "epoch-7",
            "Epoch 7: The Modern Framework",
            "Spring Boot and enterprise Java",
            15
        );

        epoch.addLesson(com.socraticjava.content.epoch7.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch7.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch7.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch7.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch7.Lesson05Content.create());
        epoch.addLesson(com.socraticjava.content.epoch7.Lesson06Content.create());
        epoch.addLesson(com.socraticjava.content.epoch7.Lesson07Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 8: The Frontend Connection
     */
    private static Epoch createEpoch8() {
        Epoch epoch = new Epoch(
            "epoch-8",
            "Epoch 8: The Frontend Connection",
            "Full-stack integration and deployment",
            6
        );

        epoch.addLesson(com.socraticjava.content.epoch8.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch8.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch8.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch8.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch8.Lesson05Content.create());
        epoch.addLesson(com.socraticjava.content.epoch8.Lesson06Content.create());

        return epoch;
    }

    /**
     * Creates Epoch 9: The Capstone Journey
     */
    private static Epoch createEpoch9() {
        Epoch epoch = new Epoch(
            "epoch-9",
            "Epoch 9: The Capstone Journey",
            "Build your portfolio project",
            20
        );

        epoch.addLesson(com.socraticjava.content.epoch9.Lesson01Content.create());
        epoch.addLesson(com.socraticjava.content.epoch9.Lesson02Content.create());
        epoch.addLesson(com.socraticjava.content.epoch9.Lesson03Content.create());
        epoch.addLesson(com.socraticjava.content.epoch9.Lesson04Content.create());
        epoch.addLesson(com.socraticjava.content.epoch9.Lesson05Content.create());
        epoch.addLesson(com.socraticjava.content.epoch9.Lesson06Content.create());

        return epoch;
    }
}
