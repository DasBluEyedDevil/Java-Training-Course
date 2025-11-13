package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.8: Testing Your Full-Stack Application
 */
public class Lesson08Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-8", "Lesson 9.8: Testing - Making Sure Everything Works")
            .addTheory("Why Testing Matters",
                "Your app works on your machine. But will it work tomorrow?\n\n" +
                "WITHOUT TESTS:\n" +
                "❌ Change breaks existing features (regression)\n" +
                "❌ Don't know until users complain\n" +
                "❌ Afraid to refactor code\n" +
                "❌ Manual testing every time (slow!)\n\n" +
                "WITH TESTS:\n" +
                "✓ Catch bugs immediately\n" +
                "✓ Refactor with confidence\n" +
                "✓ Document how code should work\n" +
                "✓ Run tests in seconds\n\n" +
                "TESTING PYRAMID (2024-2025 Best Practice):\n" +
                "       /\\\n" +
                "      /  \\  End-to-End Tests (Few)\n" +
                "     /    \\  - Full app + browser\n" +
                "    /──────\\\n" +
                "   /        \\  Integration Tests (Some)\n" +
                "  /          \\  - Multiple layers together\n" +
                " /────────────\\\n" +
                "/              \\  Unit Tests (Many)\n" +
                "────────────────  - Single class/method\n\n" +
                "FOCUS: Unit and integration tests (fastest, most valuable)")
            .addAnalogy("Testing is Like Quality Control in Manufacturing",
                "UNIT TESTS (Individual part inspection):\n" +
                "Test each screw, bolt, wire individually\n" +
                "Like: Test UserService.registerUser() method\n" +
                "Fast: Thousands per second\n" +
                "Isolated: Doesn't need database\n\n" +
                "INTEGRATION TESTS (Assembly line testing):\n" +
                "Test parts working together\n" +
                "Like: Test API endpoint → Service → Repository → Database\n" +
                "Slower: Needs real database\n" +
                "Realistic: Tests actual workflow\n\n" +
                "END-TO-END TESTS (Final product testing):\n" +
                "Test complete car on road\n" +
                "Like: Test frontend + backend + database together\n" +
                "Slowest: Requires browser automation\n" +
                "Comprehensive: Tests user experience\n\n" +
                "GOAL: Find defects before shipping!")
            .addTheory("Step 1: Repository Testing with @DataJpaTest",
                "Test database operations in isolation:\n\n" +
                "Create: src/test/java/com/yourname/taskmanager/repository/TaskRepositoryTest.java\n\n" +
                "package com.yourname.taskmanager.repository;\n\n" +
                "import com.yourname.taskmanager.model.Task;\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import org.junit.jupiter.api.BeforeEach;\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;\n" +
                "import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;\n" +
                "import java.util.List;\n" +
                "import static org.assertj.core.api.Assertions.assertThat;\n\n" +
                "@DataJpaTest  // Configures in-memory database\n" +
                "class TaskRepositoryTest {\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private TaskRepository taskRepository;\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private TestEntityManager entityManager;\n" +
                "    \n" +
                "    private User testUser;\n" +
                "    \n" +
                "    @BeforeEach\n" +
                "    void setUp() {\n" +
                "        // Create test user\n" +
                "        testUser = new User();\n" +
                "        testUser.setEmail(\"test@email.com\");\n" +
                "        testUser.setPassword(\"password\");\n" +
                "        testUser.setName(\"Test User\");\n" +
                "        entityManager.persist(testUser);\n" +
                "        entityManager.flush();\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldSaveTask() {\n" +
                "        // Given\n" +
                "        Task task = new Task(\"Buy milk\", \"From store\", testUser);\n" +
                "        \n" +
                "        // When\n" +
                "        Task saved = taskRepository.save(task);\n" +
                "        \n" +
                "        // Then\n" +
                "        assertThat(saved.getId()).isNotNull();\n" +
                "        assertThat(saved.getTitle()).isEqualTo(\"Buy milk\");\n" +
                "        assertThat(saved.getCompleted()).isFalse();\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldFindTasksByUserId() {\n" +
                "        // Given\n" +
                "        Task task1 = new Task(\"Task 1\", \"Desc 1\", testUser);\n" +
                "        Task task2 = new Task(\"Task 2\", \"Desc 2\", testUser);\n" +
                "        entityManager.persist(task1);\n" +
                "        entityManager.persist(task2);\n" +
                "        entityManager.flush();\n" +
                "        \n" +
                "        // When\n" +
                "        List<Task> tasks = taskRepository.findByUserId(testUser.getId());\n" +
                "        \n" +
                "        // Then\n" +
                "        assertThat(tasks).hasSize(2);\n" +
                "        assertThat(tasks).extracting(Task::getTitle)\n" +
                "            .containsExactlyInAnyOrder(\"Task 1\", \"Task 2\");\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldCountIncompleteTasks() {\n" +
                "        // Given\n" +
                "        Task completed = new Task(\"Done\", \"Finished\", testUser);\n" +
                "        completed.setCompleted(true);\n" +
                "        Task incomplete = new Task(\"Todo\", \"Not done\", testUser);\n" +
                "        entityManager.persist(completed);\n" +
                "        entityManager.persist(incomplete);\n" +
                "        entityManager.flush();\n" +
                "        \n" +
                "        // When\n" +
                "        Long count = taskRepository.countByUserIdAndCompletedFalse(testUser.getId());\n" +
                "        \n" +
                "        // Then\n" +
                "        assertThat(count).isEqualTo(1);\n" +
                "    }\n" +
                "}\n\n" +
                "KEY CONCEPTS:\n" +
                "@DataJpaTest - Auto-configures in-memory H2 database\n" +
                "TestEntityManager - Helps create test data\n" +
                "@BeforeEach - Runs before each test method\n" +
                "assertThat() - AssertJ fluent assertions (modern style)")
            .addTheory("Step 2: Service Testing with @SpringBootTest",
                "Test business logic with mocked dependencies:\n\n" +
                "Create: src/test/java/com/yourname/taskmanager/service/TaskServiceTest.java\n\n" +
                "package com.yourname.taskmanager.service;\n\n" +
                "import com.yourname.taskmanager.dto.TaskDTO;\n" +
                "import com.yourname.taskmanager.dto.TaskRequest;\n" +
                "import com.yourname.taskmanager.exception.ForbiddenException;\n" +
                "import com.yourname.taskmanager.exception.ResourceNotFoundException;\n" +
                "import com.yourname.taskmanager.model.Task;\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import com.yourname.taskmanager.repository.TaskRepository;\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import org.junit.jupiter.api.extension.ExtendWith;\n" +
                "import org.mockito.InjectMocks;\n" +
                "import org.mockito.Mock;\n" +
                "import org.mockito.junit.jupiter.MockitoExtension;\n" +
                "import java.util.Optional;\n" +
                "import static org.assertj.core.api.Assertions.assertThat;\n" +
                "import static org.assertj.core.api.Assertions.assertThatThrownBy;\n" +
                "import static org.mockito.ArgumentMatchers.any;\n" +
                "import static org.mockito.Mockito.*;\n\n" +
                "@ExtendWith(MockitoExtension.class)\n" +
                "class TaskServiceTest {\n" +
                "    \n" +
                "    @Mock\n" +
                "    private TaskRepository taskRepository;\n" +
                "    \n" +
                "    @InjectMocks\n" +
                "    private TaskService taskService;\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldCreateTask() {\n" +
                "        // Given\n" +
                "        TaskRequest request = new TaskRequest();\n" +
                "        request.setTitle(\"Buy milk\");\n" +
                "        request.setDescription(\"From store\");\n" +
                "        \n" +
                "        User user = new User();\n" +
                "        user.setId(1L);\n" +
                "        \n" +
                "        Task savedTask = new Task(\"Buy milk\", \"From store\", user);\n" +
                "        savedTask.setId(1L);\n" +
                "        \n" +
                "        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);\n" +
                "        \n" +
                "        // When\n" +
                "        TaskDTO result = taskService.createTask(request, 1L);\n" +
                "        \n" +
                "        // Then\n" +
                "        assertThat(result.getTitle()).isEqualTo(\"Buy milk\");\n" +
                "        assertThat(result.getCompleted()).isFalse();\n" +
                "        verify(taskRepository).save(any(Task.class));\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldThrowExceptionWhenTaskNotFound() {\n" +
                "        // Given\n" +
                "        when(taskRepository.findById(999L)).thenReturn(Optional.empty());\n" +
                "        \n" +
                "        // When/Then\n" +
                "        assertThatThrownBy(() -> taskService.getTaskById(999L, 1L))\n" +
                "            .isInstanceOf(ResourceNotFoundException.class)\n" +
                "            .hasMessageContaining(\"Task not found\");\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldThrowExceptionWhenUserNotOwner() {\n" +
                "        // Given\n" +
                "        User owner = new User();\n" +
                "        owner.setId(1L);\n" +
                "        \n" +
                "        Task task = new Task(\"Task\", \"Desc\", owner);\n" +
                "        task.setId(1L);\n" +
                "        \n" +
                "        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));\n" +
                "        \n" +
                "        // When/Then\n" +
                "        assertThatThrownBy(() -> taskService.getTaskById(1L, 2L))  // Different user!\n" +
                "            .isInstanceOf(ForbiddenException.class)\n" +
                "            .hasMessageContaining(\"permission\");\n" +
                "    }\n" +
                "}\n\n" +
                "KEY CONCEPTS:\n" +
                "@Mock - Creates mock object\n" +
                "@InjectMocks - Injects mocks into tested class\n" +
                "when().thenReturn() - Define mock behavior\n" +
                "verify() - Check method was called\n" +
                "assertThatThrownBy() - Test exceptions")
            .addTheory("Step 3: Controller Testing with @WebMvcTest",
                "Test REST endpoints with MockMvc (2024-2025 best practice):\n\n" +
                "Create: src/test/java/com/yourname/taskmanager/controller/TaskControllerTest.java\n\n" +
                "package com.yourname.taskmanager.controller;\n\n" +
                "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                "import com.yourname.taskmanager.dto.TaskDTO;\n" +
                "import com.yourname.taskmanager.dto.TaskRequest;\n" +
                "import com.yourname.taskmanager.service.TaskService;\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\n" +
                "import org.springframework.boot.test.mock.mockito.MockBean;\n" +
                "import org.springframework.http.MediaType;\n" +
                "import org.springframework.security.test.context.support.WithMockUser;\n" +
                "import org.springframework.test.web.servlet.MockMvc;\n" +
                "import java.time.LocalDateTime;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.List;\n" +
                "import static org.hamcrest.Matchers.hasSize;\n" +
                "import static org.mockito.ArgumentMatchers.any;\n" +
                "import static org.mockito.ArgumentMatchers.eq;\n" +
                "import static org.mockito.Mockito.when;\n" +
                "import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;\n" +
                "import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;\n\n" +
                "@WebMvcTest(TaskController.class)\n" +
                "class TaskControllerTest {\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private MockMvc mockMvc;\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private ObjectMapper objectMapper;\n" +
                "    \n" +
                "    @MockBean\n" +
                "    private TaskService taskService;\n" +
                "    \n" +
                "    @Test\n" +
                "    @WithMockUser(username = \"test@email.com\")\n" +
                "    void shouldGetAllTasks() throws Exception {\n" +
                "        // Given\n" +
                "        TaskDTO task1 = new TaskDTO(1L, \"Task 1\", \"Desc 1\", \n" +
                "                                     false, 1L, LocalDateTime.now(), LocalDateTime.now());\n" +
                "        TaskDTO task2 = new TaskDTO(2L, \"Task 2\", \"Desc 2\", \n" +
                "                                     true, 1L, LocalDateTime.now(), LocalDateTime.now());\n" +
                "        List<TaskDTO> tasks = Arrays.asList(task1, task2);\n" +
                "        \n" +
                "        when(taskService.getAllTasksForUser(any())).thenReturn(tasks);\n" +
                "        \n" +
                "        // When/Then\n" +
                "        mockMvc.perform(get(\"/api/tasks\"))\n" +
                "            .andExpect(status().isOk())\n" +
                "            .andExpect(jsonPath(\"$\", hasSize(2)))\n" +
                "            .andExpect(jsonPath(\"$[0].title\").value(\"Task 1\"))\n" +
                "            .andExpect(jsonPath(\"$[1].completed\").value(true));\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    @WithMockUser\n" +
                "    void shouldCreateTask() throws Exception {\n" +
                "        // Given\n" +
                "        TaskRequest request = new TaskRequest();\n" +
                "        request.setTitle(\"New Task\");\n" +
                "        request.setDescription(\"New Description\");\n" +
                "        \n" +
                "        TaskDTO created = new TaskDTO(1L, \"New Task\", \"New Description\",\n" +
                "                                      false, 1L, LocalDateTime.now(), LocalDateTime.now());\n" +
                "        \n" +
                "        when(taskService.createTask(any(TaskRequest.class), any()))\n" +
                "            .thenReturn(created);\n" +
                "        \n" +
                "        // When/Then\n" +
                "        mockMvc.perform(post(\"/api/tasks\")\n" +
                "                .contentType(MediaType.APPLICATION_JSON)\n" +
                "                .content(objectMapper.writeValueAsString(request)))\n" +
                "            .andExpect(status().isCreated())\n" +
                "            .andExpect(jsonPath(\"$.title\").value(\"New Task\"))\n" +
                "            .andExpect(jsonPath(\"$.completed\").value(false));\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    @WithMockUser\n" +
                "    void shouldReturnBadRequestForInvalidTask() throws Exception {\n" +
                "        // Given - Invalid request (no title)\n" +
                "        TaskRequest request = new TaskRequest();\n" +
                "        request.setDescription(\"Only description\");\n" +
                "        \n" +
                "        // When/Then\n" +
                "        mockMvc.perform(post(\"/api/tasks\")\n" +
                "                .contentType(MediaType.APPLICATION_JSON)\n" +
                "                .content(objectMapper.writeValueAsString(request)))\n" +
                "            .andExpect(status().isBadRequest());\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldReturn401WithoutAuthentication() throws Exception {\n" +
                "        // When/Then - No @WithMockUser annotation\n" +
                "        mockMvc.perform(get(\"/api/tasks\"))\n" +
                "            .andExpect(status().isUnauthorized());\n" +
                "    }\n" +
                "}\n\n" +
                "KEY CONCEPTS (2024-2025):\n" +
                "@WebMvcTest - Test only web layer\n" +
                "MockMvc - Simulate HTTP requests\n" +
                "@WithMockUser - Mock authenticated user\n" +
                "jsonPath() - Test JSON response\n" +
                "ObjectMapper - Convert objects to JSON")
            .addKeyPoint("Step 4: Integration Testing with @SpringBootTest",
                "Test complete application with real database:\n\n" +
                "@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)\n" +
                "@AutoConfigureTestDatabase\n" +
                "class TaskManagerIntegrationTest {\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private TestRestTemplate restTemplate;\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private TaskRepository taskRepository;\n" +
                "    \n" +
                "    @BeforeEach\n" +
                "    void setUp() {\n" +
                "        taskRepository.deleteAll();\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldRegisterAndLoginUser() {\n" +
                "        // Register\n" +
                "        RegisterRequest register = new RegisterRequest();\n" +
                "        register.setEmail(\"test@email.com\");\n" +
                "        register.setPassword(\"password123\");\n" +
                "        register.setName(\"Test User\");\n" +
                "        \n" +
                "        ResponseEntity<AuthResponse> registerResponse = \n" +
                "            restTemplate.postForEntity(\"/api/auth/register\", \n" +
                "                                      register, AuthResponse.class);\n" +
                "        \n" +
                "        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);\n" +
                "        \n" +
                "        // Login\n" +
                "        LoginRequest login = new LoginRequest();\n" +
                "        login.setEmail(\"test@email.com\");\n" +
                "        login.setPassword(\"password123\");\n" +
                "        \n" +
                "        ResponseEntity<AuthResponse> loginResponse = \n" +
                "            restTemplate.postForEntity(\"/api/auth/login\", \n" +
                "                                      login, AuthResponse.class);\n" +
                "        \n" +
                "        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldCreateAndRetrieveTask() {\n" +
                "        // Create authenticated request\n" +
                "        HttpHeaders headers = new HttpHeaders();\n" +
                "        headers.setBasicAuth(\"test@email.com\", \"password123\");\n" +
                "        \n" +
                "        TaskRequest request = new TaskRequest();\n" +
                "        request.setTitle(\"Integration Test Task\");\n" +
                "        \n" +
                "        HttpEntity<TaskRequest> entity = new HttpEntity<>(request, headers);\n" +
                "        \n" +
                "        // Create task\n" +
                "        ResponseEntity<TaskDTO> createResponse = \n" +
                "            restTemplate.exchange(\"/api/tasks\", HttpMethod.POST, \n" +
                "                                 entity, TaskDTO.class);\n" +
                "        \n" +
                "        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);\n" +
                "        \n" +
                "        // Retrieve tasks\n" +
                "        ResponseEntity<TaskDTO[]> getResponse = \n" +
                "            restTemplate.exchange(\"/api/tasks\", HttpMethod.GET, \n" +
                "                                 new HttpEntity<>(headers), TaskDTO[].class);\n" +
                "        \n" +
                "        assertThat(getResponse.getBody()).hasSize(1);\n" +
                "        assertThat(getResponse.getBody()[0].getTitle())\n" +
                "            .isEqualTo(\"Integration Test Task\");\n" +
                "    }\n" +
                "}\n\n" +
                "@SpringBootTest - Loads entire application\n" +
                "TestRestTemplate - Make real HTTP requests\n" +
                "RANDOM_PORT - Avoid port conflicts")
            .addTheory("Running Tests",
                "RUN ALL TESTS:\n\n" +
                "Command line:\n" +
                "./mvnw test\n\n" +
                "IntelliJ IDEA:\n" +
                "1. Right-click on src/test/java\n" +
                "2. Select \"Run 'All Tests'\"\n\n" +
                "VS Code:\n" +
                "1. Click Testing icon in sidebar\n" +
                "2. Click \"Run All Tests\"\n\n" +
                "RUN SPECIFIC TEST:\n" +
                "./mvnw test -Dtest=TaskServiceTest\n\n" +
                "CONTINUOUS TESTING:\n" +
                "./mvnw test --watch  # Reruns on file change\n\n" +
                "TEST COVERAGE:\n" +
                "./mvnw test jacoco:report\n" +
                "Open: target/site/jacoco/index.html\n\n" +
                "EXPECTED OUTPUT:\n" +
                "[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0\n" +
                "[INFO] BUILD SUCCESS\n\n" +
                "✓ ALL GREEN = Ready to deploy!")
            .addWarning("Common Testing Mistakes",
                "❌ MISTAKE 1: Not testing edge cases\n" +
                "Only testing happy path\n" +
                "FIX: Test null, empty, invalid, boundary values\n\n" +
                "❌ MISTAKE 2: Tests depend on each other\n" +
                "@Test void test1() { ... }\n" +
                "@Test void test2() { /* Assumes test1 ran */ }\n" +
                "FIX: Each test should be independent\n\n" +
                "❌ MISTAKE 3: Using real external services\n" +
                "Calling real payment API in tests\n" +
                "FIX: Mock external dependencies\n\n" +
                "❌ MISTAKE 4: No assertions\n" +
                "@Test void test() {\n" +
                "    service.doSomething();  // What's being tested?\n" +
                "}\n" +
                "FIX: Always have assertions\n\n" +
                "❌ MISTAKE 5: Testing too much in one test\n" +
                "100-line test method testing everything\n" +
                "FIX: One test per behavior\n\n" +
                "❌ MISTAKE 6: Ignoring failing tests\n" +
                "@Disabled  // \"Will fix later\" (never fixed)\n" +
                "FIX: Fix or delete the test\n\n" +
                "❌ MISTAKE 7: Not running tests before commit\n" +
                "Commit broken code\n" +
                "FIX: Always run ./mvnw test before git commit")
            .addKeyPoint("Best Practices Summary (2024-2025)",
                "TEST NAMING:\n" +
                "✓ shouldDoSomethingWhenCondition()\n" +
                "✓ givenCondition_whenAction_thenResult()\n" +
                "✓ Clear, descriptive names\n\n" +
                "TEST STRUCTURE (Given-When-Then):\n" +
                "// Given - Set up test data\n" +
                "Task task = new Task(...);\n" +
                "when(repo.save(task)).thenReturn(task);\n\n" +
                "// When - Execute action\n" +
                "TaskDTO result = service.createTask(request, userId);\n\n" +
                "// Then - Verify results\n" +
                "assertThat(result.getTitle()).isEqualTo(\"Task\");\n\n" +
                "ANNOTATIONS:\n" +
                "✓ @DataJpaTest - Repository tests\n" +
                "✓ @WebMvcTest - Controller tests\n" +
                "✓ @SpringBootTest - Integration tests\n" +
                "✓ @BeforeEach - Setup before each test\n" +
                "✓ @AfterEach - Cleanup after each test\n\n" +
                "ASSERTIONS:\n" +
                "✓ Use AssertJ (assertThat) over JUnit assertions\n" +
                "✓ Test one thing per assertion\n" +
                "✓ Include clear failure messages\n\n" +
                "COVERAGE GOALS:\n" +
                "✓ Aim for 70-80% code coverage\n" +
                "✓ 100% on critical business logic\n" +
                "✓ Focus on meaningful tests, not 100% coverage\n\n" +
                "SPEED:\n" +
                "✓ Unit tests: < 1 second\n" +
                "✓ Integration tests: < 5 seconds\n" +
                "✓ Use in-memory database (H2) for tests\n" +
                "✓ Keep test suite under 30 seconds total")
            .addChallenge(createTestTypeQuiz())
            .addChallenge(createMockQuiz())
            .addChallenge(createAssertionQuiz())
            .estimatedMinutes(55)
            .build();
    }

    private static Challenge createTestTypeQuiz() {
        return new Challenge.Builder("epoch-9-lesson-8-type", "Test Types", ChallengeType.MULTIPLE_CHOICE)
            .description("Which annotation is used for testing only the web layer?")
            .addMultipleChoiceOption("A) @SpringBootTest")
            .addMultipleChoiceOption("B) @DataJpaTest")
            .addMultipleChoiceOption("C) @WebMvcTest")
            .addMultipleChoiceOption("D) @IntegrationTest")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createMockQuiz() {
        return new Challenge.Builder("epoch-9-lesson-8-mock", "Mocking", ChallengeType.MULTIPLE_CHOICE)
            .description("What Mockito method defines mock behavior?")
            .addMultipleChoiceOption("A) mock()")
            .addMultipleChoiceOption("B) when().thenReturn()")
            .addMultipleChoiceOption("C) verify()")
            .addMultipleChoiceOption("D) @Mock")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createAssertionQuiz() {
        return new Challenge.Builder("epoch-9-lesson-8-assertion", "Assertions", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the recommended assertion library for modern tests?")
            .addMultipleChoiceOption("A) JUnit assertions")
            .addMultipleChoiceOption("B) Hamcrest")
            .addMultipleChoiceOption("C) AssertJ")
            .addMultipleChoiceOption("D) TestNG")
            .correctAnswer("C")
            .build();
    }
}
