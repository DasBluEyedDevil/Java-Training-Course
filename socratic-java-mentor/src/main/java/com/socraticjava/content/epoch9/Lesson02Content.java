package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.2: Planning Your Capstone Project
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-2", "Lesson 9.2: Planning Your Capstone - From Idea to Action Plan")
            .addTheory("Why Planning Matters",
                "You've learned Java, Spring Boot, databases, and frontend.\n" +
                "Now it's time to BUILD SOMETHING REAL.\n\n" +
                "But jumping straight into code is a recipe for disaster:\n\n" +
                "❌ WITHOUT PLANNING:\n" +
                "- Start coding random features\n" +
                "- Realize you need to redesign everything\n" +
                "- Get overwhelmed and give up\n" +
                "- Half-finished project collecting dust\n\n" +
                "✓ WITH PLANNING:\n" +
                "- Clear vision of what you're building\n" +
                "- Know exactly what to build next\n" +
                "- Can show progress to employers\n" +
                "- Finish with a portfolio-worthy project\n\n" +
                "GOAL: Create a solid plan so you can build with confidence!")
            .addAnalogy("Planning a Project is Like Planning a Road Trip",
                "DESTINATION (Project vision):\n" +
                "\"I want to drive from New York to California\"\n" +
                "Like: \"I want to build a task management app\"\n\n" +
                "ROUTE (Features):\n" +
                "NYC → Chicago → Denver → LA (major stops)\n" +
                "Like: User registration → Task CRUD → Sharing → Mobile app\n\n" +
                "DAILY MILESTONES (User stories):\n" +
                "Day 1: NYC to Pittsburgh (400 miles)\n" +
                "Day 2: Pittsburgh to Chicago (450 miles)\n" +
                "Like: Sprint 1: Basic user authentication\n" +
                "      Sprint 2: Create and list tasks\n\n" +
                "GAS STATIONS (MVP - Minimum Viable Product):\n" +
                "You don't drive all the way without stops!\n" +
                "Like: First version: Just login + create tasks\n" +
                "      Later: Add sharing, tags, notifications\n\n" +
                "FLEXIBILITY:\n" +
                "Road closed? Take detour.\n" +
                "Like: Feature too hard? Build simpler version first.\n\n" +
                "Planning = Knowing where you're going!")
            .addTheory("Step 1: Choose Your Project Idea",
                "Pick something you're excited about!\n\n" +
                "GOOD PROJECT IDEAS:\n\n" +
                "1. TASK/TODO MANAGER:\n" +
                "   - Create, edit, delete tasks\n" +
                "   - Mark complete, set due dates\n" +
                "   - Organize by projects/tags\n" +
                "   - Share tasks with others\n\n" +
                "2. EXPENSE TRACKER:\n" +
                "   - Log income and expenses\n" +
                "   - Categorize transactions\n" +
                "   - View charts and reports\n" +
                "   - Budget tracking\n\n" +
                "3. RECIPE MANAGER:\n" +
                "   - Save favorite recipes\n" +
                "   - Search by ingredients\n" +
                "   - Shopping list generator\n" +
                "   - Share recipes with friends\n\n" +
                "4. FITNESS TRACKER:\n" +
                "   - Log workouts\n" +
                "   - Track progress over time\n" +
                "   - Set fitness goals\n" +
                "   - View statistics and charts\n\n" +
                "5. BOOK LIBRARY:\n" +
                "   - Track books read/want to read\n" +
                "   - Rate and review books\n" +
                "   - Get recommendations\n" +
                "   - Share reading lists\n\n" +
                "CRITERIA FOR A GOOD PROJECT:\n" +
                "✓ Personally interesting (you'll stay motivated)\n" +
                "✓ Has CRUD operations (shows database skills)\n" +
                "✓ Has user authentication (shows security knowledge)\n" +
                "✓ Can start simple, grow complex\n" +
                "✓ Demonstrates full-stack skills\n\n" +
                "BAD PROJECT IDEAS:\n" +
                "❌ \"Build the next Facebook\" (too ambitious)\n" +
                "❌ \"AI that predicts stock market\" (too complex)\n" +
                "❌ \"Calculator app\" (too simple)\n" +
                "❌ \"Something I have zero interest in\" (you'll quit)")
            .addTheory("Step 2: Define Your MVP (Minimum Viable Product)",
                "MVP = Simplest version that actually works\n\n" +
                "Example: Task Manager MVP\n\n" +
                "✓ IN MVP (Core features only):\n" +
                "- User registration and login\n" +
                "- Create a task (title + description)\n" +
                "- View list of my tasks\n" +
                "- Mark task as complete\n" +
                "- Delete a task\n\n" +
                "❌ NOT IN MVP (Nice-to-have features):\n" +
                "- Share tasks with other users\n" +
                "- Due dates and reminders\n" +
                "- Tags and categories\n" +
                "- File attachments\n" +
                "- Mobile app\n" +
                "- Email notifications\n\n" +
                "WHY START WITH MVP?\n" +
                "1. See results quickly (motivation!)\n" +
                "2. Easier to debug when small\n" +
                "3. Can show working product to employers\n" +
                "4. Add features incrementally\n\n" +
                "RULE: If it's not essential to basic functionality, it's NOT in the MVP!\n\n" +
                "You can always add features later in Version 2, 3, 4...")
            .addTheory("Step 3: Write User Stories",
                "User stories describe features from user's perspective.\n\n" +
                "FORMAT:\n" +
                "As a [user type], I want [goal] so that [reason].\n\n" +
                "Example User Stories for Task Manager:\n\n" +
                "STORY 1: User Registration\n" +
                "As a new user,\n" +
                "I want to create an account with email and password,\n" +
                "So that I can save my tasks and access them later.\n\n" +
                "Acceptance Criteria:\n" +
                "- Email must be valid format\n" +
                "- Password must be at least 8 characters\n" +
                "- Cannot register with existing email\n" +
                "- Shows success message after registration\n\n" +
                "STORY 2: User Login\n" +
                "As a registered user,\n" +
                "I want to log in with my email and password,\n" +
                "So that I can access my tasks.\n\n" +
                "Acceptance Criteria:\n" +
                "- Redirect to task list after successful login\n" +
                "- Show error if credentials are wrong\n" +
                "- Stay logged in until I log out\n\n" +
                "STORY 3: Create Task\n" +
                "As a logged-in user,\n" +
                "I want to create a new task with a title and description,\n" +
                "So that I can remember what I need to do.\n\n" +
                "Acceptance Criteria:\n" +
                "- Title is required (cannot be empty)\n" +
                "- Description is optional\n" +
                "- New task appears in my task list immediately\n" +
                "- Show success message\n\n" +
                "STORY 4: View Task List\n" +
                "As a logged-in user,\n" +
                "I want to see all my tasks in a list,\n" +
                "So that I can see what I need to do.\n\n" +
                "Acceptance Criteria:\n" +
                "- Show only MY tasks (not other users' tasks)\n" +
                "- Display title, description, and completion status\n" +
                "- Show message if I have no tasks\n\n" +
                "STORY 5: Mark Task Complete\n" +
                "As a logged-in user,\n" +
                "I want to mark a task as complete,\n" +
                "So that I can track my progress.\n\n" +
                "Acceptance Criteria:\n" +
                "- Completed tasks show a checkmark\n" +
                "- Can toggle between complete and incomplete\n" +
                "- Changes saved to database immediately\n\n" +
                "STORY 6: Delete Task\n" +
                "As a logged-in user,\n" +
                "I want to delete a task I no longer need,\n" +
                "So that my task list stays clean.\n\n" +
                "Acceptance Criteria:\n" +
                "- Confirm before deleting (\"Are you sure?\")\n" +
                "- Task removed from database immediately\n" +
                "- Cannot delete other users' tasks")
            .addTheory("Step 4: Design Your Database Schema",
                "Plan your tables BEFORE coding!\n\n" +
                "Example: Task Manager Database\n\n" +
                "TABLE: users\n" +
                "-----------------------------------\n" +
                "id            BIGSERIAL PRIMARY KEY\n" +
                "email         VARCHAR(255) UNIQUE NOT NULL\n" +
                "password      VARCHAR(255) NOT NULL  (hashed!)\n" +
                "name          VARCHAR(100)\n" +
                "created_at    TIMESTAMP DEFAULT NOW()\n\n" +
                "TABLE: tasks\n" +
                "-----------------------------------\n" +
                "id            BIGSERIAL PRIMARY KEY\n" +
                "title         VARCHAR(255) NOT NULL\n" +
                "description   TEXT\n" +
                "completed     BOOLEAN DEFAULT FALSE\n" +
                "user_id       BIGINT NOT NULL\n" +
                "created_at    TIMESTAMP DEFAULT NOW()\n" +
                "updated_at    TIMESTAMP DEFAULT NOW()\n" +
                "FOREIGN KEY (user_id) REFERENCES users(id)\n\n" +
                "RELATIONSHIPS:\n" +
                "- One user has many tasks (1:N)\n" +
                "- One task belongs to one user\n\n" +
                "QUESTIONS TO ASK:\n" +
                "1. What data do I need to store?\n" +
                "2. How are tables related?\n" +
                "3. What should be required vs optional?\n" +
                "4. What needs to be unique?\n" +
                "5. What should happen if user is deleted?\n" +
                "   (CASCADE delete their tasks? Keep tasks?)\n\n" +
                "TIP: Draw it on paper first!")
            .addKeyPoint("Step 5: Choose Your Tech Stack",
                "Pick technologies you've learned:\n\n" +
                "BACKEND:\n" +
                "✓ Java 17+\n" +
                "✓ Spring Boot 3.x\n" +
                "✓ Spring Data JPA\n" +
                "✓ Spring Security (for authentication)\n" +
                "✓ Maven (build tool)\n\n" +
                "DATABASE:\n" +
                "✓ PostgreSQL (recommended)\n" +
                "or H2 (in-memory, for development)\n\n" +
                "FRONTEND:\n" +
                "Option 1: Simple HTML + CSS + JavaScript\n" +
                "✓ Easy to start\n" +
                "✓ No build tools needed\n" +
                "✓ Fetch API for REST calls\n\n" +
                "Option 2: React (if you know it)\n" +
                "✓ More professional\n" +
                "✓ Better for complex UIs\n" +
                "✓ Component-based\n\n" +
                "DEPLOYMENT:\n" +
                "✓ Docker (containerization)\n" +
                "✓ Docker Compose (multi-container)\n\n" +
                "AVOID:\n" +
                "❌ Technologies you've never used\n" +
                "❌ \"Trendy\" frameworks just for resume\n" +
                "❌ Over-engineering (microservices, Kubernetes)\n\n" +
                "REMEMBER: Simple and working beats complex and broken!")
            .addTheory("Step 6: Break Down Into Phases",
                "Build incrementally, not all at once!\n\n" +
                "PHASE 1: Database + Backend (Week 1-2)\n" +
                "□ Set up Spring Boot project\n" +
                "□ Create User and Task entities\n" +
                "□ Create repositories\n" +
                "□ Create services with business logic\n" +
                "□ Create REST controllers\n" +
                "□ Test with Postman/curl\n" +
                "□ Add Spring Security for authentication\n\n" +
                "PHASE 2: Frontend (Week 3)\n" +
                "□ Create HTML pages (login, register, task list)\n" +
                "□ Add CSS styling\n" +
                "□ Write JavaScript to call REST APIs\n" +
                "□ Connect frontend to backend\n" +
                "□ Test full workflow\n\n" +
                "PHASE 3: Polish & Deploy (Week 4)\n" +
                "□ Add error handling\n" +
                "□ Improve UI/UX\n" +
                "□ Write README with screenshots\n" +
                "□ Create Dockerfile\n" +
                "□ Deploy to cloud (optional)\n\n" +
                "PHASE 4: Additional Features (Optional)\n" +
                "□ Add due dates\n" +
                "□ Add task categories/tags\n" +
                "□ Add search functionality\n" +
                "□ Add task sharing\n\n" +
                "EACH PHASE = Working version you can demo!")
            .addExample("Complete Project Plan: Task Manager",
                "PROJECT: Personal Task Manager\n\n" +
                "VISION:\n" +
                "A web application where users can create, manage, and track\n" +
                "their daily tasks. Users can register, log in, and manage\n" +
                "their own personal task list.\n\n" +
                "MVP FEATURES:\n" +
                "1. User registration with email/password\n" +
                "2. User login with session management\n" +
                "3. Create new tasks (title + description)\n" +
                "4. View all my tasks\n" +
                "5. Mark tasks as complete/incomplete\n" +
                "6. Delete tasks\n\n" +
                "TECH STACK:\n" +
                "- Backend: Java 17, Spring Boot 3, Spring Security, Spring Data JPA\n" +
                "- Database: PostgreSQL\n" +
                "- Frontend: HTML, CSS, Vanilla JavaScript\n" +
                "- Tools: Maven, Docker\n\n" +
                "DATABASE TABLES:\n" +
                "users: id, email, password, name, created_at\n" +
                "tasks: id, title, description, completed, user_id, created_at\n\n" +
                "API ENDPOINTS:\n" +
                "POST   /api/auth/register - Register new user\n" +
                "POST   /api/auth/login    - Login user\n" +
                "POST   /api/auth/logout   - Logout user\n" +
                "GET    /api/tasks         - Get all my tasks\n" +
                "POST   /api/tasks         - Create new task\n" +
                "PUT    /api/tasks/{id}    - Update task\n" +
                "DELETE /api/tasks/{id}    - Delete task\n\n" +
                "TIMELINE:\n" +
                "Week 1: Backend setup, entities, repositories, services\n" +
                "Week 2: REST controllers, Spring Security, test with Postman\n" +
                "Week 3: Frontend HTML/CSS/JS, connect to backend\n" +
                "Week 4: Polish, error handling, documentation, deploy\n\n" +
                "SUCCESS CRITERIA:\n" +
                "✓ Can register and log in\n" +
                "✓ Can create, view, update, delete tasks\n" +
                "✓ Tasks persist in database\n" +
                "✓ Each user sees only their own tasks\n" +
                "✓ Works in production environment\n" +
                "✓ Has README with setup instructions")
            .addWarning("Common Planning Mistakes",
                "❌ MISTAKE 1: Planning for months without coding\n" +
                "\"I need the perfect plan before I start!\"\n" +
                "FIX: Plan MVP, start coding, adjust as you go\n\n" +
                "❌ MISTAKE 2: No plan at all\n" +
                "\"I'll just figure it out as I code!\"\n" +
                "FIX: Spend 1-2 days planning before coding\n\n" +
                "❌ MISTAKE 3: MVP too ambitious\n" +
                "\"MVP will have 20 features!\"\n" +
                "FIX: MVP = 5-6 core features maximum\n\n" +
                "❌ MISTAKE 4: Choosing unfamiliar technologies\n" +
                "\"Let me learn React, GraphQL, and Kubernetes!\"\n" +
                "FIX: Use what you know, learn ONE new thing max\n\n" +
                "❌ MISTAKE 5: Perfectionism\n" +
                "\"UI must be perfect before I move to backend!\"\n" +
                "FIX: Embrace iteration, improve over time\n\n" +
                "❌ MISTAKE 6: No deadline\n" +
                "\"I'll finish... eventually\"\n" +
                "FIX: Set 4-week deadline for MVP\n\n" +
                "❌ MISTAKE 7: Building for imaginary users\n" +
                "\"Users will want to export to 20 file formats!\"\n" +
                "FIX: Build features YOU would actually use")
            .addKeyPoint("Your Action Plan Template",
                "Use this template for YOUR project:\n\n" +
                "1. PROJECT NAME:\n" +
                "   [Write a catchy name]\n\n" +
                "2. PROJECT VISION (1-2 sentences):\n" +
                "   [What problem does it solve?]\n\n" +
                "3. MVP FEATURES (5-6 items):\n" +
                "   □ Feature 1\n" +
                "   □ Feature 2\n" +
                "   □ Feature 3\n" +
                "   □ Feature 4\n" +
                "   □ Feature 5\n\n" +
                "4. USER STORIES (Write 3-6):\n" +
                "   Story 1: As a [user], I want [goal] so that [reason]\n" +
                "   Story 2: ...\n\n" +
                "5. DATABASE TABLES:\n" +
                "   Table 1: [name] - [columns]\n" +
                "   Table 2: [name] - [columns]\n\n" +
                "6. API ENDPOINTS:\n" +
                "   GET    /api/resource\n" +
                "   POST   /api/resource\n" +
                "   PUT    /api/resource/{id}\n" +
                "   DELETE /api/resource/{id}\n\n" +
                "7. TECH STACK:\n" +
                "   Backend: [technologies]\n" +
                "   Frontend: [technologies]\n" +
                "   Database: [database]\n\n" +
                "8. TIMELINE:\n" +
                "   Week 1: [tasks]\n" +
                "   Week 2: [tasks]\n" +
                "   Week 3: [tasks]\n" +
                "   Week 4: [tasks]\n\n" +
                "9. SUCCESS CRITERIA:\n" +
                "   ✓ [Measurable goal 1]\n" +
                "   ✓ [Measurable goal 2]\n" +
                "   ✓ [Measurable goal 3]\n\n" +
                "NEXT STEP: Fill this out for YOUR project idea!")
            .addChallenge(createMVPQuiz())
            .addChallenge(createUserStoryQuiz())
            .addChallenge(createPlanningQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createMVPQuiz() {
        return new Challenge.Builder("epoch-9-lesson-2-mvp", "MVP Definition", ChallengeType.MULTIPLE_CHOICE)
            .description("What does MVP stand for in software development?")
            .addMultipleChoiceOption("A) Most Valuable Player")
            .addMultipleChoiceOption("B) Minimum Viable Product")
            .addMultipleChoiceOption("C) Maximum Value Project")
            .addMultipleChoiceOption("D) Minimal Version Prototype")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createUserStoryQuiz() {
        return new Challenge.Builder("epoch-9-lesson-2-story", "User Stories", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the correct format for a user story?")
            .addMultipleChoiceOption("A) As a [user], I need [feature]")
            .addMultipleChoiceOption("B) As a [user], I want [goal] so that [reason]")
            .addMultipleChoiceOption("C) The system shall [requirement]")
            .addMultipleChoiceOption("D) Feature: [name], Description: [details]")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createPlanningQuiz() {
        return new Challenge.Builder("epoch-9-lesson-2-planning", "Project Planning", ChallengeType.MULTIPLE_CHOICE)
            .description("What should you do BEFORE writing any code?")
            .addMultipleChoiceOption("A) Design the perfect UI mockups")
            .addMultipleChoiceOption("B) Define MVP features and plan database schema")
            .addMultipleChoiceOption("C) Learn 5 new frameworks")
            .addMultipleChoiceOption("D) Set up cloud deployment")
            .correctAnswer("B")
            .build();
    }
}
