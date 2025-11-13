package com.socraticjava.content.epoch8;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 8.2: Building a Complete Full-Stack Feature
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-8-lesson-2", "Lesson 8.2: Full-Stack Feature - End to End")
            .addTheory("The Complete Picture: Database → Backend → Frontend",
                "Let's build a complete \"User List\" feature:\n\n" +
                "1. DATABASE (MySQL/PostgreSQL):\n" +
                "CREATE TABLE users (\n" +
                "    id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    name VARCHAR(100) NOT NULL,\n" +
                "    email VARCHAR(255) UNIQUE,\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");\n\n" +
                "2. BACKEND (Spring Boot):\n" +
                "Entity, Repository, Service, Controller\n\n" +
                "3. FRONTEND (HTML + JavaScript):\n" +
                "Display users, add new user form")
            .addTheory("Step 1: Backend Entity and Repository",
                "@Entity\n" +
                "public class User {\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" +
                "    \n" +
                "    private String name;\n" +
                "    private String email;\n" +
                "    \n" +
                "    @CreationTimestamp\n" +
                "    private LocalDateTime createdAt;\n" +
                "    \n" +
                "    // Getters and setters\n" +
                "}\n\n" +
                "public interface UserRepository extends JpaRepository<User, Long> {\n" +
                "    // Spring generates all methods\n" +
                "}")
            .addTheory("Step 2: Backend Service and Controller",
                "@Service\n" +
                "public class UserService {\n" +
                "    @Autowired\n" +
                "    private UserRepository userRepository;\n" +
                "    \n" +
                "    public List<User> getAllUsers() {\n" +
                "        return userRepository.findAll();\n" +
                "    }\n" +
                "    \n" +
                "    public User createUser(User user) {\n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/users\")\n" +
                "@CrossOrigin(origins = \"*\")  // Allow frontend to call API\n" +
                "public class UserController {\n" +
                "    @Autowired\n" +
                "    private UserService userService;\n" +
                "    \n" +
                "    @GetMapping\n" +
                "    public List<User> getAll() {\n" +
                "        return userService.getAllUsers();\n" +
                "    }\n" +
                "    \n" +
                "    @PostMapping\n" +
                "    public User create(@RequestBody User user) {\n" +
                "        return userService.createUser(user);\n" +
                "    }\n" +
                "}\n\n" +
                "Now: http://localhost:8080/api/users returns JSON")
            .addTheory("Step 3: Frontend HTML",
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>User Manager</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Users</h1>\n" +
                "    \n" +
                "    <!-- Display users -->\n" +
                "    <div id=\"userList\"></div>\n" +
                "    \n" +
                "    <!-- Add user form -->\n" +
                "    <h2>Add User</h2>\n" +
                "    <form id=\"addUserForm\">\n" +
                "        <input type=\"text\" id=\"name\" placeholder=\"Name\" required>\n" +
                "        <input type=\"email\" id=\"email\" placeholder=\"Email\" required>\n" +
                "        <button type=\"submit\">Add User</button>\n" +
                "    </form>\n" +
                "    \n" +
                "    <script src=\"app.js\"></script>\n" +
                "</body>\n" +
                "</html>")
            .addTheory("Step 4: Frontend JavaScript",
                "const API_URL = 'http://localhost:8080/api/users';\n\n" +
                "// Load users when page loads\n" +
                "window.onload = function() {\n" +
                "    loadUsers();\n" +
                "};\n\n" +
                "// Fetch and display users\n" +
                "function loadUsers() {\n" +
                "    fetch(API_URL)\n" +
                "        .then(response => response.json())\n" +
                "        .then(users => {\n" +
                "            const userList = document.getElementById('userList');\n" +
                "            userList.innerHTML = '';\n" +
                "            \n" +
                "            users.forEach(user => {\n" +
                "                const div = document.createElement('div');\n" +
                "                div.textContent = `${user.name} (${user.email})`;\n" +
                "                userList.appendChild(div);\n" +
                "            });\n" +
                "        })\n" +
                "        .catch(error => console.error('Error:', error));\n" +
                "}\n\n" +
                "// Add new user\n" +
                "document.getElementById('addUserForm').onsubmit = function(e) {\n" +
                "    e.preventDefault();\n" +
                "    \n" +
                "    const user = {\n" +
                "        name: document.getElementById('name').value,\n" +
                "        email: document.getElementById('email').value\n" +
                "    };\n" +
                "    \n" +
                "    fetch(API_URL, {\n" +
                "        method: 'POST',\n" +
                "        headers: { 'Content-Type': 'application/json' },\n" +
                "        body: JSON.stringify(user)\n" +
                "    })\n" +
                "    .then(response => response.json())\n" +
                "    .then(() => {\n" +
                "        loadUsers();  // Refresh list\n" +
                "        e.target.reset();  // Clear form\n" +
                "    });\n" +
                "};")
            .addKeyPoint("The Full Flow",
                "USER ACTION:\n" +
                "1. User fills form, clicks \"Add User\"\n\n" +
                "FRONTEND:\n" +
                "2. JavaScript captures form data\n" +
                "3. fetch() sends POST to http://localhost:8080/api/users\n" +
                "4. Sends JSON: {\"name\":\"Alice\",\"email\":\"alice@email.com\"}\n\n" +
                "BACKEND:\n" +
                "5. @PostMapping receives request\n" +
                "6. @RequestBody converts JSON to User object\n" +
                "7. UserService.createUser() called\n" +
                "8. UserRepository.save() inserts to database\n\n" +
                "DATABASE:\n" +
                "9. INSERT INTO users...\n" +
                "10. Returns generated ID\n\n" +
                "BACKEND RESPONSE:\n" +
                "11. Return User object as JSON\n" +
                "12. Status: 200 OK\n\n" +
                "FRONTEND:\n" +
                "13. Receives response\n" +
                "14. Calls loadUsers() to refresh list\n" +
                "15. GET /api/users\n" +
                "16. Displays updated user list\n\n" +
                "COMPLETE CYCLE!")
            .addChallenge(createFullStackQuiz())
            .addChallenge(createCORSQuiz())
            .addChallenge(createFlowQuiz())
            .estimatedMinutes(50)
            .build();
    }

    private static Challenge createFullStackQuiz() {
        return new Challenge.Builder("epoch-8-lesson-2-fullstack", "Full-Stack Understanding", ChallengeType.MULTIPLE_CHOICE)
            .description("In a full-stack application, where does business logic belong?")
            .addMultipleChoiceOption("A) In the frontend JavaScript")
            .addMultipleChoiceOption("B) In the database")
            .addMultipleChoiceOption("C) In the backend Service layer")
            .addMultipleChoiceOption("D) In the HTML")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createCORSQuiz() {
        return new Challenge.Builder("epoch-8-lesson-2-cors", "CORS", ChallengeType.MULTIPLE_CHOICE)
            .description("Why do you need @CrossOrigin in the controller?")
            .addMultipleChoiceOption("A) To make the API faster")
            .addMultipleChoiceOption("B) To allow frontend running on different origin to call the API")
            .addMultipleChoiceOption("C) To encrypt data")
            .addMultipleChoiceOption("D) To validate input")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createFlowQuiz() {
        return new Challenge.Builder("epoch-8-lesson-2-flow", "Request Flow", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the correct order of layers in a Spring Boot application?")
            .addMultipleChoiceOption("A) Database → Service → Controller → Repository")
            .addMultipleChoiceOption("B) Controller → Service → Repository → Database")
            .addMultipleChoiceOption("C) Repository → Controller → Service → Database")
            .addMultipleChoiceOption("D) Service → Repository → Controller → Database")
            .correctAnswer("B")
            .build();
    }
}
