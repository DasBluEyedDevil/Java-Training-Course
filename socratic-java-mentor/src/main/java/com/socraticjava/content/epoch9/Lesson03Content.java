package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.3: Setting Up Your Development Environment
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-3", "Lesson 9.3: Setting Up Your Dev Environment")
            .addTheory("Your Development Toolkit",
                "Before building your capstone, you need the right tools!\n\n" +
                "ESSENTIAL TOOLS:\n\n" +
                "1. JAVA JDK (Java Development Kit)\n" +
                "   - Recommended: Java 21 (LTS - Long Term Support)\n" +
                "   - Compiles and runs Java code\n\n" +
                "2. IDE (Integrated Development Environment)\n" +
                "   Option A: IntelliJ IDEA (Best for Java/Spring Boot)\n" +
                "   Option B: VS Code (Lightweight, popular)\n\n" +
                "3. BUILD TOOL\n" +
                "   - Maven (comes with Spring Boot projects)\n" +
                "   - Manages dependencies, builds JAR files\n\n" +
                "4. VERSION CONTROL\n" +
                "   - Git (track changes, collaborate)\n" +
                "   - GitHub (host code online)\n\n" +
                "5. DATABASE\n" +
                "   - PostgreSQL (production-ready)\n" +
                "   - Or H2 (in-memory, for quick start)\n\n" +
                "6. API TESTING\n" +
                "   - Postman or curl (test REST APIs)\n\n" +
                "7. CONTAINERIZATION (Optional but recommended)\n" +
                "   - Docker (package and deploy)\n\n" +
                "GOAL: Get everything installed and working!")
            .addAnalogy("Development Environment is Like a Carpenter's Workshop",
                "JAVA JDK (The workbench):\n" +
                "The foundation where all work happens\n" +
                "Like: Main workbench with tools\n\n" +
                "IDE (The power tools):\n" +
                "Makes work faster and easier\n" +
                "Like: Electric saw vs hand saw\n\n" +
                "MAVEN (The assembly line):\n" +
                "Automatically fetches materials and builds project\n" +
                "Like: Conveyor belt that brings wood, nails, glue\n\n" +
                "GIT (The project journal):\n" +
                "Records every change you make\n" +
                "Like: Detailed notes of what you built each day\n\n" +
                "DATABASE (The storage room):\n" +
                "Where finished products are stored\n" +
                "Like: Warehouse for completed furniture\n\n" +
                "POSTMAN (The quality inspector):\n" +
                "Tests if everything works correctly\n" +
                "Like: Inspector checking furniture quality\n\n" +
                "Good tools make good work possible!")
            .addTheory("Step 1: Install Java JDK 21",
                "Java 21 is the recommended LTS version for 2024-2025.\n\n" +
                "WINDOWS:\n" +
                "1. Download from: https://adoptium.net/\n" +
                "2. Choose: Eclipse Temurin 21 (LTS)\n" +
                "3. Run installer, accept defaults\n" +
                "4. Verify installation:\n" +
                "   Open Command Prompt:\n" +
                "   java -version\n" +
                "   Should show: openjdk version \"21.x.x\"\n\n" +
                "MAC:\n" +
                "1. Using Homebrew:\n" +
                "   brew install openjdk@21\n" +
                "2. Or download from: https://adoptium.net/\n" +
                "3. Verify:\n" +
                "   java -version\n\n" +
                "LINUX (Ubuntu/Debian):\n" +
                "sudo apt update\n" +
                "sudo apt install openjdk-21-jdk\n" +
                "java -version\n\n" +
                "VERIFY JAVA_HOME:\n" +
                "Windows: echo %JAVA_HOME%\n" +
                "Mac/Linux: echo $JAVA_HOME\n\n" +
                "If not set, add to environment variables:\n" +
                "Windows: JAVA_HOME = C:\\Program Files\\Java\\jdk-21\n" +
                "Mac/Linux: export JAVA_HOME=/usr/lib/jvm/java-21-openjdk\n\n" +
                "✓ SUCCESS: 'java -version' shows Java 21")
            .addTheory("Step 2A: Install IntelliJ IDEA (Recommended)",
                "IntelliJ IDEA = Best IDE for Java and Spring Boot\n\n" +
                "WHY INTELLIJ?\n" +
                "✓ Built specifically for Java\n" +
                "✓ Excellent Spring Boot support\n" +
                "✓ Smart code completion\n" +
                "✓ Built-in Maven support\n" +
                "✓ Database tools included\n" +
                "✓ Git integration\n\n" +
                "INSTALLATION:\n" +
                "1. Download Community Edition (FREE):\n" +
                "   https://www.jetbrains.com/idea/download/\n\n" +
                "2. Choose your OS: Windows, Mac, or Linux\n\n" +
                "3. Install:\n" +
                "   - Windows: Run .exe installer\n" +
                "   - Mac: Drag to Applications folder\n" +
                "   - Linux: Extract and run idea.sh\n\n" +
                "4. First Launch:\n" +
                "   - Choose theme (Light or Dark)\n" +
                "   - Skip featured plugins (for now)\n" +
                "   - Select: \"Don't send\"\n\n" +
                "ESSENTIAL PLUGINS:\n" +
                "File → Settings → Plugins:\n" +
                "- Spring Boot (should be pre-installed)\n" +
                "- Docker (if using Docker)\n" +
                "- GitToolBox (enhanced Git integration)\n\n" +
                "CONFIGURE JDK:\n" +
                "File → Project Structure → SDKs\n" +
                "Add JDK → Select Java 21 installation\n\n" +
                "✓ SUCCESS: IntelliJ opens without errors")
            .addTheory("Step 2B: Install VS Code (Alternative)",
                "VS Code = Lightweight, popular, great for web development\n\n" +
                "WHY VS CODE?\n" +
                "✓ Fast and lightweight\n" +
                "✓ Great for full-stack (Java + frontend)\n" +
                "✓ Huge extension marketplace\n" +
                "✓ Free and open-source\n\n" +
                "INSTALLATION:\n" +
                "1. Download from: https://code.visualstudio.com/\n" +
                "2. Install for your OS\n" +
                "3. Launch VS Code\n\n" +
                "ESSENTIAL EXTENSIONS:\n" +
                "Click Extensions icon (left sidebar) and install:\n\n" +
                "1. Extension Pack for Java (by Microsoft)\n" +
                "   - Includes: Language Support, Debugger, Test Runner, Maven\n\n" +
                "2. Spring Boot Extension Pack\n" +
                "   - Spring Boot Tools\n" +
                "   - Spring Initializr\n" +
                "   - Spring Boot Dashboard\n\n" +
                "3. GitLens (enhanced Git)\n\n" +
                "4. REST Client (test APIs in VS Code)\n\n" +
                "5. Docker (if using Docker)\n\n" +
                "CONFIGURE JAVA:\n" +
                "Ctrl+Shift+P (Cmd+Shift+P on Mac)\n" +
                "Type: \"Java: Configure Java Runtime\"\n" +
                "Ensure Java 21 is selected\n\n" +
                "✓ SUCCESS: Extensions installed, Java detected")
            .addTheory("Step 3: Install Git and Configure",
                "Git = Version control system (track changes, collaborate)\n\n" +
                "INSTALLATION:\n\n" +
                "WINDOWS:\n" +
                "1. Download: https://git-scm.com/download/win\n" +
                "2. Run installer\n" +
                "3. Accept defaults (important: select \"Use Git from command line\")\n\n" +
                "MAC:\n" +
                "brew install git\n\n" +
                "LINUX:\n" +
                "sudo apt install git\n\n" +
                "VERIFY:\n" +
                "git --version\n" +
                "Should show: git version 2.x.x\n\n" +
                "CONFIGURE (IMPORTANT!):\n" +
                "git config --global user.name \"Your Name\"\n" +
                "git config --global user.email \"your.email@example.com\"\n\n" +
                "CHECK CONFIGURATION:\n" +
                "git config --list\n\n" +
                "CREATE GITHUB ACCOUNT:\n" +
                "1. Go to: https://github.com\n" +
                "2. Sign up (free)\n" +
                "3. Verify email\n\n" +
                "GENERATE SSH KEY (Optional but recommended):\n" +
                "ssh-keygen -t ed25519 -C \"your.email@example.com\"\n" +
                "Press Enter 3 times (default location, no passphrase)\n\n" +
                "Add to GitHub:\n" +
                "cat ~/.ssh/id_ed25519.pub\n" +
                "Copy output → GitHub → Settings → SSH Keys → New SSH Key\n\n" +
                "✓ SUCCESS: 'git --version' works, GitHub account created")
            .addTheory("Step 4: Install PostgreSQL",
                "PostgreSQL = Production-ready relational database\n\n" +
                "INSTALLATION:\n\n" +
                "WINDOWS:\n" +
                "1. Download: https://www.postgresql.org/download/windows/\n" +
                "2. Run installer\n" +
                "3. Set password for 'postgres' user (REMEMBER THIS!)\n" +
                "4. Port: 5432 (default)\n" +
                "5. Install Stack Builder? → Skip\n\n" +
                "MAC:\n" +
                "brew install postgresql@15\n" +
                "brew services start postgresql@15\n\n" +
                "LINUX:\n" +
                "sudo apt install postgresql postgresql-contrib\n" +
                "sudo systemctl start postgresql\n\n" +
                "VERIFY:\n" +
                "psql --version\n" +
                "Should show: psql (PostgreSQL) 15.x\n\n" +
                "CREATE DATABASE FOR YOUR PROJECT:\n" +
                "# Connect to PostgreSQL\n" +
                "psql -U postgres\n" +
                "(Enter password)\n\n" +
                "# Create database\n" +
                "CREATE DATABASE taskmanager;\n\n" +
                "# Create user\n" +
                "CREATE USER taskuser WITH PASSWORD 'yourpassword';\n\n" +
                "# Grant permissions\n" +
                "GRANT ALL PRIVILEGES ON DATABASE taskmanager TO taskuser;\n\n" +
                "# Exit\n" +
                "\\q\n\n" +
                "ALTERNATIVE: Use H2 Database (In-Memory)\n" +
                "✓ No installation needed\n" +
                "✓ Add dependency in Spring Boot\n" +
                "✓ Good for development, not for production\n\n" +
                "✓ SUCCESS: Can connect to PostgreSQL")
            .addTheory("Step 5: Install Postman (API Testing)",
                "Postman = Test your REST APIs without writing frontend\n\n" +
                "INSTALLATION:\n" +
                "1. Download: https://www.postman.com/downloads/\n" +
                "2. Install for your OS\n" +
                "3. Launch Postman\n" +
                "4. Sign up (free) or skip\n\n" +
                "BASIC USAGE:\n" +
                "1. Create new request\n" +
                "2. Set method: GET, POST, PUT, DELETE\n" +
                "3. Enter URL: http://localhost:8080/api/tasks\n" +
                "4. Add headers (if needed):\n" +
                "   Content-Type: application/json\n" +
                "5. Add body (for POST/PUT):\n" +
                "   {\n" +
                "     \"title\": \"Test task\",\n" +
                "     \"description\": \"Testing API\"\n" +
                "   }\n" +
                "6. Click Send\n" +
                "7. View response\n\n" +
                "ALTERNATIVE: Use curl (command-line):\n" +
                "curl -X GET http://localhost:8080/api/tasks\n" +
                "curl -X POST http://localhost:8080/api/tasks \\\n" +
                "  -H \"Content-Type: application/json\" \\\n" +
                "  -d '{\"title\":\"Test\",\"description\":\"Testing\"}'\n\n" +
                "✓ SUCCESS: Postman installed and opens")
            .addKeyPoint("Step 6: Create Your First Spring Boot Project",
                "USE SPRING INITIALIZR:\n\n" +
                "Method 1: Web Browser\n" +
                "1. Go to: https://start.spring.io/\n\n" +
                "2. Configure project:\n" +
                "   - Project: Maven\n" +
                "   - Language: Java\n" +
                "   - Spring Boot: 3.3.x (latest stable)\n" +
                "   - Group: com.yourname\n" +
                "   - Artifact: taskmanager\n" +
                "   - Packaging: Jar\n" +
                "   - Java: 21\n\n" +
                "3. Add Dependencies (click \"Add Dependencies\"):\n" +
                "   - Spring Web\n" +
                "   - Spring Data JPA\n" +
                "   - PostgreSQL Driver (or H2 Database)\n" +
                "   - Spring Security (optional for now)\n" +
                "   - Spring Boot DevTools\n" +
                "   - Validation\n\n" +
                "4. Click \"Generate\" → Downloads taskmanager.zip\n\n" +
                "5. Extract ZIP file\n\n" +
                "6. Open in IDE:\n" +
                "   IntelliJ: File → Open → Select folder\n" +
                "   VS Code: File → Open Folder → Select folder\n\n" +
                "Method 2: IntelliJ IDEA (Built-in)\n" +
                "1. File → New → Project\n" +
                "2. Select: Spring Initializr\n" +
                "3. Configure same as above\n" +
                "4. Select dependencies\n" +
                "5. Click Create\n\n" +
                "Method 3: VS Code Extension\n" +
                "1. Ctrl+Shift+P → \"Spring Initializr: Create a Maven Project\"\n" +
                "2. Follow prompts\n" +
                "3. Select dependencies\n" +
                "4. Choose folder location\n\n" +
                "✓ SUCCESS: Project opens in IDE without errors")
            .addTheory("Step 7: Understanding Project Structure",
                "Your Spring Boot project structure:\n\n" +
                "taskmanager/\n" +
                "├── src/\n" +
                "│   ├── main/\n" +
                "│   │   ├── java/\n" +
                "│   │   │   └── com/yourname/taskmanager/\n" +
                "│   │   │       ├── TaskmanagerApplication.java (Main class)\n" +
                "│   │   │       ├── controller/  (REST controllers)\n" +
                "│   │   │       ├── service/     (Business logic)\n" +
                "│   │   │       ├── repository/  (Database access)\n" +
                "│   │   │       └── model/       (Entities)\n" +
                "│   │   └── resources/\n" +
                "│   │       ├── application.yml  (Configuration)\n" +
                "│   │       ├── static/          (CSS, JS, images)\n" +
                "│   │       └── templates/       (HTML templates)\n" +
                "│   └── test/\n" +
                "│       └── java/\n" +
                "│           └── com/yourname/taskmanager/\n" +
                "│               └── TaskmanagerApplicationTests.java\n" +
                "├── target/           (Compiled code - generated)\n" +
                "├── pom.xml           (Maven dependencies)\n" +
                "├── .gitignore        (Files to ignore in Git)\n" +
                "└── README.md         (Project documentation)\n\n" +
                "KEY FILES:\n\n" +
                "1. TaskmanagerApplication.java:\n" +
                "   Main entry point, has @SpringBootApplication\n" +
                "   Don't modify unless you know what you're doing!\n\n" +
                "2. pom.xml:\n" +
                "   Lists all dependencies\n" +
                "   Maven reads this to download libraries\n\n" +
                "3. application.yml:\n" +
                "   Database connection, port, logging config\n" +
                "   YOU WILL EDIT THIS OFTEN!\n\n" +
                "4. .gitignore:\n" +
                "   Files Git should NOT track\n" +
                "   (target/, .idea/, *.class, etc.)")
            .addExample("Step 8: Configure Database Connection",
                "Edit: src/main/resources/application.yml\n\n" +
                "FOR POSTGRESQL:\n" +
                "spring:\n" +
                "  application:\n" +
                "    name: taskmanager\n" +
                "  datasource:\n" +
                "    url: jdbc:postgresql://localhost:5432/taskmanager\n" +
                "    username: taskuser\n" +
                "    password: yourpassword\n" +
                "    driver-class-name: org.postgresql.Driver\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: update  # Creates tables automatically\n" +
                "    show-sql: true      # Shows SQL in console\n" +
                "    properties:\n" +
                "      hibernate:\n" +
                "        format_sql: true  # Pretty-print SQL\n" +
                "server:\n" +
                "  port: 8080\n\n" +
                "FOR H2 DATABASE (In-Memory, Easier Start):\n" +
                "spring:\n" +
                "  application:\n" +
                "    name: taskmanager\n" +
                "  datasource:\n" +
                "    url: jdbc:h2:mem:testdb\n" +
                "    driver-class-name: org.h2.Driver\n" +
                "    username: sa\n" +
                "    password:\n" +
                "  h2:\n" +
                "    console:\n" +
                "      enabled: true  # Access at http://localhost:8080/h2-console\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: create-drop  # Recreates DB on restart\n" +
                "    show-sql: true\n\n" +
                "IMPORTANT:\n" +
                "- Replace 'yourpassword' with your actual password\n" +
                "- Never commit passwords to Git!\n" +
                "- Use environment variables in production")
            .addExample("Step 9: Test Your Setup",
                "RUN THE APPLICATION:\n\n" +
                "IntelliJ IDEA:\n" +
                "1. Open TaskmanagerApplication.java\n" +
                "2. Click green play button next to main method\n" +
                "3. Or: Right-click → Run 'TaskmanagerApplication'\n\n" +
                "VS Code:\n" +
                "1. Open TaskmanagerApplication.java\n" +
                "2. Click \"Run\" above main method\n" +
                "3. Or: Press F5\n\n" +
                "Command Line:\n" +
                "./mvnw spring-boot:run\n" +
                "(Windows: mvnw.cmd spring-boot:run)\n\n" +
                "EXPECTED OUTPUT:\n" +
                "  .   ____          _            __ _ _\n" +
                " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\\n" +
                "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\\n" +
                " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )\n" +
                "  '  |____| .__|_| |_|_| |_\\__, | / / / /\n" +
                " =========|_|==============|___/=/_/_/_/\n" +
                " :: Spring Boot ::                (v3.3.0)\n\n" +
                "...\n" +
                "Tomcat started on port 8080\n" +
                "Started TaskmanagerApplication in 2.5 seconds\n\n" +
                "✓ SUCCESS: Application starts without errors!\n\n" +
                "TEST IN BROWSER:\n" +
                "Open: http://localhost:8080\n" +
                "Expected: \"Whitelabel Error Page\" (normal for new project)\n\n" +
                "TEST DATABASE CONNECTION:\n" +
                "If you see SQL output in console → Database connected!\n\n" +
                "TROUBLESHOOTING:\n" +
                "❌ Port 8080 already in use:\n" +
                "   Change in application.yml: server.port: 8081\n\n" +
                "❌ Cannot connect to database:\n" +
                "   1. Check PostgreSQL is running\n" +
                "   2. Check username/password in application.yml\n" +
                "   3. Check database exists: psql -U postgres -l\n\n" +
                "❌ Java version error:\n" +
                "   Ensure Java 21 is installed and JAVA_HOME is set")
            .addWarning("Common Setup Mistakes",
                "❌ MISTAKE 1: Wrong Java version\n" +
                "Downloaded Java 8 instead of Java 21\n" +
                "FIX: Download Java 21 from adoptium.net\n\n" +
                "❌ MISTAKE 2: JAVA_HOME not set\n" +
                "IDE can't find Java\n" +
                "FIX: Set JAVA_HOME environment variable\n\n" +
                "❌ MISTAKE 3: Forgot to start PostgreSQL\n" +
                "Application fails with connection error\n" +
                "FIX: Start PostgreSQL service\n\n" +
                "❌ MISTAKE 4: Hardcoded passwords in application.yml\n" +
                "Then committed to GitHub (security risk!)\n" +
                "FIX: Use environment variables or application-local.yml (in .gitignore)\n\n" +
                "❌ MISTAKE 5: Installed too many tools at once\n" +
                "Overwhelmed and confused\n" +
                "FIX: Install one at a time, test each\n\n" +
                "❌ MISTAKE 6: Skipped verification steps\n" +
                "Don't know if tools work until much later\n" +
                "FIX: Test each tool immediately after installing\n\n" +
                "❌ MISTAKE 7: Used Community Edition expecting Ultimate features\n" +
                "Some IntelliJ features require paid version\n" +
                "FIX: Community Edition is sufficient for most projects")
            .addKeyPoint("Step 10: Initialize Git Repository",
                "Track your project with Git:\n\n" +
                "1. NAVIGATE TO PROJECT:\n" +
                "cd /path/to/taskmanager\n\n" +
                "2. INITIALIZE GIT:\n" +
                "git init\n\n" +
                "3. CHECK .gitignore:\n" +
                "Should already exist (Spring Initializr creates it)\n" +
                "Should include:\n" +
                "- target/\n" +
                "- .idea/\n" +
                "- *.class\n" +
                "- application-local.yml\n\n" +
                "4. ADD FILES:\n" +
                "git add .\n\n" +
                "5. FIRST COMMIT:\n" +
                "git commit -m \"Initial project setup\"\n\n" +
                "6. CREATE GITHUB REPOSITORY:\n" +
                "- Go to github.com\n" +
                "- Click \"+\" → New repository\n" +
                "- Name: taskmanager\n" +
                "- Don't initialize with README (you already have files)\n" +
                "- Click Create\n\n" +
                "7. CONNECT TO GITHUB:\n" +
                "git remote add origin https://github.com/yourusername/taskmanager.git\n" +
                "git branch -M main\n" +
                "git push -u origin main\n\n" +
                "✓ SUCCESS: Code is now on GitHub!\n\n" +
                "COMMIT REGULARLY:\n" +
                "git add .\n" +
                "git commit -m \"Add User entity\"\n" +
                "git push")
            .addChallenge(createIDEQuiz())
            .addChallenge(createDatabaseQuiz())
            .addChallenge(createGitQuiz())
            .estimatedMinutes(60)
            .build();
    }

    private static Challenge createIDEQuiz() {
        return new Challenge.Builder("epoch-9-lesson-3-ide", "Development Tools", ChallengeType.MULTIPLE_CHOICE)
            .description("Which IDE is specifically built for Java and Spring Boot development?")
            .addMultipleChoiceOption("A) Visual Studio Code")
            .addMultipleChoiceOption("B) IntelliJ IDEA")
            .addMultipleChoiceOption("C) Sublime Text")
            .addMultipleChoiceOption("D) Notepad++")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createDatabaseQuiz() {
        return new Challenge.Builder("epoch-9-lesson-3-database", "Database Setup", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the recommended Java version for Spring Boot in 2024-2025?")
            .addMultipleChoiceOption("A) Java 8")
            .addMultipleChoiceOption("B) Java 11")
            .addMultipleChoiceOption("C) Java 17")
            .addMultipleChoiceOption("D) Java 21")
            .correctAnswer("D")
            .build();
    }

    private static Challenge createGitQuiz() {
        return new Challenge.Builder("epoch-9-lesson-3-git", "Version Control", ChallengeType.MULTIPLE_CHOICE)
            .description("What should you NEVER commit to a public GitHub repository?")
            .addMultipleChoiceOption("A) Source code files")
            .addMultipleChoiceOption("B) README.md")
            .addMultipleChoiceOption("C) Passwords and secrets")
            .addMultipleChoiceOption("D) pom.xml")
            .correctAnswer("C")
            .build();
    }
}
