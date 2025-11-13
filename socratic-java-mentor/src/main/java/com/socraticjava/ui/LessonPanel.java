package com.socraticjava.ui;

import com.socraticjava.model.*;
import com.socraticjava.service.CodeCompiler;
import com.socraticjava.service.TestRunner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

/**
 * Panel for displaying lesson content and challenges
 */
public class LessonPanel {

    private final Lesson lesson;
    private final Progress progress;
    private final CodeCompiler codeCompiler;
    private final TestRunner testRunner;
    private final Consumer<String> onChallengeCompleted;
    private final Runnable onNextLesson;

    private VBox mainView;
    private int currentChallengeIndex = 0;

    public LessonPanel(Lesson lesson, Progress progress, CodeCompiler codeCompiler,
                       TestRunner testRunner, Consumer<String> onChallengeCompleted,
                       Runnable onNextLesson) {
        this.lesson = lesson;
        this.progress = progress;
        this.codeCompiler = codeCompiler;
        this.testRunner = testRunner;
        this.onChallengeCompleted = onChallengeCompleted;
        this.onNextLesson = onNextLesson;

        initializeView();
    }

    private void initializeView() {
        mainView = new VBox(20);
        mainView.setPadding(new Insets(20));

        // Lesson title
        Label titleLabel = new Label(lesson.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setWrapText(true);

        // Lesson content sections
        VBox contentBox = new VBox(15);
        for (Lesson.ContentSection section : lesson.getContentSections()) {
            contentBox.getChildren().add(createContentSection(section));
        }

        // Challenges section
        VBox challengesBox = createChallengesSection();

        // Scroll pane for content
        ScrollPane scrollPane = new ScrollPane();
        VBox scrollContent = new VBox(20, titleLabel, new Separator(), contentBox, challengesBox);
        scrollContent.setPadding(new Insets(10));
        scrollPane.setContent(scrollContent);
        scrollPane.setFitToWidth(true);

        mainView.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    private VBox createContentSection(Lesson.ContentSection section) {
        VBox sectionBox = new VBox(10);
        sectionBox.setPadding(new Insets(10));

        // Section heading
        Label headingLabel = new Label(section.getHeading());
        headingLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Section content
        TextArea contentArea = new TextArea(section.getContent());
        contentArea.setWrapText(true);
        contentArea.setEditable(false);
        contentArea.setPrefRowCount(10);

        // Style based on content type
        String style = switch (section.getType()) {
            case ANALOGY -> "-fx-background-color: #e8f5e9; -fx-border-color: #4caf50; -fx-border-width: 2px;";
            case KEY_POINT -> "-fx-background-color: #fff9c4; -fx-border-color: #fbc02d; -fx-border-width: 2px;";
            case EXAMPLE -> "-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 2px;";
            case WARNING -> "-fx-background-color: #ffebee; -fx-border-color: #f44336; -fx-border-width: 2px;";
            default -> "-fx-background-color: #f5f5f5; -fx-border-color: #9e9e9e; -fx-border-width: 1px;";
        };

        sectionBox.setStyle(style);
        sectionBox.getChildren().addAll(headingLabel, contentArea);

        return sectionBox;
    }

    private VBox createChallengesSection() {
        VBox challengesBox = new VBox(15);
        challengesBox.setPadding(new Insets(20, 0, 0, 0));

        if (lesson.getChallenges().isEmpty()) {
            Label noChallenges = new Label("No challenges for this lesson.");
            challengesBox.getChildren().add(noChallenges);
            return challengesBox;
        }

        Label challengesTitle = new Label("🎯 Challenges");
        challengesTitle.setFont(Font.font("System", FontWeight.BOLD, 22));

        challengesBox.getChildren().add(challengesTitle);

        // Display all challenges
        for (int i = 0; i < lesson.getChallenges().size(); i++) {
            Challenge challenge = lesson.getChallenges().get(i);
            VBox challengeBox = createChallengeBox(challenge, i);
            challengesBox.getChildren().add(challengeBox);
        }

        return challengesBox;
    }

    private VBox createChallengeBox(Challenge challenge, int index) {
        VBox challengeBox = new VBox(10);
        challengeBox.setPadding(new Insets(15));
        challengeBox.setStyle("-fx-border-color: #3f51b5; -fx-border-width: 2px; -fx-background-color: #fafafa;");

        // Challenge header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label challengeTitle = new Label("Challenge " + (index + 1) + ": " + challenge.getTitle());
        challengeTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Completion status
        boolean isCompleted = progress.isChallengeCompleted(lesson.getId(), challenge.getId());
        if (isCompleted) {
            Label completedLabel = new Label("✓ Completed");
            completedLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            header.getChildren().addAll(challengeTitle, completedLabel);
        } else {
            header.getChildren().add(challengeTitle);
        }

        challengeBox.getChildren().add(header);

        // Challenge description
        TextArea descriptionArea = new TextArea(challenge.getDescription());
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setPrefRowCount(3);
        challengeBox.getChildren().add(descriptionArea);

        // Type-specific UI
        if (challenge.getType() == ChallengeType.MULTIPLE_CHOICE) {
            VBox mcBox = createMultipleChoiceUI(challenge);
            challengeBox.getChildren().add(mcBox);
        } else if (challenge.getType() == ChallengeType.FREE_CODING ||
                   challenge.getType() == ChallengeType.CODE_COMPLETION) {
            VBox codeBox = createCodingChallengeUI(challenge);
            challengeBox.getChildren().add(codeBox);
        }

        return challengeBox;
    }

    private VBox createMultipleChoiceUI(Challenge challenge) {
        VBox mcBox = new VBox(10);

        ToggleGroup group = new ToggleGroup();

        for (String option : challenge.getMultipleChoiceOptions()) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(group);
            radioButton.setWrapText(true);
            mcBox.getChildren().add(radioButton);
        }

        Button submitButton = new Button("Submit Answer");
        Label resultLabel = new Label();

        submitButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected == null) {
                resultLabel.setText("Please select an answer!");
                resultLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            String answer = selected.getText().substring(0, 1); // Get A, B, C, or D
            if (answer.equals(challenge.getCorrectAnswer())) {
                resultLabel.setText("✓ Correct! Well done!");
                resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                onChallengeCompleted.accept(challenge.getId());
                submitButton.setDisable(true);
            } else {
                resultLabel.setText("✗ Not quite. Try again!");
                resultLabel.setStyle("-fx-text-fill: red;");
            }
        });

