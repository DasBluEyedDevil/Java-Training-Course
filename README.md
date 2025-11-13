# Java Training Course
## From First Principles to Full-Stack Development

**An Interactive, Self-Contained Learning Platform**

---

## 🎯 What Is This?

This repository contains **Socratic Java Mentor**, a complete Java learning platform designed to take you from absolute beginner to job-ready full-stack developer.

Unlike traditional tutorials, this is a **standalone desktop application** that provides:

- ✅ **Interactive Learning**: Read lessons, write code, get instant feedback
- ✅ **Automated Testing**: Your code is compiled and tested automatically
- ✅ **Progress Tracking**: Pick up where you left off
- ✅ **Concept-First Approach**: Understand the "why" before the "what"
- ✅ **No Internet Required**: Everything runs locally

---

## 📚 Course Curriculum

### **Epoch 0: The Foundation** (✅ Complete - 4 Lessons)
*What is a program? What is Java? Why does any of this matter?*
- ✅ Lesson 0.1: What is a Computer Program? (Conceptual + Quiz)
- ✅ Lesson 0.2: Your First Java Program (2 Coding Challenges)
- ✅ Lesson 0.3: Understanding Variables (3 Challenges: Quiz + 2 Coding)
- ✅ Lesson 0.4: Making Decisions with If/Else (3 Challenges: Quiz + 2 Coding)

### **Epoch 1: The Bare Essentials** (✅ Complete - 5 Lessons)
*Data types, operators, loops, and methods*
- ✅ Lesson 1.1: Data Types in Depth (4 Challenges: 2 Quizzes + 2 Coding)
- ✅ Lesson 1.2: Operators and Expressions (4 Challenges: 2 Quizzes + 2 Coding)
- ✅ Lesson 1.3: While Loops - Mastering Repetition (4 Challenges: 2 Quizzes + 2 Coding)
- ✅ Lesson 1.4: For Loops - Counting Made Easy (4 Challenges: 1 Quiz + 3 Coding)
- ✅ Lesson 1.5: Introduction to Methods (4 Challenges: 1 Quiz + 3 Coding)

### **Epoch 2: Thinking in Objects** (✅ Available - 2 Lessons)
*Object-oriented programming fundamentals*
- ✅ Lesson 2.1: Classes and Objects - The Blueprint
- ✅ Lesson 2.2: Constructors - Better Object Creation

### **Epoch 3: Building Your Toolkit** (✅ Available - 1 Lesson)
*Arrays and data structures*
- ✅ Lesson 3.1: Arrays - Storing Multiple Values

### **Epoch 4: The Professional's Toolbox** (✅ Available - 1 Lesson)
*Testing and professional practices*
- ✅ Lesson 4.1: Introduction to Testing

### **Epoch 5: The Database** (✅ Available - 1 Lesson)
*SQL and persistent storage*
- ✅ Lesson 5.1: Why Do We Need Databases?

### **Epoch 6: The Connected Web** (✅ Available - 1 Lesson)
*HTTP and REST APIs*
- ✅ Lesson 6.1: How Does the Web Actually Work?

### **Epoch 7: The Modern Framework** (✅ Available - 1 Lesson)
*Spring Boot framework*
- ✅ Lesson 7.1: Introduction to Spring Boot

### **Epoch 8: The Frontend Connection** (✅ Available - 1 Lesson)
*Full-stack integration*
- ✅ Lesson 8.1: Connecting Frontend to Your API

### **Epoch 9: The Capstone Journey** (✅ Available - 1 Lesson)
*Portfolio project*
- ✅ Lesson 9.1: Your Capstone Project

Full curriculum map available in: `COURSE_INDEX.md`

---

## 🚀 Quick Start

### Prerequisites

