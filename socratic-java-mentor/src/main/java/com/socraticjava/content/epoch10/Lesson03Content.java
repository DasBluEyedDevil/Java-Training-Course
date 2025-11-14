package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 10, Lesson 3: Pagination, Sorting, and Filtering APIs
 *
 * This lesson teaches professional approaches to handling large datasets
 * with pagination, sorting, and dynamic filtering in Spring Data JPA.
 */
public class Lesson03Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Pagination, Sorting, and Filtering APIs",
            """
            # Pagination, Sorting, and Filtering APIs

            ## Why These Features Matter

            Imagine Amazon displaying all 500 million products on a single page.
            Your browser would crash. The server would crash. The database would crash.

            Or imagine searching for "laptop" and getting results in completely random order,
            with no way to filter by price, brand, or rating.

            **Every application dealing with data needs:**
            - **Pagination**: Breaking large datasets into manageable chunks
            - **Sorting**: Ordering results by relevant criteria
            - **Filtering**: Narrowing results based on conditions

            Without these features:
            - Poor user experience (overwhelming amount of data)
            - Terrible performance (loading thousands of records)
            - High server costs (wasted bandwidth)
            - Database overload (expensive queries)
            - Slow page loads (users abandon site)

            ## The Analogy: Library System

            **Without pagination/sorting/filtering (bad library):**
            - All books dumped in giant pile
            - Want fiction? Dig through entire pile
            - No alphabetical order
            - Can't find anything efficiently
            - Takes hours to find one book

            **With pagination/sorting/filtering (good library):**
            - Books organized on shelves (paginated)
            - Sorted alphabetically by author
            - Filtered by genre, year, language
            - Can find any book in minutes
            - Pleasant, efficient experience

            Professional APIs provide the same organization for data.

            ## Understanding Pagination

            ### The Problem

            ```java
            // ❌ BAD: Returns all 1 million tasks!
            @GetMapping("/api/tasks")
            public List<Task> getAllTasks() {
                return taskRepository.findAll();  // Disaster!
            }
            ```

            This query:
            - Loads 1 million rows from database
            - Consumes gigabytes of memory
            - Takes minutes to execute
            - Crashes frontend trying to render
            - Times out before completion

            ### The Solution: Pagination

            ```java
            // ✅ GOOD: Returns page of 20 tasks
            @GetMapping("/api/tasks")
            public Page<TaskDTO> getTasks(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size
            ) {
                Pageable pageable = PageRequest.of(page, size);
                Page<Task> taskPage = taskRepository.findAll(pageable);
                return taskPage.map(this::convertToDTO);
            }
            ```

            **URL Examples:**
            ```
            GET /api/tasks?page=0&size=20  // First 20 tasks
            GET /api/tasks?page=1&size=20  // Next 20 tasks
            GET /api/tasks?page=2&size=50  // Third page with 50 tasks
            ```

            **Response Structure:**
            ```json
            {
              "content": [
                {"id": 1, "title": "Task 1"},
                {"id": 2, "title": "Task 2"}
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 20
              },
              "totalElements": 1000,
              "totalPages": 50,
              "last": false,
              "first": true,
              "numberOfElements": 20,
              "empty": false
            }
            ```

            ## Implementing Pagination in Spring Boot

            ### Step 1: Repository Setup

            ```java
            package com.example.repository;

            import com.example.model.Task;
            import org.springframework.data.jpa.repository.JpaRepository;
            import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

            public interface TaskRepository extends
                JpaRepository<Task, Long>,
                JpaSpecificationExecutor<Task> {  // For dynamic filtering
                // Pagination and sorting already provided by JpaRepository!
            }
            ```

            **Key interfaces:**
            - `JpaRepository` provides `findAll(Pageable)` automatically
            - `JpaSpecificationExecutor` enables dynamic filtering (covered later)

            ### Step 2: Controller with Pagination

            ```java
            package com.example.controller;

            import io.swagger.v3.oas.annotations.Parameter;
            import lombok.RequiredArgsConstructor;
            import org.springframework.data.domain.Page;
            import org.springframework.data.domain.PageRequest;
            import org.springframework.data.domain.Pageable;
            import org.springframework.http.ResponseEntity;
            import org.springframework.web.bind.annotation.*;

            @RestController
            @RequestMapping("/api/tasks")
            @RequiredArgsConstructor
            public class TaskController {

                private final TaskService taskService;

                @GetMapping
                public ResponseEntity<Page<TaskDTO>> getTasks(
                    @Parameter(description = "Page number (0-indexed)")
                    @RequestParam(defaultValue = "0") int page,

                    @Parameter(description = "Page size (max 100)")
                    @RequestParam(defaultValue = "20") int size
                ) {
                    // Validate and limit page size
                    if (size > 100) {
                        size = 100;  // Prevent excessive page sizes
                    }

                    Pageable pageable = PageRequest.of(page, size);
                    Page<TaskDTO> tasks = taskService.getTasks(pageable);

                    return ResponseEntity.ok(tasks);
                }
            }
            ```

            **Important:** Always limit maximum page size to prevent abuse.

            ### Step 3: Service Layer

            ```java
            package com.example.service;

            import lombok.RequiredArgsConstructor;
            import org.springframework.data.domain.Page;
            import org.springframework.data.domain.Pageable;
            import org.springframework.stereotype.Service;
            import org.springframework.transaction.annotation.Transactional;

            @Service
            @Transactional(readOnly = true)
            @RequiredArgsConstructor
            public class TaskService {

                private final TaskRepository taskRepository;

                public Page<TaskDTO> getTasks(Pageable pageable) {
                    Page<Task> taskPage = taskRepository.findAll(pageable);

                    // Map entities to DTOs
                    return taskPage.map(task -> TaskDTO.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .status(task.getStatus())
                        .createdAt(task.getCreatedAt())
                        .build());
                }
            }
            ```

            ## Adding Sorting

            ### Basic Sorting

            ```java
            @GetMapping
            public ResponseEntity<Page<TaskDTO>> getTasks(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size,

                @Parameter(description = "Sort by field")
                @RequestParam(defaultValue = "createdAt") String sortBy,

                @Parameter(description = "Sort direction (asc/desc)")
                @RequestParam(defaultValue = "desc") String direction
            ) {
                Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

                Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection, sortBy)
                );

                return ResponseEntity.ok(taskService.getTasks(pageable));
            }
            ```

            **URL Examples:**
            ```
            GET /api/tasks?sortBy=title&direction=asc      // Sort by title A-Z
            GET /api/tasks?sortBy=createdAt&direction=desc  // Newest first
            GET /api/tasks?sortBy=priority&direction=asc    // Low to high priority
            ```

            ### Multiple Sort Fields

            ```java
            // Sort by status (ascending), then by createdAt (descending)
            Sort sort = Sort.by(
                Sort.Order.asc("status"),
                Sort.Order.desc("createdAt")
            );

            Pageable pageable = PageRequest.of(page, size, sort);
            ```

            **URL Example:**
            ```
            GET /api/tasks?sort=status,asc&sort=createdAt,desc
            ```

            ### Using Spring's Built-in Pageable Parameter

            Spring can automatically parse pagination and sorting from URL parameters:

            ```java
            @GetMapping
            public ResponseEntity<Page<TaskDTO>> getTasks(Pageable pageable) {
                // Spring automatically creates Pageable from request parameters!
                return ResponseEntity.ok(taskService.getTasks(pageable));
            }
            ```

            **URL Examples:**
            ```
            GET /api/tasks?page=0&size=20
            GET /api/tasks?page=1&size=50&sort=title,asc
            GET /api/tasks?sort=status,asc&sort=createdAt,desc
            ```

            **Enable in Configuration:**
            ```java
            @Configuration
            public class WebConfig implements WebMvcConfigurer {
                @Override
                public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                    PageableHandlerMethodArgumentResolver resolver =
                        new PageableHandlerMethodArgumentResolver();
                    resolver.setMaxPageSize(100);  // Maximum allowed page size
                    resolver.setFallbackPageable(PageRequest.of(0, 20));  // Default
                    resolvers.add(resolver);
                }
            }
            ```

            ## Dynamic Filtering with Specifications

            ### The Problem: Fixed Query Methods

            ```java
            // Need separate method for each filter combination!
            List<Task> findByStatus(TaskStatus status);
            List<Task> findByPriority(Priority priority);
            List<Task> findByStatusAndPriority(TaskStatus status, Priority priority);
            List<Task> findByStatusAndPriorityAndTitleContaining(...);
            // This doesn't scale!
            ```

            ### The Solution: JPA Specifications

            Specifications allow building dynamic queries programmatically:

            ```java
            package com.example.specification;

            import com.example.model.Task;
            import com.example.model.TaskStatus;
            import com.example.model.Priority;
            import jakarta.persistence.criteria.Predicate;
            import org.springframework.data.jpa.domain.Specification;

            import java.time.LocalDateTime;
            import java.util.ArrayList;
            import java.util.List;

            public class TaskSpecification {

                /**
                 * Create dynamic specification based on filter criteria
                 */
                public static Specification<Task> withFilters(
                    TaskStatus status,
                    Priority priority,
                    String titleSearch,
                    LocalDateTime createdAfter,
                    LocalDateTime createdBefore,
                    Long userId
                ) {
                    return (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        // Filter by status
                        if (status != null) {
                            predicates.add(
                                criteriaBuilder.equal(root.get("status"), status)
                            );
                        }

                        // Filter by priority
                        if (priority != null) {
                            predicates.add(
                                criteriaBuilder.equal(root.get("priority"), priority)
                            );
                        }

                        // Search in title (case-insensitive)
                        if (titleSearch != null && !titleSearch.isBlank()) {
                            predicates.add(
                                criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("title")),
                                    "%" + titleSearch.toLowerCase() + "%"
                                )
                            );
                        }

                        // Filter by creation date range
                        if (createdAfter != null) {
                            predicates.add(
                                criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("createdAt"),
                                    createdAfter
                                )
                            );
                        }

                        if (createdBefore != null) {
                            predicates.add(
                                criteriaBuilder.lessThanOrEqualTo(
                                    root.get("createdAt"),
                                    createdBefore
                                )
                            );
                        }

                        // Filter by user (join)
                        if (userId != null) {
                            predicates.add(
                                criteriaBuilder.equal(
                                    root.join("user").get("id"),
                                    userId
                                )
                            );
                        }

                        // Combine all predicates with AND
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    };
                }
            }
            ```

            ### Controller with Filtering

            ```java
            @GetMapping
            public ResponseEntity<Page<TaskDTO>> getTasks(
                // Pagination and sorting
                Pageable pageable,

                // Filter parameters
                @RequestParam(required = false) TaskStatus status,
                @RequestParam(required = false) Priority priority,
                @RequestParam(required = false) String search,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime createdAfter,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime createdBefore,
                @RequestParam(required = false) Long userId
            ) {
                // Build specification
                Specification<Task> spec = TaskSpecification.withFilters(
                    status,
                    priority,
                    search,
                    createdAfter,
                    createdBefore,
                    userId
                );

                // Execute query with pagination, sorting, and filtering
                Page<TaskDTO> tasks = taskService.getTasks(spec, pageable);

                return ResponseEntity.ok(tasks);
            }
            ```

            **URL Examples:**
            ```
            # Basic pagination
            GET /api/tasks?page=0&size=20

            # With sorting
            GET /api/tasks?page=0&size=20&sort=createdAt,desc

            # With single filter
            GET /api/tasks?status=PENDING&page=0&size=20

            # With multiple filters
            GET /api/tasks?status=IN_PROGRESS&priority=HIGH&page=0&size=20

            # With search
            GET /api/tasks?search=documentation&page=0&size=20

            # With date range
            GET /api/tasks?createdAfter=2025-01-01T00:00:00&createdBefore=2025-01-31T23:59:59

            # Combined: filters + sorting + pagination
            GET /api/tasks?status=PENDING&priority=HIGH&search=urgent&sort=createdAt,desc&page=0&size=20
            ```

            ### Service with Specifications

            ```java
            @Service
            @Transactional(readOnly = true)
            @RequiredArgsConstructor
            public class TaskService {

                private final TaskRepository taskRepository;

                public Page<TaskDTO> getTasks(Specification<Task> spec, Pageable pageable) {
                    Page<Task> taskPage = taskRepository.findAll(spec, pageable);
                    return taskPage.map(this::convertToDTO);
                }

                private TaskDTO convertToDTO(Task task) {
                    return TaskDTO.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .status(task.getStatus())
                        .priority(task.getPriority())
                        .createdAt(task.getCreatedAt())
                        .build();
                }
            }
            ```

            ## Performance Considerations

            ### 1. Add Database Indexes

            ```sql
            -- Indexes for commonly sorted/filtered columns
            CREATE INDEX idx_tasks_status ON tasks(status);
            CREATE INDEX idx_tasks_priority ON tasks(priority);
            CREATE INDEX idx_tasks_created_at ON tasks(created_at);
            CREATE INDEX idx_tasks_user_id ON tasks(user_id);

            -- Composite index for common query patterns
            CREATE INDEX idx_tasks_user_status ON tasks(user_id, status);
            ```

            Without indexes, filtering and sorting can be extremely slow on large tables.

            ### 2. Use Page vs. Slice

            ```java
            // Page: Includes total count (extra COUNT query)
            Page<Task> page = taskRepository.findAll(pageable);
            long totalElements = page.getTotalElements();  // Requires COUNT(*)

            // Slice: No total count (faster, one query only)
            Slice<Task> slice = taskRepository.findAll(pageable);
            boolean hasNext = slice.hasNext();  // No COUNT(*) needed
            ```

            **When to use Slice:**
            - Infinite scroll UI (don't need total)
            - Large datasets where COUNT(*) is expensive
            - Mobile apps with "Load More" button

            **When to use Page:**
            - Need to show total page count
            - Traditional pagination UI with page numbers
            - Analytics/reports requiring totals

            ### 3. Limit Maximum Page Size

            ```java
            @GetMapping
            public ResponseEntity<Page<TaskDTO>> getTasks(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size
            ) {
                // Prevent abuse
                int maxSize = 100;
                if (size < 1) size = 20;
                if (size > maxSize) size = maxSize;

                Pageable pageable = PageRequest.of(page, size);
                return ResponseEntity.ok(taskService.getTasks(pageable));
            }
            ```

            ### 4. Avoid N+1 Query Problems

            ```java
            // ❌ BAD: N+1 queries (one for tasks, then one per task for user)
            Page<Task> tasks = taskRepository.findAll(pageable);
            tasks.forEach(task -> {
                String userName = task.getUser().getName();  // Lazy load!
            });

            // ✅ GOOD: Single query with JOIN FETCH
            @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE ...")
            Page<Task> findAllWithUser(Pageable pageable);
            ```

            ## Common Mistakes to Avoid

            ### ❌ Not Validating Sort Fields

            **Wrong:**
            ```java
            // User can sort by any field, including internal ones!
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            ```

            **Attack:**
            ```
            GET /api/tasks?sortBy=password  // Leak password hashes through timing!
            GET /api/tasks?sortBy=invalidField  // Cause exceptions
            ```

            **Right:**
            ```java
            private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
                "title", "status", "priority", "createdAt", "dueDate"
            );

            public Pageable createPageable(int page, int size, String sortBy, String direction) {
                // Validate sort field
                if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
                    sortBy = "createdAt";  // Default to safe field
                }

                Sort.Direction dir = direction.equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

                return PageRequest.of(page, size, Sort.by(dir, sortBy));
            }
            ```

            ### ❌ Building Dynamic SQL from User Input

            **Wrong (SQL Injection vulnerability!):**
            ```java
            // NEVER DO THIS!
            String query = "SELECT * FROM tasks WHERE title LIKE '%" + search + "%'";
            ```

            **Right:**
            ```java
            // Use Specifications (safe by design)
            Specification<Task> spec = (root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%");
            ```

            JPA Criteria API (used by Specifications) is parameterized by design.

            ### ❌ Not Setting Default Sort

            **Wrong:**
            ```java
            // No sort specified - database returns in arbitrary order
            PageRequest.of(page, size);  // Order unpredictable!
            ```

            **Problem:** Same page might return different results on different requests.

            **Right:**
            ```java
            // Always have a default sort
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            ```

            ### ❌ Not Handling Empty Results

            ```java
            Page<TaskDTO> tasks = taskService.getTasks(spec, pageable);

            // Check if empty
            if (tasks.isEmpty()) {
                // Return 200 with empty page, NOT 404
                // Empty results are valid, not an error
            }

            return ResponseEntity.ok(tasks);
            ```

            Empty pages should return 200 OK with empty content array, not 404.

            ## Frontend Integration

            ### React Example

            ```javascript
            const TaskList = () => {
                const [tasks, setTasks] = useState([]);
                const [page, setPage] = useState(0);
                const [totalPages, setTotalPages] = useState(0);
                const [loading, setLoading] = useState(false);

                // Filters
                const [status, setStatus] = useState('');
                const [search, setSearch] = useState('');
                const [sortBy, setSortBy] = useState('createdAt');
                const [sortDir, setSortDir] = useState('desc');

                const fetchTasks = async () => {
                    setLoading(true);

                    // Build query parameters
                    const params = new URLSearchParams({
                        page: page,
                        size: 20,
                        sort: `${sortBy},${sortDir}`
                    });

                    if (status) params.append('status', status);
                    if (search) params.append('search', search);

                    const response = await fetch(`/api/tasks?${params}`);
                    const data = await response.json();

                    setTasks(data.content);
                    setTotalPages(data.totalPages);
                    setLoading(false);
                };

                useEffect(() => {
                    fetchTasks();
                }, [page, status, search, sortBy, sortDir]);

                return (
                    <div>
                        {/* Filters */}
                        <input
                            type="text"
                            placeholder="Search..."
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />

                        <select value={status} onChange={(e) => setStatus(e.target.value)}>
                            <option value="">All</option>
                            <option value="PENDING">Pending</option>
                            <option value="IN_PROGRESS">In Progress</option>
                            <option value="COMPLETED">Completed</option>
                        </select>

                        {/* Task list */}
                        {loading ? <div>Loading...</div> : (
                            tasks.map(task => <TaskCard key={task.id} task={task} />)
                        )}

                        {/* Pagination */}
                        <div>
                            <button
                                disabled={page === 0}
                                onClick={() => setPage(page - 1)}
                            >
                                Previous
                            </button>

                            <span>Page {page + 1} of {totalPages}</span>

                            <button
                                disabled={page >= totalPages - 1}
                                onClick={() => setPage(page + 1)}
                            >
                                Next
                            </button>
                        </div>
                    </div>
                );
            };
            ```

            ## Summary

            Pagination, sorting, and filtering are essential for professional APIs:

            **Pagination:**
            - Use `Pageable` and `PageRequest` from Spring Data
            - Always set maximum page size (e.g., 100)
            - Return `Page<T>` with metadata (totalPages, totalElements)
            - Consider `Slice<T>` for infinite scroll (no total count)

            **Sorting:**
            - Accept sort field and direction parameters
            - Validate allowed sort fields (security!)
            - Always provide default sort order
            - Support multiple sort criteria

            **Filtering:**
            - Use JPA Specifications for dynamic queries
            - Never build SQL strings from user input
            - Add database indexes for filter columns
            - Combine filters with AND logic

            **Performance:**
            - Index frequently queried/sorted columns
            - Use Slice when total count not needed
            - Avoid N+1 queries with JOIN FETCH
            - Limit maximum page sizes

            **Security:**
            - Validate all user inputs
            - Whitelist allowed sort fields
            - Use parameterized queries (Specifications)
            - Rate limit API endpoints

            These patterns transform APIs from returning massive datasets to providing
            efficient, user-friendly access to exactly the data users need.
            """,
            55  // Estimated minutes to complete
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        QuizQuestion question = new QuizQuestion(
            "Your API allows users to request paginated tasks with sorting. A malicious user sends: GET /api/tasks?sortBy=password&direction=asc. What is the PRIMARY security concern?",
            "B",
            """
            This question tests understanding of a subtle but serious security vulnerability
            in pagination and sorting implementations: information leakage through timing attacks
            and error messages when users can sort by arbitrary fields.

            The correct answer is B. Allowing sorting by any field can leak sensitive information
            through timing attacks, error messages, or actual data exposure. This is a real
            security vulnerability that many developers overlook.

            **How the attack works:**

            **Timing Attack Example:**
            ```java
            // If password field exists and database performs actual sort:
            GET /api/tasks?sortBy=password&direction=asc

            Database executes: SELECT * FROM tasks ORDER BY password ASC LIMIT 20

            Response times might reveal information:
            - Sorting by valid field (password): 50ms
            - Sorting by invalid field: 10ms (immediate error)

            Attacker learns: password field exists!
            ```

            **Error Message Leakage:**
            ```
            GET /api/tasks?sortBy=secretInternalField

            Error: "Column 'secretInternalField' not found in tasks table"

            Attacker learns database schema!
            ```

            **Direct Data Exposure:**
            If your DTO accidentally includes sensitive fields:
            ```java
            // If TaskDTO has password field (bad practice, but happens):
            GET /api/tasks?sortBy=password

            Response orders tasks by password hash
            Even if hash not shown, order might leak information
            ```

            **The Solution:**

            Whitelist allowed sort fields:
            ```java
            private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
                "title",
                "status",
                "priority",
                "createdAt",
                "dueDate"
                // Only safe, public fields!
            );

            public Pageable createPageable(int page, int size, String sortBy, String direction) {
                // Validate sort field
                if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
                    // Don't leak info about why it's invalid
                    sortBy = "createdAt";  // Silently default to safe field
                    // OR throw generic error: "Invalid sort parameter"
                }

                Sort.Direction dir = "asc".equalsIgnoreCase(direction)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

                return PageRequest.of(page, size, Sort.by(dir, sortBy));
            }
            ```

            Why other answers are less critical:

            A is a concern but NOT the primary security issue. Yes, allowing arbitrary sorting
            could be used for DoS if someone sorts by unindexed columns on huge tables:
            ```
            GET /api/tasks?sortBy=description  // No index, full table scan!
            ```

            But this is:
            - Performance problem, not direct security vulnerability
            - Mitigated by proper indexing
            - Less severe than information disclosure
            - Usually caught by performance monitoring

            The primary security concern is information leakage, not performance degradation.

            C is wrong because SQL injection through sort parameters is actually PREVENTED
            by using Spring Data JPA with Pageable:

            ```java
            // This is SAFE - Spring uses parameterized queries:
            PageRequest.of(page, size, Sort.by(sortBy))

            // Behind the scenes Spring generates:
            // SELECT * FROM tasks ORDER BY :sortField  (parameterized)
            // NOT: "SELECT * FROM tasks ORDER BY " + sortBy  (vulnerable)
            ```

            JPA Criteria API and Spring Data's Sort mechanism don't concatenate strings
            to build SQL, so SQL injection isn't possible through standard sort parameters.

            **Exception:** If you manually build SQL queries with string concatenation:
            ```java
            // VULNERABLE:
            String sql = "SELECT * FROM tasks ORDER BY " + sortBy;  // DON'T DO THIS!
            ```

            But this violates Spring Data best practices. Using Pageable correctly is safe
            from SQL injection.

            D is wrong because pagination parameters (page number and size) have much less
            security impact than sort field:

            ```
            GET /api/tasks?page=999999999
            ```

            This might:
            - Return empty page (no data)
            - Cause performance hit (seeking to high page)
            - Waste some resources

            But it doesn't:
            - Leak sensitive information
            - Expose internal schema
            - Enable timing attacks
            - Allow arbitrary code execution

            You should still validate page size (max 100) to prevent resource exhaustion,
            but this is operational concern, not primary security vulnerability.

            **Real-world impact:**

            1. **Schema Discovery:**
            Attacker iterates through common field names to discover database schema:
            ```
            GET /api/tasks?sortBy=email       → Error reveals field exists
            GET /api/tasks?sortBy=ssn         → Error reveals field exists
            GET /api/tasks?sortBy=creditCard  → Error reveals field exists
            ```

            2. **Account Enumeration:**
            ```
            GET /api/users?sortBy=username&search=admin
            Timing differences reveal if 'admin' user exists
            ```

            3. **GDPR Violations:**
            If sorting by PII fields (even if not returned in response), logs might
            capture these queries, creating compliance issues.

            **Security checklist for pagination/sorting:**
            - ✅ Whitelist allowed sort fields
            - ✅ Never expose internal/sensitive field names
            - ✅ Return generic errors for invalid parameters
            - ✅ Add rate limiting to prevent automated scanning
            - ✅ Log and alert on suspicious sort patterns
            - ✅ Use DTOs that only expose safe fields
            - ✅ Regular security audits of exposed fields
            """
        );

        question.addChoice("A", "Sorting by password might cause performance issues by creating an expensive database operation");
        question.addChoice("B", "It could leak information about database schema or enable timing attacks on sensitive fields");
        question.addChoice("C", "The sortBy parameter is vulnerable to SQL injection attacks");
        question.addChoice("D", "Large page numbers in pagination parameters could cause denial of service");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "You need to implement pagination for a table with 50 million rows. Users want to jump to page 10,000 (with 100 items per page). What's the MAIN performance problem and solution?",
            "D",
            """
            This question tests deep understanding of database pagination performance at scale.
            Traditional OFFSET pagination becomes extremely slow on high page numbers with
            large datasets—a problem many developers don't discover until production.

            The correct answer is D. OFFSET requires the database to skip millions of rows,
            which is slow. Cursor-based (keyset) pagination is much faster for deep pages.

            **Understanding the Problem:**

            **Traditional OFFSET Pagination:**
            ```sql
            -- Page 10,000 with 100 items per page
            SELECT * FROM tasks
            ORDER BY id
            LIMIT 100 OFFSET 1000000;  -- Skip first 1 million rows!
            ```

            What the database must do:
            1. Scan index to find matching rows
            2. Skip first 1,000,000 rows (reads them, then discards!)
            3. Return next 100 rows

            **Performance at Different Pages:**
            ```
            Page 1:      OFFSET 0       →  10ms   ✅ Fast
            Page 100:    OFFSET 10,000  →  50ms   ✅ OK
            Page 1,000:  OFFSET 100,000 →  500ms  ⚠️ Slow
            Page 10,000: OFFSET 1M      →  5000ms ❌ Unusable!
            ```

            On a 50 million row table, page 10,000 might take 30+ seconds!

            **Why It's Slow:**
            - Database can't jump directly to row 1,000,000
            - Must read and discard all previous rows
            - Even with indexes, this is O(n) complexity
            - Each deep page gets slower
            - No way to efficiently "skip" in most databases

            **The Solution: Cursor-Based (Keyset) Pagination:**

            Instead of OFFSET, use a "cursor" (last seen ID):

            ```sql
            -- First page
            SELECT * FROM tasks
            WHERE id > 0
            ORDER BY id ASC
            LIMIT 100;

            -- Returns tasks with id 1-100
            -- Last id seen: 100

            -- Next page (use last seen ID as cursor)
            SELECT * FROM tasks
            WHERE id > 100
            ORDER BY id ASC
            LIMIT 100;

            -- Returns tasks 101-200
            -- Last id seen: 200

            -- Next page
            SELECT * FROM tasks
            WHERE id > 200
            ORDER BY id ASC
            LIMIT 100;
            ```

            **Performance Comparison:**
            ```
            OFFSET-based:
            Page 1:      10ms
            Page 10,000: 5000ms  ← Gets progressively slower

            Cursor-based:
            Page 1:      10ms
            Page 10,000: 10ms    ← Consistently fast!
            ```

            **Spring Implementation:**

            ```java
            // DTO with cursor
            public class TaskPageResponse {
                private List<TaskDTO> content;
                private Long nextCursor;  // ID of last item
                private boolean hasNext;
            }

            // Controller
            @GetMapping
            public ResponseEntity<TaskPageResponse> getTasks(
                @RequestParam(required = false) Long cursor,
                @RequestParam(defaultValue = "100") int size
            ) {
                List<Task> tasks = taskService.getTasksAfterCursor(cursor, size + 1);

                boolean hasNext = tasks.size() > size;
                if (hasNext) {
                    tasks = tasks.subList(0, size);  // Remove extra item
                }

                Long nextCursor = tasks.isEmpty() ? null : tasks.get(tasks.size() - 1).getId();

                TaskPageResponse response = TaskPageResponse.builder()
                    .content(tasks.stream().map(this::toDTO).collect(Collectors.toList()))
                    .nextCursor(nextCursor)
                    .hasNext(hasNext)
                    .build();

                return ResponseEntity.ok(response);
            }

            // Repository
            @Query("SELECT t FROM Task t WHERE t.id > :cursor ORDER BY t.id ASC")
            List<Task> findAfterCursor(@Param("cursor") Long cursor, Pageable pageable);

            // Service
            public List<Task> getTasksAfterCursor(Long cursor, int size) {
                if (cursor == null) cursor = 0L;  // First page

                return taskRepository.findAfterCursor(
                    cursor,
                    PageRequest.of(0, size)
                );
            }
            ```

            **Frontend Usage:**
            ```javascript
            // First request
            GET /api/tasks?size=100
            Response: { content: [...], nextCursor: 100, hasNext: true }

            // Next page
            GET /api/tasks?cursor=100&size=100
            Response: { content: [...], nextCursor: 200, hasNext: true }

            // Continue until hasNext = false
            ```

            **Advantages:**
            - O(1) complexity - always fast regardless of page depth
            - Uses index efficiently (id > cursor)
            - Consistent performance even at page 100,000
            - Handles concurrent inserts better
            - Lower database load

            **Limitations:**
            - Can't jump to arbitrary page number
            - Only supports forward navigation
            - Cursor depends on sort order
            - More complex to implement with multi-field sorting

            Why other answers are incorrect:

            A is wrong because LIMIT 100 is NOT the problem. Returning 100 rows is fast—
            the problem is OFFSET skipping millions of rows to get there. Reducing page
            size from 100 to 20 doesn't help:

            ```sql
            -- Still has to skip 1 million rows!
            SELECT * FROM tasks
            LIMIT 20 OFFSET 1000000;  -- Still slow!
            ```

            Smaller page size actually makes it WORSE:
            - Users need MORE pages to reach same data
            - Page 50,000 with size=20 is slower than page 10,000 with size=100

            B is wrong because JOIN performance is NOT the primary issue here. Yes, JOINs
            add overhead, but the OFFSET problem is far more severe:

            ```
            Without JOIN + OFFSET 1M: 5000ms  ← OFFSET is the problem
            With JOIN + OFFSET 1M:    5500ms  ← JOIN adds marginal overhead

            With JOIN + cursor:       15ms    ← Fast with cursor
            Without JOIN + cursor:    10ms
            ```

            The 500ms JOIN overhead is negligible compared to 5000ms OFFSET overhead.

            Also, removing JOINs means N+1 queries in application code:
            ```java
            // Worse than single JOIN:
            List<Task> tasks = taskRepo.findAll();  // 1 query
            tasks.forEach(task -> {
                User user = userRepo.findById(task.getUserId());  // N queries!
            });
            ```

            C is wrong because this is NOT an index problem. Sorting by id DESC doesn't
            fundamentally solve OFFSET performance issues:

            ```sql
            -- Reverse order still has OFFSET problem
            SELECT * FROM tasks
            ORDER BY id DESC  -- Index still exists for DESC
            LIMIT 100 OFFSET 1000000;  -- Still slow!
            ```

            Database indexes work for both ASC and DESC. The problem is OFFSET, not sort order.

            **When to use each pagination strategy:**

            **OFFSET Pagination (Page):**
            - Small to medium datasets (< 100,000 rows)
            - Need to jump to specific page numbers
            - UI shows page numbers (1, 2, 3... 10)
            - Users rarely go past page 10

            **Cursor Pagination (Slice):**
            - Large datasets (millions of rows)
            - Users need to scroll deeply
            - Infinite scroll UI
            - Mobile apps with "Load More"
            - Real-time feeds (Twitter, Facebook)
            - "Show more" button interfaces

            **Hybrid Approach:**
            ```java
            // Use OFFSET for first 100 pages
            if (page < 100) {
                return taskRepo.findAll(PageRequest.of(page, size));
            }

            // Switch to cursor for deep pages
            return taskRepo.findAfterCursor(cursor, size);
            ```

            This gives best of both worlds: traditional pagination for early pages where
            users need it, cursor pagination for deep pages where performance matters.
            """
        );

        question.addChoice("A", "LIMIT 100 is too large, should use smaller page sizes like 20");
        question.addChoice("B", "Database JOIN operations become expensive, should load related entities separately");
        question.addChoice("C", "Missing index on id field, should create index for sorting");
        question.addChoice("D", "OFFSET requires skipping millions of rows (slow), should use cursor-based (keyset) pagination");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "When implementing dynamic filtering with JPA Specifications, which approach is MOST important for preventing N+1 query problems?",
            "B",
            """
            This question tests understanding of a critical performance problem in JPA:
            N+1 queries, which can turn a single database query into hundreds or thousands
            of queries, devastating application performance.

            The correct answer is B. Use JOIN FETCH in specifications to eagerly load
            related entities in a single query, preventing lazy loading from triggering
            multiple separate queries.

            **Understanding the N+1 Problem:**

            **The Setup:**
            ```java
            @Entity
            public class Task {
                @Id
                private Long id;
                private String title;

                @ManyToOne(fetch = FetchType.LAZY)  // Lazy by default
                private User user;
            }
            ```

            **The Problem Code:**
            ```java
            // Controller
            @GetMapping
            public ResponseEntity<List<TaskDTO>> getTasks() {
                List<Task> tasks = taskRepository.findAll();  // 1 query

                return tasks.stream()
                    .map(task -> TaskDTO.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .userName(task.getUser().getName())  // N queries!
                        .build())
                    .collect(Collectors.toList());
            }
            ```

            **Database Queries Executed:**
            ```sql
            -- Query 1: Get all tasks
            SELECT * FROM tasks;  -- Returns 100 tasks

            -- Queries 2-101: Get user for each task (lazy loading triggered!)
            SELECT * FROM users WHERE id = 1;
            SELECT * FROM users WHERE id = 2;
            SELECT * FROM users WHERE id = 3;
            ...
            SELECT * FROM users WHERE id = 100;

            -- Total: 101 queries for 100 tasks!
            ```

            **Performance Impact:**
            ```
            100 tasks:    101 queries  →  500ms
            1,000 tasks:  1,001 queries →  5,000ms
            10,000 tasks: 10,001 queries → 50,000ms (50 seconds!)
            ```

            This scales linearly with data size and can bring applications to a halt.

            **The Solution: JOIN FETCH in Specification:**

            ```java
            public class TaskSpecification {

                public static Specification<Task> withFiltersAndUser(
                    TaskStatus status,
                    Priority priority,
                    String titleSearch
                ) {
                    return (root, query, criteriaBuilder) -> {
                        // Eagerly fetch user to prevent lazy loading
                        root.fetch("user", JoinType.LEFT);  // ← Key solution!

                        List<Predicate> predicates = new ArrayList<>();

                        if (status != null) {
                            predicates.add(
                                criteriaBuilder.equal(root.get("status"), status)
                            );
                        }

                        if (priority != null) {
                            predicates.add(
                                criteriaBuilder.equal(root.get("priority"), priority)
                            );
                        }

                        if (titleSearch != null && !titleSearch.isBlank()) {
                            predicates.add(
                                criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("title")),
                                    "%" + titleSearch.toLowerCase() + "%"
                                )
                            );
                        }

                        // Ensure we don't return duplicate rows from JOIN
                        query.distinct(true);

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    };
                }
            }
            ```

            **Or with @Query in Repository:**
            ```java
            @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE ...")
            List<Task> findAllWithUser(Pageable pageable);
            ```

            **Result:**
            ```sql
            -- Single query with JOIN
            SELECT t.*, u.*
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.status = 'PENDING';

            -- Total: 1 query for 100 tasks! ✅
            ```

            **Performance Improvement:**
            ```
            Before (N+1):  101 queries, 500ms
            After (FETCH): 1 query,   10ms

            50x faster! 🚀
            ```

            Why other answers don't solve N+1:

            A is wrong because eager fetching at the entity level is too broad and causes
            other problems:

            ```java
            @Entity
            public class Task {
                @ManyToOne(fetch = FetchType.EAGER)  // ❌ Always fetches user
                private User user;
            }
            ```

            Problems:
            - Fetches user ALWAYS, even when not needed
            - Can't control when to fetch vs. not fetch
            - Might trigger additional eager fetches in User entity
            - Creates "eager fetch chain" across entire object graph
            - Wastes bandwidth and memory
            - Makes caching less effective

            Example:
            ```java
            // Just need task IDs for dashboard
            List<Long> taskIds = taskRepo.findAll()  // Still fetches all users!
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
            ```

            **Better:** Keep LAZY at entity level, use JOIN FETCH selectively when needed.

            C is wrong because @Transactional(readOnly = true) does NOT prevent N+1 queries.
            This annotation:

            **What it does:**
            - Hints to database that transaction is read-only (performance optimization)
            - Tells Hibernate not to flush changes
            - Potentially allows database to use read replicas

            **What it does NOT do:**
            - Does NOT prevent lazy loading
            - Does NOT prevent N+1 queries
            - Does NOT change fetch strategies

            With @Transactional(readOnly = true):
            ```java
            @Transactional(readOnly = true)
            public List<TaskDTO> getTasks() {
                List<Task> tasks = taskRepo.findAll();  // 1 query

                return tasks.stream()
                    .map(task -> TaskDTO.builder()
                        .userName(task.getUser().getName())  // Still N queries!
                        .build())
                    .collect(Collectors.toList());
            }
            // Total: Still 101 queries! ❌
            ```

            The transaction keeps the session open, which actually ENABLES lazy loading.
            Without @Transactional, you'd get LazyInitializationException instead.

            D is wrong because batch fetching (hibernate.jdbc.batch_size) reduces but
            doesn't eliminate the N+1 problem:

            ```properties
            spring.jpa.properties.hibernate.jdbc.batch_size=10
            ```

            **What happens:**
            ```sql
            -- Query 1: Get tasks
            SELECT * FROM tasks;  -- 100 tasks

            -- Queries 2-11: Get users in batches of 10
            SELECT * FROM users WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            SELECT * FROM users WHERE id IN (11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
            ...
            SELECT * FROM users WHERE id IN (91, 92, 93, 94, 95, 96, 97, 98, 99, 100);

            -- Total: 11 queries (better than 101, but not as good as 1!)
            ```

            **Performance:**
            ```
            No batching:    101 queries, 500ms
            With batching:  11 queries,  100ms   ← Better but not optimal
            JOIN FETCH:     1 query,     10ms    ← Best!
            ```

            Batch fetching:
            - Still requires multiple round trips to database
            - More complex to configure
            - Doesn't work well with pagination
            - Band-aid solution, not proper fix

            **Detecting N+1 Queries:**

            Enable query logging:
            ```yaml
            spring:
              jpa:
                show-sql: true
                properties:
                  hibernate:
                    format_sql: true
                    use_sql_comments: true
            ```

            Or use performance monitoring tools:
            ```java
            // Hibernate statistics
            @Configuration
            public class HibernateConfig {
                @Bean
                public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
                    return hibernateProperties -> {
                        hibernateProperties.put("hibernate.generate_statistics", true);
                    };
                }
            }
            ```

            **Best Practice Pattern:**
            ```java
            // Separate specifications for different use cases

            // When you need users
            Specification<Task> withUser = (root, query, cb) -> {
                root.fetch("user", JoinType.LEFT);
                query.distinct(true);
                return null;
            };

            // When you don't need users
            Specification<Task> taskOnly = (root, query, cb) -> null;

            // Combine as needed
            Specification<Task> spec = Specification.where(withUser)
                .and(TaskSpecification.withFilters(status, priority, search));

            Page<Task> tasks = taskRepo.findAll(spec, pageable);
            ```

            N+1 queries are one of the most common and devastating performance problems
            in JPA applications. JOIN FETCH is the proper solution for eager loading
            related entities when needed.
            """
        );

        question.addChoice("A", "Set fetch = FetchType.EAGER on all @ManyToOne and @OneToMany relationships");
        question.addChoice("B", "Use JOIN FETCH in specifications to eagerly load related entities when needed");
        question.addChoice("C", "Always use @Transactional(readOnly = true) on service methods");
        question.addChoice("D", "Configure hibernate.jdbc.batch_size to batch load related entities");

        return question;
    }
}
