package com.socraticjava.ui;

import com.socraticjava.model.*;
import com.socraticjava.service.CodeCompiler;
import com.socraticjava.service.LessonService;
import com.socraticjava.service.ProgressTracker;
import com.socraticjava.service.TestRunner;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Main application window
 */
public class MainWindow {

    private final Stage stage;
    private final LessonService lessonService;
    private final ProgressTracker progressTracker;
    private final Progress progress;
    private final CodeCompiler codeCompiler;
    private final TestRunner testRunner;

    private TreeView<String> lessonTree;
    private BorderPane contentArea;
    private Label statusLabel;

    private Lesson currentLesson;
    private String currentEpochId;

    public MainWindow(Stage stage, LessonService lessonService, ProgressTracker progressTracker,
                      Progress progress, Lesson startingLesson) {
        this.stage = stage;
        this.lessonService = lessonService;
        this.progressTracker = progressTracker;
        this.progress = progress;
        this.codeCompiler = new CodeCompiler();
        this.testRunner = new TestRunner();
        this.currentLesson = startingLesson;

        // Determine current epoch from the lesson
        if (startingLesson != null) {
            this.currentEpochId = extractEpochId(startingLesson.getId());
        }

        initializeUI();
    }

    private void initializeUI() {
        BorderPane root = new BorderPane();

        // Top: Title and navigation
        VBox top = createTopSection();
        root.setTop(top);

        // Left: Lesson tree
        lessonTree = createLessonTree();
        ScrollPane treeScroll = new ScrollPane(lessonTree);
        treeScroll.setFitToWidth(true);
        treeScroll.setPrefWidth(250);

        // Center: Content area
        contentArea = new BorderPane();
        contentArea.setPadding(new Insets(20));

        // Split pane for tree and content
        SplitPane splitPane = new SplitPane(treeScroll, contentArea);
        splitPane.setDividerPositions(0.25);
        root.setCenter(splitPane);

        // Bottom: Status bar
        statusLabel = new Label("Ready");
        statusLabel.setPadding(new Insets(5));
        root.setBottom(statusLabel);

        // Load the current lesson
        if (currentLesson != null) {
            loadLesson(currentLesson);
        }

        // Create scene
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Socratic Java Mentor - Learn Java from First Principles");
    }

    private VBox createTopSection() {
        VBox top = new VBox(10);
        top.setPadding(new Insets(10));
        top.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("🎓 Socratic Java Mentor");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Interactive Java Learning Platform");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #ecf0f1;");

        top.getChildren().addAll(title, subtitle);
        return top;
    }

    private TreeView<String> createLessonTree() {
        TreeItem<String> root = new TreeItem<>("Course");
        root.setExpanded(true);

        List<Epoch> epochs = lessonService.getAllEpochs();
        for (Epoch epoch : epochs) {
            TreeItem<String> epochItem = new TreeItem<>(epoch.getTitle());
            epochItem.setExpanded(false);

            for (Lesson lesson : epoch.getLessons()) {
                String displayText = lesson.getTitle();
                if (progress.isLessonCompleted(lesson.getId())) {
                    displayText = "✓ " + displayText;
                }
                TreeItem<String> lessonItem = new TreeItem<>(displayText);
                lessonItem.setValue(lesson.getId()); // Store lesson ID as value
                epochItem.getChildren().add(lessonItem);
            }

            root.getChildren().add(epochItem);
        }

        TreeView<String> tree = new TreeView<>(root);
        tree.setShowRoot(false);

        // Handle lesson selection
        tree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.isLeaf()) {
                String lessonId = newVal.getValue();
                // Extract epoch ID from lesson ID (format: epoch-X-lesson-Y)
                String epochId = extractEpochId(lessonId);
                Lesson lesson = lessonService.getLesson(epochId, lessonId);
                if (lesson != null) {
                    loadLesson(lesson);
                }
            }
        });

        return tree;
    }

    private String extractEpochId(String lessonId) {
        // Extract epoch ID from lesson ID (format: epoch-X-lesson-Y)
        // Example: "epoch-0-lesson-1" -> "epoch-0"
        if (lessonId.contains("-lesson-")) {
            return lessonId.substring(0, lessonId.indexOf("-lesson-"));
        }
        return "epoch-0"; // Default
    }

    private void loadLesson(Lesson lesson) {
        this.currentLesson = lesson;
        this.currentEpochId = extractEpochId(lesson.getId());

        // Update progress
        progress.setCurrentEpochId(currentEpochId);
        progress.setCurrentLessonId(lesson.getId());

        // Create lesson panel
        LessonPanel lessonPanel = new LessonPanel(
            lesson,
            progress,
            codeCompiler,
            testRunner,
            this::onChallengeCompleted,
            this::onNextLesson
        );

        contentArea.setCenter(lessonPanel.getView());
        statusLabel.setText("Lesson: " + lesson.getTitle());
    }

    private void onChallengeCompleted(String challengeId) {
        // Mark challenge as completed
        progress.completeChallenge(currentLesson.getId(), challengeId);

        // Check if all challenges are completed
        long completedCount = currentLesson.getChallenges().stream()
            .filter(c -> progress.isChallengeCompleted(currentLesson.getId(), c.getId()))
            .count();

        if (completedCount == currentLesson.getChallenges().size()) {
            progress.completeLesson(
                currentLesson.getId(),
                (int) completedCount,
                currentLesson.getChallenges().size()
            );
            showLessonCompletedDialog();
        }

        // Save progress
        progressTracker.saveProgress(progress);
    }

    private void onNextLesson() {
        Lesson nextLesson = lessonService.getNextLesson(currentEpochId, currentLesson.getId());
        if (nextLesson != null) {
            loadLesson(nextLesson);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Epoch Complete!");
            alert.setHeaderText("You've completed all lessons in this epoch!");
            alert.setContentText("Congratulations! More lessons coming soon.");
            alert.showAndWait();
        }
    }

    private void showLessonCompletedDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lesson Complete! 🎉");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("You've completed " + currentLesson.getTitle() + "!\n\nReady for the next lesson?");
        alert.showAndWait();
    }

    public void show() {
        stage.show();
    }
}
