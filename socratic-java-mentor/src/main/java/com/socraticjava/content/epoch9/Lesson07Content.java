package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.7: Building Your Frontend - HTML, CSS, JavaScript
 */
public class Lesson07Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-7", "Lesson 9.7: Frontend - Connecting to Your API")
            .addTheory("The Frontend Layer",
                "You have a complete backend API. Now build the UI!\n\n" +
                "FRONTEND STACK (Modern 2024-2025):\n" +
                "✓ HTML5 - Structure\n" +
                "✓ CSS3 - Styling (Flexbox, Grid)\n" +
                "✓ JavaScript ES6+ - Logic\n" +
                "✓ Fetch API - HTTP requests (async/await)\n\n" +
                "FILE STRUCTURE:\n" +
                "src/main/resources/static/\n" +
                "├── index.html        (Main page)\n" +
                "├── login.html        (Login page)\n" +
                "├── register.html     (Registration page)\n" +
                "├── css/\n" +
                "│   └── style.css     (All styles)\n" +
                "└── js/\n" +
                "    ├── auth.js       (Authentication logic)\n" +
                "    ├── tasks.js      (Task CRUD operations)\n" +
                "    └── api.js        (API helper functions)\n\n" +
                "GOAL: Build a complete, working task manager UI!")
            .addAnalogy("Frontend is Like a Restaurant's Dining Room",
                "BACKEND (Kitchen):\n" +
                "Prepares food (processes data)\n" +
                "Hidden from customers\n" +
                "Like: Spring Boot API\n\n" +
                "FRONTEND (Dining room):\n" +
                "What customers see and interact with\n" +
                "Beautiful presentation\n" +
                "Easy to use\n" +
                "Like: HTML/CSS/JavaScript UI\n\n" +
                "COMMUNICATION:\n" +
                "Waiter takes order (Fetch API)\n" +
                "Brings to kitchen (POST /api/tasks)\n" +
                "Returns food (JSON response)\n" +
                "Customer eats (UI displays data)\n\n" +
                "BOTH NEEDED:\n" +
                "Kitchen without dining room = No customers\n" +
                "Dining room without kitchen = No food\n" +
                "Backend without frontend = No users\n" +
                "Frontend without backend = No data")
            .addTheory("Step 1: Create Registration Page",
                "Create: src/main/resources/static/register.html\n\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Register - Task Manager</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"auth-box\">\n" +
                "            <h1>Create Account</h1>\n" +
                "            \n" +
                "            <form id=\"registerForm\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"name\">Name</label>\n" +
                "                    <input type=\"text\" id=\"name\" required>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"email\">Email</label>\n" +
                "                    <input type=\"email\" id=\"email\" required>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"password\">Password</label>\n" +
                "                    <input type=\"password\" id=\"password\" required minlength=\"8\">\n" +
                "                    <small>At least 8 characters</small>\n" +
                "                </div>\n" +
                "                \n" +
                "                <button type=\"submit\" class=\"btn btn-primary\">Register</button>\n" +
                "            </form>\n" +
                "            \n" +
                "            <p class=\"auth-link\">\n" +
                "                Already have an account? <a href=\"login.html\">Login</a>\n" +
                "            </p>\n" +
                "            \n" +
                "            <div id=\"message\" class=\"message\" style=\"display: none;\"></div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <script src=\"js/api.js\"></script>\n" +
                "    <script src=\"js/auth.js\"></script>\n" +
                "</body>\n" +
                "</html>\n\n" +
                "KEY HTML5 FEATURES:\n" +
                "- <!DOCTYPE html> - HTML5 doctype\n" +
                "- <meta charset=\"UTF-8\"> - Character encoding\n" +
                "- <meta name=\"viewport\"> - Responsive design\n" +
                "- type=\"email\" - Email validation\n" +
                "- required - Field validation\n" +
                "- minlength=\"8\" - Password strength")
            .addTheory("Step 2: Create Login Page",
                "Create: src/main/resources/static/login.html\n\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Login - Task Manager</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"auth-box\">\n" +
                "            <h1>Welcome Back</h1>\n" +
                "            \n" +
                "            <form id=\"loginForm\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"email\">Email</label>\n" +
                "                    <input type=\"email\" id=\"email\" required>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"password\">Password</label>\n" +
                "                    <input type=\"password\" id=\"password\" required>\n" +
                "                </div>\n" +
                "                \n" +
                "                <button type=\"submit\" class=\"btn btn-primary\">Login</button>\n" +
                "            </form>\n" +
                "            \n" +
                "            <p class=\"auth-link\">\n" +
                "                Don't have an account? <a href=\"register.html\">Register</a>\n" +
                "            </p>\n" +
                "            \n" +
                "            <div id=\"message\" class=\"message\" style=\"display: none;\"></div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <script src=\"js/api.js\"></script>\n" +
                "    <script src=\"js/auth.js\"></script>\n" +
                "</body>\n" +
                "</html>")
            .addTheory("Step 3: Create Main Task Page",
                "Create: src/main/resources/static/index.html\n\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>My Tasks - Task Manager</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <nav class=\"navbar\">\n" +
                "        <div class=\"container\">\n" +
                "            <h1>Task Manager</h1>\n" +
                "            <div>\n" +
                "                <span id=\"userEmail\"></span>\n" +
                "                <button onclick=\"logout()\" class=\"btn btn-secondary\">Logout</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </nav>\n" +
                "    \n" +
                "    <div class=\"container\">\n" +
                "        <!-- Create Task Form -->\n" +
                "        <div class=\"task-form\">\n" +
                "            <h2>Create New Task</h2>\n" +
                "            <form id=\"createTaskForm\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"title\">Title</label>\n" +
                "                    <input type=\"text\" id=\"title\" required>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"description\">Description</label>\n" +
                "                    <textarea id=\"description\" rows=\"3\"></textarea>\n" +
                "                </div>\n" +
                "                \n" +
                "                <button type=\"submit\" class=\"btn btn-primary\">Add Task</button>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "        \n" +
                "        <!-- Messages -->\n" +
                "        <div id=\"message\" class=\"message\" style=\"display: none;\"></div>\n" +
                "        \n" +
                "        <!-- Task List -->\n" +
                "        <div class=\"task-list\">\n" +
                "            <h2>My Tasks</h2>\n" +
                "            <div id=\"tasks\">\n" +
                "                <div class=\"loading\">Loading tasks...</div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <script src=\"js/api.js\"></script>\n" +
                "    <script src=\"js/tasks.js\"></script>\n" +
                "</body>\n" +
                "</html>\n\n" +
                "SEMANTIC HTML:\n" +
                "<nav> - Navigation bar\n" +
                "<form> - User input\n" +
                "<button> - Interactive elements\n" +
                "<div class=\"task-list\"> - Content sections")
            .addTheory("Step 4: Add Modern CSS Styling",
                "Create: src/main/resources/static/css/style.css\n\n" +
                "/* Modern CSS Reset */\n" +
                "* {\n" +
                "    margin: 0;\n" +
                "    padding: 0;\n" +
                "    box-sizing: border-box;\n" +
                "}\n\n" +
                "/* CSS Variables (2024 best practice) */\n" +
                ":root {\n" +
                "    --primary-color: #3b82f6;\n" +
                "    --secondary-color: #6b7280;\n" +
                "    --success-color: #10b981;\n" +
                "    --danger-color: #ef4444;\n" +
                "    --bg-color: #f9fafb;\n" +
                "    --text-color: #1f2937;\n" +
                "    --border-color: #e5e7eb;\n" +
                "    --shadow: 0 1px 3px rgba(0,0,0,0.1);\n" +
                "}\n\n" +
                "body {\n" +
                "    font-family: system-ui, -apple-system, sans-serif;\n" +
                "    background-color: var(--bg-color);\n" +
                "    color: var(--text-color);\n" +
                "    line-height: 1.6;\n" +
                "}\n\n" +
                "/* Container with max-width */\n" +
                ".container {\n" +
                "    max-width: 800px;\n" +
                "    margin: 0 auto;\n" +
                "    padding: 20px;\n" +
                "}\n\n" +
                "/* Modern Flexbox navbar */\n" +
                ".navbar {\n" +
                "    background: white;\n" +
                "    box-shadow: var(--shadow);\n" +
                "    padding: 1rem 0;\n" +
                "    margin-bottom: 2rem;\n" +
                "}\n\n" +
                ".navbar .container {\n" +
                "    display: flex;\n" +
                "    justify-content: space-between;\n" +
                "    align-items: center;\n" +
                "}\n\n" +
                "/* Form styling */\n" +
                ".form-group {\n" +
                "    margin-bottom: 1rem;\n" +
                "}\n\n" +
                ".form-group label {\n" +
                "    display: block;\n" +
                "    margin-bottom: 0.5rem;\n" +
                "    font-weight: 500;\n" +
                "}\n\n" +
                ".form-group input,\n" +
                ".form-group textarea {\n" +
                "    width: 100%;\n" +
                "    padding: 0.75rem;\n" +
                "    border: 1px solid var(--border-color);\n" +
                "    border-radius: 6px;\n" +
                "    font-size: 1rem;\n" +
                "}\n\n" +
                ".form-group input:focus,\n" +
                ".form-group textarea:focus {\n" +
                "    outline: none;\n" +
                "    border-color: var(--primary-color);\n" +
                "    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);\n" +
                "}\n\n" +
                "/* Button styles */\n" +
                ".btn {\n" +
                "    padding: 0.75rem 1.5rem;\n" +
                "    border: none;\n" +
                "    border-radius: 6px;\n" +
                "    font-size: 1rem;\n" +
                "    cursor: pointer;\n" +
                "    transition: all 0.2s;\n" +
                "}\n\n" +
                ".btn-primary {\n" +
                "    background: var(--primary-color);\n" +
                "    color: white;\n" +
                "}\n\n" +
                ".btn-primary:hover {\n" +
                "    background: #2563eb;\n" +
                "}\n\n" +
                "/* Task card with CSS Grid */\n" +
                ".task-card {\n" +
                "    background: white;\n" +
                "    padding: 1.5rem;\n" +
                "    margin-bottom: 1rem;\n" +
                "    border-radius: 8px;\n" +
                "    box-shadow: var(--shadow);\n" +
                "    display: grid;\n" +
                "    grid-template-columns: 1fr auto;\n" +
                "    gap: 1rem;\n" +
                "}\n\n" +
                ".task-card.completed {\n" +
                "    opacity: 0.6;\n" +
                "}\n\n" +
                ".task-card h3 {\n" +
                "    margin-bottom: 0.5rem;\n" +
                "}\n\n" +
                ".task-actions {\n" +
                "    display: flex;\n" +
                "    gap: 0.5rem;\n" +
                "    align-items: center;\n" +
                "}\n\n" +
                "/* Responsive design */\n" +
                "@media (max-width: 768px) {\n" +
                "    .task-card {\n" +
                "        grid-template-columns: 1fr;\n" +
                "    }\n" +
                "}")
            .addKeyPoint("Step 5: Create API Helper Module (Modern ES6+)",
                "Create: src/main/resources/static/js/api.js\n\n" +
                "// API Configuration\n" +
                "const API_BASE_URL = 'http://localhost:8080/api';\n\n" +
                "// Get auth credentials from localStorage\n" +
                "function getAuthHeader() {\n" +
                "    const email = localStorage.getItem('userEmail');\n" +
                "    const password = localStorage.getItem('userPassword');\n" +
                "    \n" +
                "    if (!email || !password) {\n" +
                "        return null;\n" +
                "    }\n" +
                "    \n" +
                "    // Basic Auth: base64(email:password)\n" +
                "    const credentials = btoa(`${email}:${password}`);\n" +
                "    return `Basic ${credentials}`;\n" +
                "}\n\n" +
                "// Generic API request function (2024 best practice)\n" +
                "async function apiRequest(endpoint, options = {}) {\n" +
                "    const authHeader = getAuthHeader();\n" +
                "    \n" +
                "    const config = {\n" +
                "        headers: {\n" +
                "            'Content-Type': 'application/json',\n" +
                "            ...(authHeader && { 'Authorization': authHeader }),\n" +
                "            ...options.headers\n" +
                "        },\n" +
                "        ...options\n" +
                "    };\n" +
                "    \n" +
                "    try {\n" +
                "        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);\n" +
                "        \n" +
                "        // Check for authentication errors\n" +
                "        if (response.status === 401) {\n" +
                "            localStorage.clear();\n" +
                "            window.location.href = '/login.html';\n" +
                "            throw new Error('Authentication failed');\n" +
                "        }\n" +
                "        \n" +
                "        // Parse response\n" +
                "        const data = response.status !== 204 ? await response.json() : null;\n" +
                "        \n" +
                "        if (!response.ok) {\n" +
                "            throw new Error(data?.detail || data?.message || 'Request failed');\n" +
                "        }\n" +
                "        \n" +
                "        return data;\n" +
                "    } catch (error) {\n" +
                "        console.error('API Error:', error);\n" +
                "        throw error;\n" +
                "    }\n" +
                "}\n\n" +
                "// Check if user is authenticated\n" +
                "function isAuthenticated() {\n" +
                "    return localStorage.getItem('userEmail') !== null;\n" +
                "}\n\n" +
                "// Redirect to login if not authenticated\n" +
                "function requireAuth() {\n" +
                "    if (!isAuthenticated()) {\n" +
                "        window.location.href = '/login.html';\n" +
                "    }\n" +
                "}\n\n" +
                "MODERN JAVASCRIPT FEATURES:\n" +
                "- async/await (ES2017) - Clean asynchronous code\n" +
                "- Template literals (`${var}`) - String interpolation\n" +
                "- Arrow functions (() => {}) - Concise syntax\n" +
                "- Destructuring ({...options}) - Spread operator\n" +
                "- Optional chaining (data?.detail) - Null safety")
            .addTheory("Step 6: Authentication Logic (Modern Async/Await)",
                "Create: src/main/resources/static/js/auth.js\n\n" +
                "// Register user\n" +
                "async function register(event) {\n" +
                "    event.preventDefault();\n" +
                "    \n" +
                "    const name = document.getElementById('name').value;\n" +
                "    const email = document.getElementById('email').value;\n" +
                "    const password = document.getElementById('password').value;\n" +
                "    \n" +
                "    try {\n" +
                "        const data = await apiRequest('/auth/register', {\n" +
                "            method: 'POST',\n" +
                "            body: JSON.stringify({ name, email, password })\n" +
                "        });\n" +
                "        \n" +
                "        showMessage('Registration successful! Please login.', 'success');\n" +
                "        \n" +
                "        // Redirect to login after 2 seconds\n" +
                "        setTimeout(() => {\n" +
                "            window.location.href = '/login.html';\n" +
                "        }, 2000);\n" +
                "        \n" +
                "    } catch (error) {\n" +
                "        showMessage(error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Login user\n" +
                "async function login(event) {\n" +
                "    event.preventDefault();\n" +
                "    \n" +
                "    const email = document.getElementById('email').value;\n" +
                "    const password = document.getElementById('password').value;\n" +
                "    \n" +
                "    try {\n" +
                "        const data = await apiRequest('/auth/login', {\n" +
                "            method: 'POST',\n" +
                "            body: JSON.stringify({ email, password })\n" +
                "        });\n" +
                "        \n" +
                "        // Store credentials (Note: Use JWT in production!)\n" +
                "        localStorage.setItem('userEmail', email);\n" +
                "        localStorage.setItem('userPassword', password);\n" +
                "        \n" +
                "        // Redirect to tasks page\n" +
                "        window.location.href = '/index.html';\n" +
                "        \n" +
                "    } catch (error) {\n" +
                "        showMessage(error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Logout\n" +
                "function logout() {\n" +
                "    localStorage.clear();\n" +
                "    window.location.href = '/login.html';\n" +
                "}\n\n" +
                "// Show message to user\n" +
                "function showMessage(text, type) {\n" +
                "    const messageEl = document.getElementById('message');\n" +
                "    messageEl.textContent = text;\n" +
                "    messageEl.className = `message ${type}`;\n" +
                "    messageEl.style.display = 'block';\n" +
                "    \n" +
                "    setTimeout(() => {\n" +
                "        messageEl.style.display = 'none';\n" +
                "    }, 5000);\n" +
                "}\n\n" +
                "// Attach event listeners when page loads\n" +
                "document.addEventListener('DOMContentLoaded', () => {\n" +
                "    const registerForm = document.getElementById('registerForm');\n" +
                "    const loginForm = document.getElementById('loginForm');\n" +
                "    \n" +
                "    if (registerForm) {\n" +
                "        registerForm.addEventListener('submit', register);\n" +
                "    }\n" +
                "    \n" +
                "    if (loginForm) {\n" +
                "        loginForm.addEventListener('submit', login);\n" +
                "    }\n" +
                "});\n\n" +
                "KEY PATTERNS:\n" +
                "- async/await for cleaner code\n" +
                "- try-catch for error handling\n" +
                "- localStorage for session management\n" +
                "- DOMContentLoaded for initialization")
            .addExample("Step 7: Task Management Logic",
                "Create: src/main/resources/static/js/tasks.js\n\n" +
                "// Load tasks when page loads\n" +
                "document.addEventListener('DOMContentLoaded', async () => {\n" +
                "    requireAuth();  // Redirect if not logged in\n" +
                "    \n" +
                "    // Display user email\n" +
                "    const userEmail = localStorage.getItem('userEmail');\n" +
                "    document.getElementById('userEmail').textContent = userEmail;\n" +
                "    \n" +
                "    // Load tasks\n" +
                "    await loadTasks();\n" +
                "    \n" +
                "    // Attach form listener\n" +
                "    document.getElementById('createTaskForm')\n" +
                "        .addEventListener('submit', createTask);\n" +
                "});\n\n" +
                "// Load all tasks\n" +
                "async function loadTasks() {\n" +
                "    try {\n" +
                "        const tasks = await apiRequest('/tasks');\n" +
                "        displayTasks(tasks);\n" +
                "    } catch (error) {\n" +
                "        showMessage('Failed to load tasks', 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Display tasks in UI\n" +
                "function displayTasks(tasks) {\n" +
                "    const container = document.getElementById('tasks');\n" +
                "    \n" +
                "    if (tasks.length === 0) {\n" +
                "        container.innerHTML = '<p>No tasks yet. Create one above!</p>';\n" +
                "        return;\n" +
                "    }\n" +
                "    \n" +
                "    // Modern template literals and map\n" +
                "    container.innerHTML = tasks.map(task => `\n" +
                "        <div class=\"task-card ${task.completed ? 'completed' : ''}\">\n" +
                "            <div class=\"task-content\">\n" +
                "                <h3>${escapeHtml(task.title)}</h3>\n" +
                "                <p>${escapeHtml(task.description || '')}</p>\n" +
                "                <small>Created: ${new Date(task.createdAt).toLocaleDateString()}</small>\n" +
                "            </div>\n" +
                "            <div class=\"task-actions\">\n" +
                "                <button onclick=\"toggleComplete(${task.id}, ${!task.completed})\" \n" +
                "                        class=\"btn ${task.completed ? 'btn-secondary' : 'btn-success'}\">\n" +
                "                    ${task.completed ? 'Undo' : 'Complete'}\n" +
                "                </button>\n" +
                "                <button onclick=\"deleteTask(${task.id})\" class=\"btn btn-danger\">\n" +
                "                    Delete\n" +
                "                </button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    `).join('');\n" +
                "}\n\n" +
                "// Create new task\n" +
                "async function createTask(event) {\n" +
                "    event.preventDefault();\n" +
                "    \n" +
                "    const title = document.getElementById('title').value;\n" +
                "    const description = document.getElementById('description').value;\n" +
                "    \n" +
                "    try {\n" +
                "        await apiRequest('/tasks', {\n" +
                "            method: 'POST',\n" +
                "            body: JSON.stringify({ title, description })\n" +
                "        });\n" +
                "        \n" +
                "        // Clear form\n" +
                "        document.getElementById('createTaskForm').reset();\n" +
                "        \n" +
                "        showMessage('Task created!', 'success');\n" +
                "        await loadTasks();  // Reload tasks\n" +
                "        \n" +
                "    } catch (error) {\n" +
                "        showMessage(error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Toggle task completion\n" +
                "async function toggleComplete(taskId, completed) {\n" +
                "    try {\n" +
                "        await apiRequest(`/tasks/${taskId}`, {\n" +
                "            method: 'PUT',\n" +
                "            body: JSON.stringify({ completed })\n" +
                "        });\n" +
                "        \n" +
                "        await loadTasks();\n" +
                "    } catch (error) {\n" +
                "        showMessage(error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Delete task\n" +
                "async function deleteTask(taskId) {\n" +
                "    if (!confirm('Delete this task?')) return;\n" +
                "    \n" +
                "    try {\n" +
                "        await apiRequest(`/tasks/${taskId}`, {\n" +
                "            method: 'DELETE'\n" +
                "        });\n" +
                "        \n" +
                "        showMessage('Task deleted', 'success');\n" +
                "        await loadTasks();\n" +
                "    } catch (error) {\n" +
                "        showMessage(error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Prevent XSS attacks\n" +
                "function escapeHtml(text) {\n" +
                "    const div = document.createElement('div');\n" +
                "    div.textContent = text;\n" +
                "    return div.innerHTML;\n" +
                "}\n\n" +
                "function showMessage(text, type) {\n" +
                "    const messageEl = document.getElementById('message');\n" +
                "    messageEl.textContent = text;\n" +
                "    messageEl.className = `message ${type}`;\n" +
                "    messageEl.style.display = 'block';\n" +
                "    setTimeout(() => messageEl.style.display = 'none', 3000);\n" +
                "}")
            .addWarning("Common Frontend Mistakes",
                "❌ MISTAKE 1: Not escaping user input\n" +
                "innerHTML = task.title;  // XSS vulnerability!\n" +
                "FIX: Use textContent or escapeHtml() function\n\n" +
                "❌ MISTAKE 2: Not handling errors\n" +
                "const data = await fetch('/api/tasks');  // No try-catch!\n" +
                "FIX: Always wrap fetch in try-catch\n\n" +
                "❌ MISTAKE 3: Storing passwords in localStorage\n" +
                "localStorage is NOT secure!\n" +
                "FIX: Use JWT tokens instead (better security)\n\n" +
                "❌ MISTAKE 4: Not checking response.ok\n" +
                "const data = await response.json();  // Might be error!\n" +
                "FIX: Check if (!response.ok) throw error\n\n" +
                "❌ MISTAKE 5: Forgetting async on function\n" +
                "function loadTasks() { await fetch(...) }  // Error!\n" +
                "FIX: async function loadTasks()\n\n" +
                "❌ MISTAKE 6: Not clearing forms after submit\n" +
                "Form still has old data\n" +
                "FIX: form.reset() after successful submit\n\n" +
                "❌ MISTAKE 7: Hardcoding URLs\n" +
                "fetch('http://localhost:8080/api/tasks')\n" +
                "FIX: Use const API_BASE_URL")
            .addKeyPoint("Best Practices Summary (2024-2025)",
                "MODERN JAVASCRIPT:\n" +
                "✓ Use async/await (not .then() chains)\n" +
                "✓ Use const/let (never var)\n" +
                "✓ Use arrow functions\n" +
                "✓ Use template literals for strings\n" +
                "✓ Use ES6 modules (import/export)\n\n" +
                "FETCH API:\n" +
                "✓ Always wrap in try-catch\n" +
                "✓ Check response.ok before parsing\n" +
                "✓ Handle 401 (redirect to login)\n" +
                "✓ Show user-friendly error messages\n\n" +
                "SECURITY:\n" +
                "✓ Escape all user input (prevent XSS)\n" +
                "✓ Use HTTPS in production\n" +
                "✓ Don't store sensitive data in localStorage\n" +
                "✓ Implement CORS properly\n" +
                "✓ Validate input on frontend AND backend\n\n" +
                "CSS:\n" +
                "✓ Use CSS variables for colors\n" +
                "✓ Use Flexbox/Grid for layout\n" +
                "✓ Make responsive with media queries\n" +
                "✓ Use semantic HTML\n\n" +
                "UX:\n" +
                "✓ Show loading states\n" +
                "✓ Display error messages clearly\n" +
                "✓ Confirm destructive actions (delete)\n" +
                "✓ Clear forms after submission\n" +
                "✓ Provide feedback for all actions")
            .addChallenge(createAsyncQuiz())
            .addChallenge(createFetchQuiz())
            .addChallenge(createSecurityQuiz())
            .estimatedMinutes(65)
            .build();
    }

    private static Challenge createAsyncQuiz() {
        return new Challenge.Builder("epoch-9-lesson-7-async", "Async JavaScript", ChallengeType.MULTIPLE_CHOICE)
            .description("What keyword must be used with 'await'?")
            .addMultipleChoiceOption("A) function")
            .addMultipleChoiceOption("B) async")
            .addMultipleChoiceOption("C) promise")
            .addMultipleChoiceOption("D) fetch")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createFetchQuiz() {
        return new Challenge.Builder("epoch-9-lesson-7-fetch", "Fetch API", ChallengeType.MULTIPLE_CHOICE)
            .description("How do you check if a fetch request succeeded?")
            .addMultipleChoiceOption("A) if (response.success)")
            .addMultipleChoiceOption("B) if (response.ok)")
            .addMultipleChoiceOption("C) if (response.status === 200)")
            .addMultipleChoiceOption("D) if (!response.error)")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSecurityQuiz() {
        return new Challenge.Builder("epoch-9-lesson-7-security", "Frontend Security", ChallengeType.MULTIPLE_CHOICE)
            .description("Why should you escape user input before displaying it?")
            .addMultipleChoiceOption("A) To make it look prettier")
            .addMultipleChoiceOption("B) To prevent XSS attacks")
            .addMultipleChoiceOption("C) To save database space")
            .addMultipleChoiceOption("D) To improve performance")
            .correctAnswer("B")
            .build();
    }
}
