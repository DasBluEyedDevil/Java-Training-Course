# Architecture Design Document

## Application: Socratic Java Mentor (Standalone Desktop Learning Platform)

### Technology Stack
- **Java**: 17+ (LTS)
- **UI Framework**: JavaFX 21
- **Build Tool**: Maven
- **Testing**: JUnit 5
- **Code Compilation**: Java Compiler API (javax.tools)
- **Persistence**: JSON (for progress tracking)

---

## Architecture Overview

### MVC Pattern
```
┌─────────────────────────────────────────────────────┐
│                   JavaFX UI Layer                    │
│  (MainWindow, LessonPanel, CodeEditor, Console)     │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                Controller Layer                      │
│     (LessonController, NavigationController)        │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                 Service Layer                        │
│  (LessonService, CodeCompiler, TestRunner,          │
│   ProgressTracker)                                   │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                  Model Layer                         │
│  (Lesson, Challenge, TestCase, Progress, Epoch)     │
└──────────────────────────────────────────────────────┘
```

---

## Core Components

### 1. Model Layer

#### `Epoch.java`
- Represents a learning epoch (e.g., "The Foundation")
- Contains list of lessons
- Metadata: title, description, estimated hours

#### `Lesson.java`
- Lesson content (theory sections)
- List of challenges
- Lesson type: CONCEPTUAL, CODING, HYBRID

#### `Challenge.java`
- Challenge description
- Challenge type: MULTIPLE_CHOICE, CODE_COMPLETION, FREE_CODING
- For code challenges: starter code template, test cases

#### `TestCase.java`
- Input parameters
- Expected output
- Validation logic

#### `Progress.java`
- User progress data
- Completed lessons, challenge scores
- Timestamps, session data

---

### 2. Service Layer

#### `LessonService.java`
- Loads lesson content from embedded registry
- Retrieves lessons by epoch/ID
- Manages lesson navigation

#### `CodeCompiler.java`
- Uses Java Compiler API to compile student code in-memory
- Handles syntax errors, returns compilation results
- Sandboxed execution (no file system access)

#### `TestRunner.java`
- Executes compiled code against test cases
- Captures output (stdout, return values)
- Compares with expected results
- Returns pass/fail with detailed feedback

#### `ProgressTracker.java`
- Persists progress to JSON file (~/.socratic-java/progress.json)
- Loads previous progress on startup
- Updates completion status

---

### 3. UI Layer (JavaFX)

#### `MainWindow.java`
- Main application window (BorderPane)
- Top: Navigation bar (epoch selector, progress indicator)
- Left: Lesson tree view (epoch → lessons)
- Center: Content area (switches between lesson view and challenge view)
- Bottom: Status bar

#### `LessonPanel.java`
- Displays lesson content (rich text with formatting)
- "Next Challenge" button to transition to coding

#### `ChallengePanel.java`
- Top: Challenge description
- Middle: Code editor (textarea with syntax highlighting via RichTextFX)
- Bottom: "Run Tests" button, "Submit" button
- Right: Output console (test results, compilation errors)

#### `ProgressDashboard.java`
- Visual progress tracker (epoch completion %, time spent)

---

### 4. Content Layer (Embedded)

#### `EpochRegistry.java`
- Central registry that instantiates all epochs
- Epochs contain lessons with embedded content
- Sample structure:

```java
public class EpochRegistry {
    public static List<Epoch> getAllEpochs() {
        return List.of(
            createEpoch0(),
            createEpoch1(),
            // ...
        );
    }

    private static Epoch createEpoch0() {
        Epoch epoch = new Epoch("Epoch 0", "The Foundation");
        epoch.addLesson(Epoch0Lesson1.create());
        epoch.addLesson(Epoch0Lesson2.create());
        return epoch;
    }
}
```

#### `content/epoch0/Lesson01.java`
- Static factory method creates Lesson object
- Embeds all text content, challenges, test cases

---

## Data Flow: Student Completes a Coding Challenge

1. Student reads lesson in `LessonPanel`
2. Clicks "Start Challenge" → switches to `ChallengePanel`
3. Student writes code in editor
4. Student clicks "Run Tests"
5. `CodeCompiler.compile(studentCode)` → compiles in-memory
6. If compilation fails → display errors in console
7. If success → `TestRunner.runTests(compiledClass, testCases)`
8. Test runner executes each test case, captures results
9. Results displayed in console (✓/✗ for each test)
10. If all tests pass → "Challenge Complete!" + `ProgressTracker.markComplete()`
11. Student proceeds to next lesson

---

## File Structure

```
socratic-java-mentor/
├── pom.xml
├── src/main/java/com/socraticjava/
│   ├── SocraticJavaApp.java
│   ├── model/
│   │   ├── Epoch.java
│   │   ├── Lesson.java
│   │   ├── Challenge.java
│   │   ├── TestCase.java
│   │   ├── Progress.java
│   │   └── ChallengeType.java (enum)
│   ├── service/
│   │   ├── LessonService.java
│   │   ├── CodeCompiler.java
│   │   ├── TestRunner.java
│   │   └── ProgressTracker.java
│   ├── controller/
│   │   ├── LessonController.java
│   │   └── NavigationController.java
│   ├── ui/
│   │   ├── MainWindow.java
│   │   ├── LessonPanel.java
│   │   ├── ChallengePanel.java
│   │   ├── CodeEditor.java
│   │   └── ProgressDashboard.java
│   └── content/
│       ├── EpochRegistry.java
│       └── epoch0/
│           ├── Lesson01Content.java
│           └── Lesson02Content.java
├── src/main/resources/
│   ├── styles/
│   │   └── app.css
│   └── icons/
└── README.md
```

---

## Phase 1 Implementation Plan

### Step 1: Maven Setup
- Create `pom.xml` with JavaFX, JUnit 5, Gson dependencies

### Step 2: Core Models
- Build `Lesson`, `Challenge`, `TestCase`, `Epoch`, `Progress` classes

### Step 3: Services (MVP)
- Implement `CodeCompiler` (Java Compiler API)
- Implement `TestRunner` (basic JUnit-style assertions)
- Implement `ProgressTracker` (JSON serialization)

### Step 4: UI (Basic)
- Create `MainWindow` with basic layout
- Create `LessonPanel` (simple text display)
- Create `ChallengePanel` (textarea + button + output)

### Step 5: Content (Sample)
- Create Epoch 0, Lesson 1 (conceptual - "What is a program?")
- Create Epoch 0, Lesson 2 (first coding challenge - "Hello World")

### Step 6: Integration
- Wire everything together
- Test end-to-end flow

### Step 7: Packaging
- Maven Assembly plugin to create executable JAR
- Launch scripts for Windows (.bat) and Unix (.sh)

---

## Success Criteria

✅ Student can launch app with `java -jar socratic-java-mentor.jar`
✅ Student sees lesson tree, can navigate lessons
✅ Student reads theory content
✅ Student writes code in editor
✅ Student runs tests, sees pass/fail results
✅ Progress is saved and restored
✅ At least 2 lessons fully functional

---

This architecture supports the full curriculum (Epoch 0-9) while keeping the platform extensible.
