package com.socraticjava;

import com.socraticjava.model.Epoch;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.Progress;
import com.socraticjava.service.LessonService;
import com.socraticjava.service.ProgressTracker;
import com.socraticjava.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the Socratic Java Mentor application
 */
public class SocraticJavaApp extends Application {

    private LessonService lessonService;
    private ProgressTracker progressTracker;
    private Progress progress;

    @Override
    public void start(Stage primaryStage) {
        // Initialize services
        lessonService = new LessonService();
        progressTracker = new ProgressTracker();

        // Load or create progress
        progress = progressTracker.loadProgress();

        // Determine starting lesson
        Lesson startingLesson = determineStartingLesson();

        // Create and show main window
        MainWindow mainWindow = new MainWindow(
            primaryStage,
            lessonService,
            progressTracker,
            progress,
            startingLesson
        );

        mainWindow.show();
    }

    /**
     * Determines which lesson to show on startup
     * If there's saved progress, resume from there
     * Otherwise, start from the first lesson
     */
    private Lesson determineStartingLesson() {
        if (progress.getCurrentLessonId() != null && progress.getCurrentEpochId() != null) {
            // Resume from saved progress
            Lesson savedLesson = lessonService.getLesson(
                progress.getCurrentEpochId(),
                progress.getCurrentLessonId()
            );
            if (savedLesson != null) {
                return savedLesson;
            }
        }

        // Start from the first lesson
        Epoch firstEpoch = lessonService.getAllEpochs().get(0);
        return lessonService.getFirstLesson(firstEpoch.getId());
    }

    /**
     * Save progress on shutdown
     */
    @Override
    public void stop() {
        progressTracker.saveProgress(progress);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