        mcBox.getChildren().addAll(submitButton, resultLabel);
        return mcBox;
    }

    private VBox createCodingChallengeUI(Challenge challenge) {
        VBox codeBox = new VBox(10);

        Label instructionLabel = new Label("Write your code below:");
        instructionLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Code editor (simple TextArea for now)
        TextArea codeEditor = new TextArea(challenge.getStarterCode());
        codeEditor.setFont(Font.font("Monospaced", 14));
        codeEditor.setPrefRowCount(15);
        codeEditor.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: #f8f8f2;");

        // Output console
        TextArea outputConsole = new TextArea();
        outputConsole.setEditable(false);
        outputConsole.setPrefRowCount(8);
        outputConsole.setPromptText("Test results will appear here...");

        // Buttons
        HBox buttonBox = new HBox(10);
        Button runTestsButton = new Button("Run Tests");
        Button submitButton = new Button("Submit Solution");
        submitButton.setDisable(true);

        runTestsButton.setOnAction(e -> {
            String code = codeEditor.getText();
            String className = extractClassName(code);

            // Compile
            outputConsole.setText("Compiling...\n");
            CodeCompiler.CompilationResult compResult = codeCompiler.compile(className, code);

            if (!compResult.isSuccess()) {
                outputConsole.appendText("Compilation failed:\n" + compResult.getErrors());
                return;
            }

            outputConsole.appendText("Compilation successful!\n\nRunning tests...\n");

            // Run tests
            TestRunner.TestSuiteResult testResult = testRunner.runTests(
                compResult.getCompiledClasses(),
                className,
                challenge.getTestCases()
            );

            // Display results
            outputConsole.appendText("\nTest Results:\n");
            outputConsole.appendText("================\n");
            for (TestRunner.TestResult result : testResult.getTestResults()) {
                String status = result.isPassed() ? "✓ PASS" : "✗ FAIL";
                outputConsole.appendText(status + ": " + result.getTestCase().getDescription() + "\n");
                if (!result.isPassed()) {
                    outputConsole.appendText("  " + result.getErrorMessage() + "\n");
                }
            }
            outputConsole.appendText("\nTotal: " + testResult.getPassedCount() + "/" +
                                    testResult.getTotalCount() + " passed\n");

            if (testResult.allPassed()) {
                outputConsole.appendText("\n🎉 All tests passed! Click 'Submit Solution' to complete this challenge.\n");
                submitButton.setDisable(false);
            }
        });

        submitButton.setOnAction(e -> {
            outputConsole.appendText("\n✓ Challenge completed!\n");
            onChallengeCompleted.accept(challenge.getId());
            runTestsButton.setDisable(true);
            submitButton.setDisable(true);
            codeEditor.setEditable(false);
        });

        buttonBox.getChildren().addAll(runTestsButton, submitButton);

        codeBox.getChildren().addAll(instructionLabel, codeEditor, buttonBox,
                                     new Label("Output:"), outputConsole);
        return codeBox;
    }

    private String extractClassName(String code) {
        // Simple regex to extract class name
        String[] lines = code.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("public class ")) {
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length - 1; i++) {
                    if (parts[i].equals("class")) {
                        return parts[i + 1].replace("{", "").trim();
                    }
                }
            }
        }
        return "UnknownClass";
    }

    public VBox getView() {
        return mainView;
    }
}