- **Java Development Kit (JDK) 17 or higher**
  - Download: [Adoptium JDK](https://adoptium.net/)
  - Verify: `java -version`
- **Maven** (for building from source)
  - Download: [Apache Maven](https://maven.apache.org/)

### Running the Application

```bash
# Navigate to the application directory
cd socratic-java-mentor

# Option 1: Run with Maven (development)
mvn clean javafx:run

# Option 2: Build and run the JAR
mvn clean package
java -jar target/socratic-java-mentor-shaded.jar

# Option 3: Use platform-specific launcher
./launch-unix.sh         # Linux/Mac
launch-windows.bat       # Windows
```

---

## 📁 Repository Structure

```
Java-Training-Course/
├── README.md                    # This file
├── COURSE_INDEX.md              # Full curriculum overview
├── ARCHITECTURE.md              # Technical architecture
│
├── socratic-java-mentor/        # The main application
│   ├── src/main/java/com/socraticjava/
│   │   ├── SocraticJavaApp.java      # Main entry point
│   │   ├── model/                     # Data models
│   │   ├── service/                   # Compiler, test runner, progress
│   │   ├── ui/                        # JavaFX interface
│   │   └── content/                   # Embedded lessons
│   ├── pom.xml                        # Maven configuration
│   ├── README.md                      # Application README
│   └── launch-*.sh/bat                # Launch scripts
│
└── Epoch-0-Foundation/          # Legacy markdown lessons
    └── (For reference only)
```

---

## 🎓 How It Works

### 1. Read the Lesson
Each lesson presents concepts through:
- Real-world analogies
- Clear explanations
- Visual examples

### 2. Complete Challenges
Practice what you learned:
- Multiple choice questions
- Coding exercises
- Real-time compilation and testing

### 3. Get Instant Feedback
The app:
- Compiles your Java code in-memory
- Runs automated test cases
- Shows you exactly what went wrong

### 4. Track Your Progress
- Completed lessons are saved
- Resume anytime from where you left off
- Progress stored locally in `~/.socratic-java/`

---

## 🛠️ Technology Stack

**Application:**
- Java 17
- JavaFX 21 (UI framework)
- RichTextFX (code editor)
- Java Compiler API (in-memory compilation)
- Gson (progress persistence)

**Build:**
- Maven 3.8+
- Maven Assembly Plugin (executable JAR)

---

## 🔧 Building from Source

### Full Build
```bash
cd socratic-java-mentor
mvn clean package
```

This creates:
- `target/socratic-java-mentor-1.0.0.jar` - Standard JAR
- `target/socratic-java-mentor-shaded.jar` - Executable with dependencies

### Development Mode
```bash
mvn clean compile    # Compile only
mvn clean test       # Run tests
mvn javafx:run       # Run application
```

---

## 📖 Learning Philosophy

This course follows three core principles:

### 1. Concept First, Jargon Last
You'll never see a technical term without first understanding the problem it solves.

### 2. The Feynman Technique
Everything is explained simply, with analogies a 10-year-old could understand.

### 3. Active Learning
You learn by doing. Every lesson includes hands-on challenges with automated feedback.

---

## 💾 Progress Data

Your progress is saved automatically to:
- **Windows**: `C:\Users\<YourName>\.socratic-java\progress.json`
- **Linux/Mac**: `~/.socratic-java/progress.json`

To reset progress, delete this file.

---

## 🐛 Troubleshooting

### "Java compiler not available"
You need the **JDK** (not JRE). Download from [Adoptium](https://adoptium.net/).

### Application won't start
Verify Java version:
```bash
java -version  # Should be 17 or higher
```

### UI rendering issues
Try software rendering:
```bash
java -Dprism.order=sw -jar target/socratic-java-mentor-shaded.jar
```

---

## 🤝 Contributing

Contributions welcome! Areas of interest:
- Additional lesson content (Epochs 1-9)
- UI/UX improvements
- Additional challenge types
- Bug fixes

---

## 📧 Support

For issues or questions, please file an issue on GitHub.

---

## 📄 License

This project is open-source and available for educational purposes.

---

## 🎉 Get Started!

Ready to begin your Java journey?

```bash
cd socratic-java-mentor
mvn clean javafx:run
```

**From zero to full-stack developer—one lesson at a time.**
