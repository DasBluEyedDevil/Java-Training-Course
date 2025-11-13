# Socratic Java Mentor

**Learn Java from First Principles to Full-Stack Development**

A self-contained, interactive desktop learning platform that teaches Java through hands-on practice and automated feedback.

---

## 🎯 What Is This?

Socratic Java Mentor is a standalone desktop application that provides:

- **Interactive Lessons**: Learn concepts through clear explanations and real-world analogies
- **Hands-On Coding**: Write Java code directly in the app
- **Automated Testing**: Get instant feedback on your code
- **Progress Tracking**: Your progress is saved automatically
- **Concept-First Learning**: Understand the "why" before the "what"

---

## 🚀 Quick Start

### Prerequisites

- **Java Development Kit (JDK) 17 or higher**
  - Download from: https://adoptium.net/
  - Verify installation: `java -version`

### Running the Application

#### Option 1: Using Maven (Development)

```bash
cd socratic-java-mentor
mvn clean javafx:run
```

#### Option 2: Using the Executable JAR

```bash
# Build the JAR first
mvn clean package

# Run the application
java -jar target/socratic-java-mentor-shaded.jar
```

#### Option 3: Platform-Specific Launch Scripts

**Windows:**
```batch
launch-windows.bat
```

**Linux/Mac:**
```bash
chmod +x launch-unix.sh
./launch-unix.sh
```

---

## 📚 Course Structure

### Epoch 0: The Foundation (✅ Complete - 4 Lessons, 11 Challenges)
- **Lesson 0.1**: What is a Computer Program? — Conceptual introduction with quiz
- **Lesson 0.2**: Your First Java Program — Hello World with 2 coding challenges
- **Lesson 0.3**: Understanding Variables — The "box" analogy with 3 challenges
- **Lesson 0.4**: Making Decisions with If/Else — Conditional logic with 3 challenges

### Epoch 1: The Bare Essentials (✅ Complete - 5 Lessons, 20 Challenges)
- **Lesson 1.1**: Data Types in Depth — int, double, String, boolean explained
- **Lesson 1.2**: Operators and Expressions — Arithmetic, precedence, shortcuts
- **Lesson 1.3**: While Loops — Mastering repetition and avoiding infinite loops
- **Lesson 1.4**: For Loops — Counting made easy with clean syntax
- **Lesson 1.5**: Introduction to Methods — Reusable code blocks and functions

### Epoch 2-9: Full Curriculum Available
- **Epoch 2**: Thinking in Objects (OOP fundamentals)
- **Epoch 3**: Building Your Toolkit (Arrays, Collections)
- **Epoch 4**: The Professional's Toolbox (Testing, Maven)
- **Epoch 5**: The Database (SQL, JDBC)
- **Epoch 6**: The Connected Web (HTTP, REST APIs)
- **Epoch 7**: The Modern Framework (Spring Boot)
- **Epoch 8**: The Frontend Connection (Full-Stack)
- **Epoch 9**: The Capstone Journey (Portfolio Project)

**Total: 10 Epochs, 18+ Lessons, Complete Java Learning Path!**

---

## 🎓 How It Works

1. **Read the Lesson**: Each lesson starts with clear explanations and analogies
2. **Complete Challenges**: Practice what you learned with interactive coding challenges
3. **Get Instant Feedback**: The app compiles and tests your code automatically
4. **Track Progress**: Your completed lessons are saved locally

---

## 🛠️ Technology Stack

- **Java 17**: Core language
- **JavaFX 21**: User interface
- **Maven**: Build tool
- **Java Compiler API**: In-memory code compilation
- **Gson**: Progress persistence

---

## 📁 Project Structure

```
socratic-java-mentor/
├── src/main/java/com/socraticjava/
│   ├── SocraticJavaApp.java          # Main entry point
│   ├── model/                         # Data models (Lesson, Challenge, etc.)
│   ├── service/                       # Business logic (Compiler, TestRunner, etc.)
│   ├── ui/                            # JavaFX UI components
│   └── content/                       # Embedded lesson content
│       └── epoch0/                    # Epoch 0 lessons
├── pom.xml                            # Maven configuration
└── README.md
```

---

## 🔧 Building from Source

### Compile Only
```bash
mvn clean compile
```

### Run Tests (if any)
```bash
mvn test
```

### Create Executable JAR
```bash
mvn clean package
```

This creates two JAR files in the `target/` directory:
- `socratic-java-mentor-1.0.0.jar` - Standard JAR
- `socratic-java-mentor-shaded.jar` - Executable JAR with all dependencies

---

## 💾 Progress Data

Your progress is automatically saved to:
- **Windows**: `C:\Users\<YourName>\.socratic-java\progress.json`
- **Linux/Mac**: `~/.socratic-java/progress.json`

To reset your progress, simply delete this file.

---

## 🤝 Contributing

This is a learning platform under active development. Contributions are welcome!

Areas for contribution:
- More lesson content (Epochs 1-9)
- UI improvements
- Additional challenge types
- Bug fixes

---

## 📝 License

This project is open-source and available for educational purposes.

---

## 🐛 Troubleshooting

### "Java compiler not available"
**Solution**: You need the JDK (not just the JRE). Download from https://adoptium.net/

### Application won't start
**Solution**: Verify you have Java 17+ installed:
```bash
java -version
```

### UI looks strange
**Solution**: Try running with hardware acceleration disabled:
```bash
java -Dprism.order=sw -jar socratic-java-mentor-shaded.jar
```

---

## 📧 Support

For issues or questions, please file an issue on the GitHub repository.

---

**Happy Learning! 🎉**

Start your journey from absolute beginner to full-stack Java developer today.
