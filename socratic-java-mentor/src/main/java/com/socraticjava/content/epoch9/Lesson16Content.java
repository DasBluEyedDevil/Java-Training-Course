package com.socraticjava.content.epoch9;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 9, Lesson 16: Asynchronous Processing and Background Jobs
 *
 * This lesson teaches async processing, background jobs, and scheduled tasks
 * for building responsive, scalable applications.
 */
public class Lesson16Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Asynchronous Processing and Background Jobs",
            """
            # Asynchronous Processing and Background Jobs

            ## Why Async Processing Matters

            Imagine ordering food at a restaurant. The waiter takes your order, then stands
            at your table waiting while the chef cooks your meal. No one else can order.
            The restaurant serves one customer per hour.

            That's **synchronous processing**—one task at a time, blocking everything else.

            Now imagine the waiter takes your order, gives it to the kitchen, and immediately
            helps other customers while your food cooks. Everyone gets served efficiently.

            That's **asynchronous processing**—tasks run in the background, freeing up
            resources to handle other work.

            **Without async processing:**
            - Sending email blocks HTTP request (5 seconds)
            - Generating reports freezes API (30 seconds)
            - Processing images makes user wait (10 seconds)
            - Server can only handle 10 requests/second
            - Users experience slow, unresponsive application

            **With async processing:**
            - Email sent in background, user gets immediate response
            - Reports generated asynchronously, user notified when done
            - Images processed while user continues browsing
            - Server handles 100+ requests/second
            - Fast, responsive user experience

            ## The Analogy: Restaurant Kitchen

            **Synchronous (One chef, one dish at a time):**
            - Chef starts cooking steak
            - Waits for steak to cook (15 minutes)
            - Can't start anything else
            - Next order waits 15 minutes
            - Restaurant serves 4 customers per hour

            **Asynchronous (Chef with multiple burners):**
            - Chef starts steak on burner 1
            - Immediately starts pasta on burner 2
            - Begins salad prep while both cook
            - All three ready in 15 minutes
            - Restaurant serves 12 customers per hour

            Professional applications work the same way—multiple tasks cooking at once.

            ## Enabling Async in Spring Boot

            ### Step 1: Enable Async Support

            ```java
            package com.example.config;

            import org.springframework.context.annotation.Configuration;
            import org.springframework.scheduling.annotation.EnableAsync;

            @Configuration
            @EnableAsync  // Enable async method execution
            public class AsyncConfig {
            }
            ```

            ### Step 2: Configure Custom Thread Pool (Production Best Practice)

            ```java
            package com.example.config;

            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.scheduling.annotation.EnableAsync;
            import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

            import java.util.concurrent.Executor;

            @Configuration
            @EnableAsync
            public class AsyncConfig {

                /**
                 * Custom thread pool for async tasks
                 * Production best practice: Don't use default SimpleAsyncTaskExecutor
                 */
                @Bean(name = "taskExecutor")
                public Executor taskExecutor() {
                    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

                    // Core pool size: minimum threads always alive
                    executor.setCorePoolSize(5);

                    // Max pool size: maximum threads under load
                    executor.setMaxPoolSize(10);

                    // Queue capacity: tasks waiting for threads
                    executor.setQueueCapacity(100);

                    // Thread name prefix for debugging
                    executor.setThreadNamePrefix("async-task-");

                    // Rejection policy when queue is full
                    executor.setRejectedExecutionHandler(
                        new ThreadPoolExecutor.CallerRunsPolicy()
                    );

                    // Wait for tasks to complete on shutdown
                    executor.setWaitForTasksToCompleteOnShutdown(true);
                    executor.setAwaitTerminationSeconds(60);

                    executor.initialize();
                    return executor;
                }
            }
            ```

            **Configuration explained:**
            - **corePoolSize(5)**: Always keep 5 threads alive
            - **maxPoolSize(10)**: Scale up to 10 threads under heavy load
            - **queueCapacity(100)**: Queue up to 100 tasks waiting for threads
            - **CallerRunsPolicy**: If queue full, run task in calling thread (backpressure)

            ## Using @Async

            ### Basic Async Method (Fire and Forget)

            ```java
            package com.example.service;

            import lombok.RequiredArgsConstructor;
            import lombok.extern.slf4j.Slf4j;
            import org.springframework.scheduling.annotation.Async;
            import org.springframework.stereotype.Service;

            @Service
            @RequiredArgsConstructor
            @Slf4j
            public class EmailService {

                private final EmailClient emailClient;

                /**
                 * Send email asynchronously
                 * Returns immediately, email sent in background
                 */
                @Async("taskExecutor")  // Use our custom thread pool
                public void sendWelcomeEmail(String email, String name) {
                    log.info("Sending welcome email to {} on thread {}",
                        email, Thread.currentThread().getName());

                    try {
                        // Simulate slow email sending
                        Thread.sleep(3000);  // 3 seconds

                        emailClient.send(email, "Welcome!", "Hello " + name + "!");

                        log.info("Welcome email sent to {}", email);
                    } catch (Exception e) {
                        log.error("Failed to send welcome email to {}", email, e);
                        // Handle error (maybe retry, or store for manual review)
                    }
                }
            }
            ```

            **Usage:**
            ```java
            @RestController
            @RequestMapping("/api/users")
            @RequiredArgsConstructor
            public class UserController {

                private final UserService userService;
                private final EmailService emailService;

                @PostMapping("/register")
                public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
                    // Create user (synchronous)
                    UserDTO user = userService.createUser(request);

                    // Send email (asynchronous - returns immediately)
                    emailService.sendWelcomeEmail(user.getEmail(), user.getName());

                    // Return response without waiting for email
                    return ResponseEntity.status(HttpStatus.CREATED).body(user);
                }
            }
            ```

            **Flow:**
            ```
            0ms  - HTTP Request received
            50ms - User created in database
            51ms - emailService.sendWelcomeEmail() called
            52ms - HTTP Response returned ✅ (User doesn't wait!)
            3052ms - Email actually sent (in background)
            ```

            ### Async with Return Value (CompletableFuture)

            ```java
            @Service
            @Slf4j
            public class ReportService {

                /**
                 * Generate report asynchronously
                 * Returns CompletableFuture so caller can wait or chain operations
                 */
                @Async("taskExecutor")
                public CompletableFuture<Report> generateSalesReport(Long userId, LocalDate date) {
                    log.info("Generating sales report for user {} on thread {}",
                        userId, Thread.currentThread().getName());

                    try {
                        // Expensive computation
                        List<Sale> sales = salesRepository.findByUserIdAndDate(userId, date);
                        BigDecimal totalRevenue = sales.stream()
                            .map(Sale::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                        Report report = Report.builder()
                            .userId(userId)
                            .date(date)
                            .totalSales(sales.size())
                            .totalRevenue(totalRevenue)
                            .build();

                        log.info("Sales report generated for user {}", userId);

                        return CompletableFuture.completedFuture(report);

                    } catch (Exception e) {
                        log.error("Failed to generate report for user {}", userId, e);
                        return CompletableFuture.failedFuture(e);
                    }
                }
            }
            ```

            **Usage - Wait for Result:**
            ```java
            @GetMapping("/reports/sales")
            public ResponseEntity<Report> getSalesReport(
                @RequestParam Long userId,
                @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
            ) throws ExecutionException, InterruptedException {

                // Start async task
                CompletableFuture<Report> futureReport =
                    reportService.generateSalesReport(userId, date);

                // Wait for completion (blocks)
                Report report = futureReport.get();

                return ResponseEntity.ok(report);
            }
            ```

            **Usage - Non-blocking with Callback:**
            ```java
            @GetMapping("/reports/sales-async")
            public ResponseEntity<Void> requestSalesReport(
                @RequestParam Long userId,
                @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
            ) {
                // Start async task
                reportService.generateSalesReport(userId, date)
                    .thenAccept(report -> {
                        // Called when report is ready
                        log.info("Report ready: {}", report);
                        notificationService.notifyUser(userId,
                            "Your sales report is ready!");
                    })
                    .exceptionally(error -> {
                        // Called if generation fails
                        log.error("Report generation failed", error);
                        notificationService.notifyUser(userId,
                            "Report generation failed. Please try again.");
                        return null;
                    });

                // Return immediately
                return ResponseEntity.accepted().build();  // 202 Accepted
            }
            ```

            ## Parallel Async Execution

            Execute multiple independent tasks in parallel:

            ```java
            @Service
            @RequiredArgsConstructor
            @Slf4j
            public class DashboardService {

                private final UserService userService;
                private final TaskService taskService;
                private final NotificationService notificationService;

                /**
                 * Load dashboard data in parallel
                 * Traditional: 500ms (sequential)
                 * Async: 200ms (parallel)
                 */
                public DashboardData getDashboardData(Long userId) throws Exception {
                    log.info("Loading dashboard for user {}", userId);

                    // Start all async tasks simultaneously
                    CompletableFuture<UserProfile> profileFuture =
                        userService.getUserProfileAsync(userId);

                    CompletableFuture<List<Task>> tasksFuture =
                        taskService.getRecentTasksAsync(userId);

                    CompletableFuture<List<Notification>> notificationsFuture =
                        notificationService.getUnreadNotificationsAsync(userId);

                    // Wait for all to complete
                    CompletableFuture<Void> allOf = CompletableFuture.allOf(
                        profileFuture,
                        tasksFuture,
                        notificationsFuture
                    );

                    // Block until all complete
                    allOf.get();

                    // All data is now ready, combine into response
                    return DashboardData.builder()
                        .profile(profileFuture.get())
                        .recentTasks(tasksFuture.get())
                        .unreadNotifications(notificationsFuture.get())
                        .build();
                }
            }
            ```

            **Performance comparison:**
            ```
            Sequential execution:
            Get profile:         100ms
            Get tasks:           200ms
            Get notifications:   200ms
            Total:               500ms ✗

            Parallel execution:
            Get profile:         100ms  ┐
            Get tasks:           200ms  ├─ All running simultaneously
            Get notifications:   200ms  ┘
            Total:               200ms ✅ (2.5x faster!)
            ```

            ## Scheduled Tasks

            Run tasks automatically at scheduled intervals:

            ```java
            package com.example.config;

            import org.springframework.context.annotation.Configuration;
            import org.springframework.scheduling.annotation.EnableScheduling;

            @Configuration
            @EnableScheduling  // Enable scheduled tasks
            public class SchedulingConfig {
            }
            ```

            ### Fixed Rate Scheduling

            ```java
            @Service
            @Slf4j
            public class CleanupService {

                /**
                 * Run every 1 hour (3,600,000 ms)
                 * Starts next execution 1 hour after previous START
                 */
                @Scheduled(fixedRate = 3600000)
                public void cleanupExpiredSessions() {
                    log.info("Running session cleanup task");
                    sessionRepository.deleteExpiredSessions();
                }

                /**
                 * Run every 5 minutes
                 * Waits 5 minutes after previous COMPLETION before starting again
                 */
                @Scheduled(fixedDelay = 300000)
                public void cleanupTempFiles() {
                    log.info("Cleaning up temporary files");
                    fileStorageService.cleanupOldFiles();
                }

                /**
                 * Initial delay of 10 seconds, then every 1 minute
                 */
                @Scheduled(initialDelay = 10000, fixedRate = 60000)
                public void refreshCache() {
                    log.info("Refreshing cache");
                    cacheService.refreshPopularItems();
                }
            }
            ```

            ### Cron Expression Scheduling

            ```java
            @Service
            @Slf4j
            public class ReportService {

                /**
                 * Run every day at 2:00 AM
                 * Cron format: second minute hour day month weekday
                 */
                @Scheduled(cron = "0 0 2 * * *")
                public void generateDailyReports() {
                    log.info("Generating daily reports at {}", LocalDateTime.now());
                    reportGenerator.generateDailyReports();
                }

                /**
                 * Run every Monday at 9:00 AM
                 * 0 = second, 0 = minute, 9 = hour, * = any day, * = any month, MON = Monday
                 */
                @Scheduled(cron = "0 0 9 * * MON")
                public void generateWeeklyReport() {
                    log.info("Generating weekly report");
                    reportGenerator.generateWeeklyReport();
                }

                /**
                 * Run first day of every month at midnight
                 */
                @Scheduled(cron = "0 0 0 1 * *")
                public void generateMonthlyReport() {
                    log.info("Generating monthly report");
                    reportGenerator.generateMonthlyReport();
                }

                /**
                 * Run every 30 minutes during business hours (9 AM - 5 PM)
                 */
                @Scheduled(cron = "0 */30 9-17 * * MON-FRI")
                public void syncInventory() {
                    log.info("Syncing inventory during business hours");
                    inventoryService.sync();
                }
            }
            ```

            **Cron expression format:**
            ```
            ┌─────────── second (0-59)
            │ ┌───────── minute (0-59)
            │ │ ┌─────── hour (0-23)
            │ │ │ ┌───── day of month (1-31)
            │ │ │ │ ┌─── month (1-12 or JAN-DEC)
            │ │ │ │ │ ┌─ day of week (0-6 or SUN-SAT)
            │ │ │ │ │ │
            * * * * * *

            Examples:
            "0 0 * * * *"        - Every hour on the hour
            "0 */15 * * * *"     - Every 15 minutes
            "0 0 12 * * *"       - Every day at noon
            "0 0 0 * * SUN"      - Every Sunday at midnight
            "0 0 9-17 * * MON-FRI" - Every hour during business hours
            ```

            ## Common Async Patterns

            ### Pattern 1: Image Processing

            ```java
            @Service
            @RequiredArgsConstructor
            @Slf4j
            public class ImageService {

                private final ImageRepository imageRepository;
                private final StorageService storageService;

                @Async("taskExecutor")
                public CompletableFuture<Void> processUploadedImage(Long imageId) {
                    log.info("Processing image {}", imageId);

                    try {
                        Image image = imageRepository.findById(imageId).orElseThrow();

                        // Generate thumbnails (CPU intensive)
                        byte[] thumbnail = generateThumbnail(image.getData(), 200, 200);
                        byte[] smallImage = generateThumbnail(image.getData(), 800, 600);

                        // Upload to storage (I/O intensive)
                        String thumbnailUrl = storageService.upload(thumbnail, "thumbnails/");
                        String smallImageUrl = storageService.upload(smallImage, "small/");

                        // Update database
                        image.setThumbnailUrl(thumbnailUrl);
                        image.setSmallImageUrl(smallImageUrl);
                        image.setProcessed(true);
                        imageRepository.save(image);

                        log.info("Image {} processed successfully", imageId);
                        return CompletableFuture.completedFuture(null);

                    } catch (Exception e) {
                        log.error("Failed to process image {}", imageId, e);
                        return CompletableFuture.failedFuture(e);
                    }
                }
            }

            @RestController
            @RequestMapping("/api/images")
            @RequiredArgsConstructor
            public class ImageController {

                private final ImageService imageService;

                @PostMapping("/upload")
                public ResponseEntity<ImageUploadResponse> uploadImage(
                    @RequestParam("file") MultipartFile file
                ) {
                    // Save original image (fast)
                    Image image = imageService.saveOriginal(file);

                    // Process asynchronously (returns immediately)
                    imageService.processUploadedImage(image.getId());

                    return ResponseEntity.accepted().body(
                        ImageUploadResponse.builder()
                            .imageId(image.getId())
                            .status("PROCESSING")
                            .message("Image uploaded. Processing in background.")
                            .build()
                    );
                }
            }
            ```

            ### Pattern 2: Batch Operations

            ```java
            @Service
            @Slf4j
            public class BatchService {

                /**
                 * Send notifications to all users
                 * Process in batches to avoid overwhelming resources
                 */
                @Async("taskExecutor")
                public CompletableFuture<BatchResult> sendBulkNotifications(
                    String message,
                    List<Long> userIds
                ) {
                    log.info("Sending notifications to {} users", userIds.size());

                    int batchSize = 100;
                    int successCount = 0;
                    int failureCount = 0;

                    // Process in batches
                    for (int i = 0; i < userIds.size(); i += batchSize) {
                        int end = Math.min(i + batchSize, userIds.size());
                        List<Long> batch = userIds.subList(i, end);

                        try {
                            notificationService.sendToUsers(batch, message);
                            successCount += batch.size();
                            log.info("Processed batch {}-{}", i, end);
                        } catch (Exception e) {
                            log.error("Failed to process batch {}-{}", i, end, e);
                            failureCount += batch.size();
                        }

                        // Small delay between batches to avoid overwhelming system
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }

                    BatchResult result = BatchResult.builder()
                        .totalProcessed(userIds.size())
                        .successCount(successCount)
                        .failureCount(failureCount)
                        .build();

                    log.info("Bulk notification complete: {}", result);
                    return CompletableFuture.completedFuture(result);
                }
            }
            ```

            ### Pattern 3: Retry Logic

            ```java
            @Service
            @Slf4j
            public class ExternalApiService {

                /**
                 * Call external API with retries
                 */
                @Async("taskExecutor")
                public CompletableFuture<ApiResponse> callExternalApi(String endpoint) {
                    int maxRetries = 3;
                    int retryDelay = 1000;  // 1 second

                    for (int attempt = 1; attempt <= maxRetries; attempt++) {
                        try {
                            log.info("Calling external API (attempt {})", attempt);
                            ApiResponse response = restTemplate.getForObject(endpoint, ApiResponse.class);
                            return CompletableFuture.completedFuture(response);

                        } catch (Exception e) {
                            log.warn("API call failed (attempt {}): {}", attempt, e.getMessage());

                            if (attempt == maxRetries) {
                                log.error("All retry attempts exhausted");
                                return CompletableFuture.failedFuture(e);
                            }

                            // Wait before retry with exponential backoff
                            try {
                                Thread.sleep(retryDelay * attempt);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                return CompletableFuture.failedFuture(ie);
                            }
                        }
                    }

                    return CompletableFuture.failedFuture(
                        new RuntimeException("Failed to call external API")
                    );
                }
            }
            ```

            ## Common Mistakes to Avoid

            ### ❌ Calling @Async Method from Same Class

            **Wrong:**
            ```java
            @Service
            public class UserService {

                public void registerUser(RegisterRequest request) {
                    // Save user...

                    // This won't be async! (same class call)
                    sendWelcomeEmail(request.getEmail());
                }

                @Async
                public void sendWelcomeEmail(String email) {
                    // Executed synchronously! ✗
                }
            }
            ```

            **Why:** Spring creates a proxy for @Async methods. Internal calls bypass the proxy.

            **Right:**
            ```java
            @Service
            @RequiredArgsConstructor
            public class UserService {

                private final EmailService emailService;  // Separate service

                public void registerUser(RegisterRequest request) {
                    // Save user...

                    // This WILL be async ✓
                    emailService.sendWelcomeEmail(request.getEmail());
                }
            }

            @Service
            public class EmailService {
                @Async
                public void sendWelcomeEmail(String email) {
                    // Executed asynchronously ✓
                }
            }
            ```

            ### ❌ Not Handling Exceptions

            **Wrong:**
            ```java
            @Async
            public void processOrder(Long orderId) {
                // Exception thrown here is swallowed!
                Order order = orderRepository.findById(orderId).orElseThrow();
                // If this fails, no one knows!
            }
            ```

            **Right:**
            ```java
            @Async
            public CompletableFuture<Void> processOrder(Long orderId) {
                try {
                    Order order = orderRepository.findById(orderId).orElseThrow();
                    // Process order...
                    return CompletableFuture.completedFuture(null);

                } catch (Exception e) {
                    log.error("Failed to process order {}", orderId, e);
                    // Store error for manual review
                    errorLogService.logError("ORDER_PROCESSING", orderId, e);
                    return CompletableFuture.failedFuture(e);
                }
            }
            ```

            ### ❌ Using @Async for CPU-Intensive Tasks

            **Wrong:**
            ```java
            @Async
            public void calculatePrimes() {
                // CPU-intensive calculation blocking thread pool!
                for (long i = 0; i < 1_000_000_000; i++) {
                    // Calculation...
                }
            }
            ```

            **Why:** Thread pools are for I/O-bound tasks (database, network). CPU-bound
            tasks should use dedicated worker processes or queues.

            **Right:**
            ```java
            // Use dedicated queue system (RabbitMQ, Kafka, etc.)
            // Or dedicated worker service
            // Or limit with separate thread pool
            ```

            ### ❌ Not Configuring Thread Pool

            **Wrong:**
            ```java
            @EnableAsync  // Uses default SimpleAsyncTaskExecutor
            ```

            **Why:** Default executor creates unlimited new threads. Under load, can crash app.

            **Right:**
            ```java
            @Bean(name = "taskExecutor")
            public Executor taskExecutor() {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setCorePoolSize(5);
                executor.setMaxPoolSize(10);
                executor.setQueueCapacity(100);
                // Properly configured with limits
                return executor;
            }
            ```

            ## Monitoring Async Tasks

            ### Custom Thread Pool with Metrics

            ```java
            @Configuration
            public class AsyncConfig {

                @Bean(name = "taskExecutor")
                public ThreadPoolTaskExecutor taskExecutor() {
                    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                    executor.setCorePoolSize(5);
                    executor.setMaxPoolSize(10);
                    executor.setQueueCapacity(100);
                    executor.setThreadNamePrefix("async-");

                    // Add decorator for monitoring
                    executor.setTaskDecorator(new MonitoringTaskDecorator());

                    executor.initialize();
                    return executor;
                }
            }

            public class MonitoringTaskDecorator implements TaskDecorator {
                private static final Logger log = LoggerFactory.getLogger(MonitoringTaskDecorator.class);

                @Override
                public Runnable decorate(Runnable runnable) {
                    return () -> {
                        long startTime = System.currentTimeMillis();
                        try {
                            runnable.run();
                        } finally {
                            long duration = System.currentTimeMillis() - startTime;
                            log.debug("Async task completed in {}ms on thread {}",
                                duration, Thread.currentThread().getName());
                        }
                    };
                }
            }
            ```

            ### Actuator Metrics

            ```yaml
            management:
              endpoints:
                web:
                  exposure:
                    include: metrics, health, scheduledtasks
              metrics:
                enable:
                  executor: true
            ```

            Access metrics:
            ```
            GET /actuator/metrics/executor.active
            GET /actuator/metrics/executor.completed
            GET /actuator/metrics/executor.queued
            GET /actuator/scheduledtasks
            ```

            ## Summary

            Asynchronous processing is essential for responsive, scalable applications:

            **Key Concepts:**
            - @Async executes methods in separate threads
            - CompletableFuture returns results from async methods
            - @Scheduled runs tasks at fixed intervals or cron schedules
            - Custom thread pools required for production

            **Best Practices:**
            - Configure ThreadPoolTaskExecutor with appropriate limits
            - Use separate services for @Async methods (avoid same-class calls)
            - Always handle exceptions in async methods
            - Return CompletableFuture for async methods that return values
            - Use @Async for I/O-bound tasks, not CPU-intensive operations

            **Common Patterns:**
            - Fire-and-forget (email, notifications)
            - Parallel execution (dashboard data loading)
            - Background processing (image processing, batch jobs)
            - Scheduled maintenance (cleanup, reports)

            **Performance Impact:**
            - Sequential: 500ms for 3 tasks
            - Parallel async: 200ms (2.5x faster!)
            - User gets immediate response while work happens in background

            Async processing transforms slow, blocking operations into fast,
            responsive user experiences while maximizing server throughput.
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
            "You add @Async to a method in UserService and call it from another method in the same UserService class. The method still runs synchronously. What's the problem?",
            "C",
            """
            This is one of the most common mistakes developers make when first using @Async
            in Spring. Understanding Spring's proxy-based AOP is crucial for async to work.

            The correct answer is C. Spring uses proxies for @Async. Calling @Async methods
            from the same class bypasses the proxy, so the method executes synchronously.

            **Understanding Spring Proxies:**

            When you use @Async, Spring creates a proxy wrapper around your service:

            ```
            Your Code                     Spring Proxy                      Your Actual Service
            ─────────────────────────────────────────────────────────────────────────────────
            userService.sendEmail()  →   Proxy intercepts call    →       UserService.sendEmail()
                                         Checks for @Async
                                         Executes in thread pool
            ```

            **The Problem - Same Class Call:**

            ```java
            @Service
            public class UserService {

                public void registerUser(RegisterRequest request) {
                    User user = userRepository.save(new User(request));

                    // Direct method call - bypasses proxy!
                    sendWelcomeEmail(user.getEmail());  // ✗ Runs synchronously
                }

                @Async
                public void sendWelcomeEmail(String email) {
                    log.info("Sending email to {} on thread {}",
                        email, Thread.currentThread().getName());
                    emailClient.send(email, "Welcome!", "Hello!");
                }
            }
            ```

            **What happens:**
            ```
            1. registerUser() called
            2. Creates user
            3. Calls sendWelcomeEmail() directly (this.sendWelcomeEmail())
            4. No proxy involved - just regular method call
            5. Executes on same thread ✗
            6. User waits for email to send
            ```

            **Log output:**
            ```
            Sending email to user@example.com on thread http-nio-8080-exec-1
            ```
            Note: Same HTTP thread! Not an async thread!

            **The Solution - Separate Service:**

            ```java
            @Service
            @RequiredArgsConstructor
            public class UserService {

                private final EmailService emailService;  // Inject separate service

                public void registerUser(RegisterRequest request) {
                    User user = userRepository.save(new User(request));

                    // Call through Spring proxy ✓
                    emailService.sendWelcomeEmail(user.getEmail());
                }
            }

            @Service
            public class EmailService {

                @Async("taskExecutor")
                public void sendWelcomeEmail(String email) {
                    log.info("Sending email to {} on thread {}",
                        email, Thread.currentThread().getName());
                    emailClient.send(email, "Welcome!", "Hello!");
                }
            }
            ```

            **What happens now:**
            ```
            1. registerUser() called
            2. Creates user
            3. Calls emailService.sendWelcomeEmail()
            4. Goes through Spring proxy ✓
            5. Proxy sees @Async annotation
            6. Submits task to thread pool
            7. Returns immediately
            8. Email sent on separate thread
            ```

            **Log output:**
            ```
            Sending email to user@example.com on thread async-task-1
            ```
            Note: Custom async thread! Working correctly! ✓

            **Why This Happens - Technical Details:**

            Spring AOP creates a proxy using either:
            - JDK Dynamic Proxy (interface-based)
            - CGLIB Proxy (class-based)

            ```java
            // What Spring creates (simplified):
            public class UserServiceProxy extends UserService {

                private UserService target;  // Real instance

                @Override
                public void registerUser(RegisterRequest request) {
                    // Just delegates to real instance
                    target.registerUser(request);
                }

                // No override for sendWelcomeEmail because it's not called from outside!
            }
            ```

            When you call `this.sendWelcomeEmail()` inside registerUser, you're calling
            the real method directly, not going through the proxy.

            **Alternative Solution - Self-Injection:**

            ```java
            @Service
            public class UserService {

                @Autowired
                private UserService self;  // Inject proxy of self

                public void registerUser(RegisterRequest request) {
                    User user = userRepository.save(new User(request));

                    // Call through proxy ✓
                    self.sendWelcomeEmail(user.getEmail());
                }

                @Async
                public void sendWelcomeEmail(String email) {
                    emailClient.send(email, "Welcome!", "Hello!");
                }
            }
            ```

            This works but is considered a code smell. Separate services is cleaner.

            Why other answers are wrong:

            A is wrong because @EnableAsync IS required, but it's not the issue here.
            Without @EnableAsync, NO @Async methods would work anywhere. The fact that
            the question states the method "still runs synchronously" implies @EnableAsync
            is configured (otherwise it wouldn't run at all, you'd get an error or warning).

            If @EnableAsync were missing:
            - All @Async methods would run synchronously
            - This would affect ALL services, not just same-class calls
            - The issue in the question is specific to same-class calls

            B is wrong because return type has nothing to do with whether a method executes
            asynchronously. Both are valid:

            ```java
            @Async
            public void sendEmail(String email) {
                // Fire and forget - valid ✓
            }

            @Async
            public CompletableFuture<Void> sendEmail(String email) {
                // Returns future - also valid ✓
            }
            ```

            The difference is:
            - `void`: Caller can't wait for completion or get result
            - `CompletableFuture`: Caller CAN wait or chain operations

            Both execute asynchronously. Return type is about what caller needs, not
            whether async works.

            D is wrong because while configuring a custom thread pool is a best practice
            for production, it's not required for @Async to work. If you don't configure
            a custom executor, Spring uses SimpleAsyncTaskExecutor by default:

            ```java
            // Without custom executor - still works asynchronously
            @EnableAsync  // Uses default executor
            public class AsyncConfig {
            }
            ```

            The default executor creates a new thread for each task, which isn't ideal for
            production (no pooling, no limits), but it DOES execute tasks asynchronously.

            The issue in the question is that the method runs synchronously, which means
            @Async isn't being applied at all. This happens with same-class calls, not
            because of missing executor configuration.

            **Testing for Async Execution:**

            ```java
            @Test
            void shouldExecuteAsynchronously() throws Exception {
                String threadName = Thread.currentThread().getName();

                emailService.sendWelcomeEmail("test@example.com");

                // Give async task time to execute
                Thread.sleep(100);

                // Verify it ran on different thread
                // (In real test, you'd use mocks to verify behavior)
            }
            ```

            **Key Takeaway:**

            Spring AOP (including @Async, @Transactional, @Cacheable) only works when
            going through Spring proxies. Same-class method calls bypass proxies.

            Always use separate services for @Async methods.
            """
        );

        question.addChoice("A", "@EnableAsync annotation is missing from configuration class");
        question.addChoice("B", "@Async methods must return CompletableFuture, not void");
        question.addChoice("C", "Spring uses proxies for @Async - same-class calls bypass the proxy");
        question.addChoice("D", "Custom ThreadPoolTaskExecutor not configured - @Async needs explicit executor");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "Your application uses @Async with the default executor. Under heavy load, you see 'OutOfMemoryError: unable to create new native thread'. What's the MOST LIKELY cause and solution?",
            "B",
            """
            This is a critical production issue that can bring down applications. Understanding
            the difference between Spring's default async executor and a properly configured
            thread pool is essential.

            The correct answer is B. The default SimpleAsyncTaskExecutor creates unlimited
            threads. Under heavy load this exhausts system resources. Solution: configure
            a ThreadPoolTaskExecutor with limits.

            **Understanding the Problem:**

            **Default Executor Behavior:**
            ```java
            @Configuration
            @EnableAsync
            public class AsyncConfig {
                // No custom executor configured!
                // Spring uses SimpleAsyncTaskExecutor
            }
            ```

            **What SimpleAsyncTaskExecutor does:**
            ```java
            // Simplified view of SimpleAsyncTaskExecutor:
            public class SimpleAsyncTaskExecutor {
                public void execute(Runnable task) {
                    // Creates NEW thread for EVERY task!
                    Thread thread = new Thread(task);
                    thread.start();
                }
            }
            ```

            **Under Heavy Load:**
            ```
            Request 1  → New thread created (Thread-1)
            Request 2  → New thread created (Thread-2)
            Request 3  → New thread created (Thread-3)
            ...
            Request 1000 → New thread created (Thread-1000)
            Request 2000 → New thread created (Thread-2000)
            Request 5000 → OutOfMemoryError! ✗

            Total threads: 5000+
            Memory per thread: ~1MB (stack)
            Total memory: 5GB just for thread stacks!
            ```

            **System Impact:**
            - Each thread consumes ~1MB of stack memory
            - Context switching overhead increases exponentially
            - CPU thrashing (more time switching than executing)
            - Eventually: OutOfMemoryError
            - Application crashes

            **Real-World Scenario:**
            ```java
            @Service
            public class EmailService {
                @Async  // Using default executor!
                public void sendEmail(String email) {
                    Thread.sleep(5000);  // Takes 5 seconds
                }
            }

            // Black Friday sale - 10,000 users register in 1 minute
            // Each registration sends email
            // 10,000 emails × 5 seconds each = 10,000 threads!
            // Application crashes ✗
            ```

            **The Solution: Bounded Thread Pool**

            ```java
            @Configuration
            @EnableAsync
            public class AsyncConfig {

                @Bean(name = "taskExecutor")
                public Executor taskExecutor() {
                    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

                    // Core threads: Always alive
                    executor.setCorePoolSize(10);

                    // Max threads: Upper limit
                    executor.setMaxPoolSize(20);

                    // Queue capacity: Tasks waiting for threads
                    executor.setQueueCapacity(500);

                    // What happens when queue is full?
                    executor.setRejectedExecutionHandler(
                        new ThreadPoolExecutor.CallerRunsPolicy()  // Run in caller thread
                    );

                    executor.setThreadNamePrefix("async-");
                    executor.initialize();
                    return executor;
                }
            }
            ```

            **With Bounded Pool:**
            ```
            Requests 1-10:    Use core threads (Thread-1 to Thread-10)
            Requests 11-20:   Create new threads up to max (Thread-11 to Thread-20)
            Requests 21-520:  Queue tasks (500 capacity)
            Request 521+:     Run in caller thread (backpressure!)

            Maximum threads: 20 (not 5000!)
            Maximum queued: 500
            Memory usage: Bounded and predictable ✓
            ```

            **Behavior Under Load:**
            ```java
            @Service
            public class EmailService {
                @Async("taskExecutor")  // Use configured executor
                public void sendEmail(String email) {
                    log.info("Sending email on {}", Thread.currentThread().getName());
                    Thread.sleep(5000);
                }
            }

            // 10,000 registration requests:
            // - 20 threads actively processing emails
            // - 500 emails queued
            // - Remaining requests slow down (CallerRunsPolicy)
            // - Application stays alive! ✓
            ```

            Why other answers are incorrect:

            A is wrong because this isn't a database connection pool issue. The error
            "unable to create new native thread" is about operating system thread limits,
            not database connections:

            ```
            Database connection pool exhausted:
            Error: "Could not get JDBC Connection"
            Error: "Connection pool timeout"

            Thread creation failure:
            Error: "unable to create new native thread" ← The actual error
            Error: "OutOfMemoryError"
            ```

            While database connection pools should also be configured properly, they don't
            cause "unable to create new native thread" errors. That's specifically about
            the JVM trying to create too many threads.

            C is wrong because increasing heap size doesn't solve thread creation limits.
            Thread limits are imposed by:

            1. **Operating System:** `/proc/sys/kernel/threads-max` (Linux)
            2. **Process limits:** `ulimit -u` (max user processes)
            3. **JVM stack size:** Each thread consumes stack memory

            ```bash
            # Linux default limits:
            ulimit -u
            # 4096 or similar (max threads per user)

            # If you hit this limit, heap size is irrelevant!
            java -Xmx8G MyApp  # Doesn't help with thread limits
            ```

            Even with infinite heap memory, you'd still hit OS thread limits. The solution
            isn't more memory—it's fewer threads (thread pooling).

            D is wrong because @Async itself has no maximum thread limit. The limit comes
            from the executor you use:

            ```java
            // No limit on @Async annotations
            @Async
            public void method1() { }

            @Async
            public void method2() { }

            @Async
            public void method3() { }

            // Problem is the EXECUTOR, not @Async
            ```

            You don't need to "reduce @Async usage." You need to configure a proper executor
            that pools and limits threads.

            **Additional Configuration Options:**

            **1. Rejection Policies:**
            ```java
            // CallerRunsPolicy: Run in calling thread (backpressure)
            new ThreadPoolExecutor.CallerRunsPolicy()

            // AbortPolicy: Throw RejectedExecutionException
            new ThreadPoolExecutor.AbortPolicy()

            // DiscardPolicy: Silently discard task
            new ThreadPoolExecutor.DiscardPolicy()

            // DiscardOldestPolicy: Discard oldest queued task
            new ThreadPoolExecutor.DiscardOldestPolicy()
            ```

            **2. Multiple Thread Pools:**
            ```java
            @Bean(name = "emailExecutor")
            public Executor emailExecutor() {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setCorePoolSize(5);
                executor.setMaxPoolSize(10);
                executor.setQueueCapacity(100);
                return executor;
            }

            @Bean(name = "reportExecutor")
            public Executor reportExecutor() {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setCorePoolSize(2);
                executor.setMaxPoolSize(4);
                executor.setQueueCapacity(50);
                return executor;
            }

            // Use different executors for different purposes
            @Async("emailExecutor")
            public void sendEmail() { }

            @Async("reportExecutor")
            public void generateReport() { }
            ```

            **3. Monitoring Thread Pool:**
            ```java
            @Scheduled(fixedRate = 60000)
            public void monitorThreadPool() {
                ThreadPoolTaskExecutor executor =
                    (ThreadPoolTaskExecutor) taskExecutor;

                log.info("Thread Pool Stats: " +
                    "Active: {}, " +
                    "Pool Size: {}, " +
                    "Queue Size: {}, " +
                    "Max Pool Size: {}",
                    executor.getActiveCount(),
                    executor.getPoolSize(),
                    executor.getThreadPoolExecutor().getQueue().size(),
                    executor.getMaxPoolSize()
                );

                // Alert if queue is filling up
                if (executor.getThreadPoolExecutor().getQueue().size() > 400) {
                    log.warn("Thread pool queue is 80% full!");
                }
            }
            ```

            **Choosing Thread Pool Sizes:**

            ```java
            // CPU-bound tasks:
            corePoolSize = number of CPU cores
            maxPoolSize = number of CPU cores + 1

            // I/O-bound tasks (database, network, file I/O):
            corePoolSize = number of CPU cores × 2
            maxPoolSize = number of CPU cores × 4

            // Mixed workload:
            corePoolSize = 10-20
            maxPoolSize = 50-100
            queueCapacity = 100-500

            // Monitor and adjust based on actual load!
            ```

            **Key Takeaway:**

            Never use Spring's default async executor in production. Always configure
            a bounded thread pool with appropriate limits. This prevents resource exhaustion
            and makes your application resilient under load.

            The default executor is only suitable for development or very low-traffic applications.
            """
        );

        question.addChoice("A", "Database connection pool is too small - increase max connections");
        question.addChoice("B", "Default SimpleAsyncTaskExecutor creates unlimited threads - configure ThreadPoolTaskExecutor with limits");
        question.addChoice("C", "JVM heap size too small - increase with -Xmx parameter");
        question.addChoice("D", "Too many @Async annotations - reduce number of async methods");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "You have a @Scheduled task that runs every minute with fixedRate. Sometimes it takes 90 seconds to complete. What happens to the next scheduled execution?",
            "A",
            """
            Understanding the difference between fixedRate and fixedDelay is crucial for
            correctly implementing scheduled tasks and avoiding unexpected behavior.

            The correct answer is A. With fixedRate, the next execution starts immediately
            if the previous one is still running, potentially causing overlapping executions
            and resource exhaustion.

            **Understanding fixedRate:**

            ```java
            @Scheduled(fixedRate = 60000)  // Every 60 seconds
            public void processTask() {
                log.info("Task started at {}", LocalDateTime.now());
                // Takes 90 seconds to complete
                expensiveOperation();
                log.info("Task completed at {}", LocalDateTime.now());
            }
            ```

            **Execution Timeline with fixedRate:**
            ```
            Time    Event
            00:00   Execution 1 starts
            01:00   Execution 2 starts (Execution 1 still running!) ⚠️
            01:30   Execution 1 completes (after 90 seconds)
            02:00   Execution 3 starts (Execution 2 still running!) ⚠️
            02:30   Execution 2 completes
            03:00   Execution 4 starts (Execution 3 still running!) ⚠️

            Result: Multiple executions running simultaneously!
            ```

            **Log Output:**
            ```
            2025-01-15 10:00:00 Task started
            2025-01-15 10:01:00 Task started  ← Second instance started!
            2025-01-15 10:01:30 Task completed ← First instance finished
            2025-01-15 10:02:00 Task started  ← Third instance started!
            2025-01-15 10:02:30 Task completed ← Second instance finished
            ```

            **Problems This Causes:**

            1. **Resource Exhaustion:**
            ```java
            @Scheduled(fixedRate = 60000)
            public void generateReport() {
                // Uses 2GB memory and 1 database connection
                // After 10 overlapping executions:
                // - 20GB memory used
                // - 10 database connections used
                // - Application crashes or slows to crawl
            }
            ```

            2. **Data Corruption:**
            ```java
            @Scheduled(fixedRate = 60000)
            public void processQueue() {
                List<Task> tasks = taskRepository.findPending();
                tasks.forEach(task -> {
                    task.setStatus(PROCESSING);
                    taskRepository.save(task);
                    // If multiple executions run, might process same task twice!
                });
            }
            ```

            3. **Database Deadlocks:**
            ```java
            @Scheduled(fixedRate = 60000)
            public void syncData() {
                // Update many rows
                // Multiple simultaneous executions try to update same rows
                // Deadlock! Each execution waiting for locks held by others
            }
            ```

            **The Solutions:**

            **Solution 1: Use fixedDelay Instead**
            ```java
            @Scheduled(fixedDelay = 60000)  // Wait 60 seconds AFTER completion
            public void processTask() {
                expensiveOperation();  // Takes 90 seconds
            }
            ```

            **Execution Timeline with fixedDelay:**
            ```
            Time    Event
            00:00   Execution 1 starts
            01:30   Execution 1 completes (90 seconds)
            02:30   Execution 2 starts (60 seconds after previous completion)
            04:00   Execution 2 completes
            05:00   Execution 3 starts

            Result: No overlapping! Each execution completes before next starts ✓
            ```

            **Solution 2: Add Synchronization**
            ```java
            private final AtomicBoolean isRunning = new AtomicBoolean(false);

            @Scheduled(fixedRate = 60000)
            public void processTask() {
                // Skip if already running
                if (!isRunning.compareAndSet(false, true)) {
                    log.warn("Previous execution still running, skipping this execution");
                    return;
                }

                try {
                    expensiveOperation();
                } finally {
                    isRunning.set(false);
                }
            }
            ```

            **Execution with Synchronization:**
            ```
            Time    Event
            00:00   Execution 1 starts (isRunning = true)
            01:00   Execution 2 attempted - sees isRunning=true, skips
            01:30   Execution 1 completes (isRunning = false)
            02:00   Execution 3 starts (isRunning = true)
            ```

            **Solution 3: Use Distributed Lock (Multi-Instance)**
            ```java
            @Scheduled(fixedRate = 60000)
            @SchedulerLock(
                name = "processTask",
                lockAtMostFor = "10m",
                lockAtLeastFor = "1m"
            )
            public void processTask() {
                // Only one instance across all servers can run at a time
                expensiveOperation();
            }
            ```

            Why other answers are incorrect:

            B is wrong because fixedRate does NOT automatically wait for completion.
            That's what fixedDelay does:

            ```java
            // fixedRate: Starts at fixed intervals (may overlap)
            @Scheduled(fixedRate = 60000)

            // fixedDelay: Waits after completion (no overlap)
            @Scheduled(fixedDelay = 60000)
            ```

            The whole point of fixedRate is to maintain a consistent schedule regardless
            of execution time. If it automatically waited, it would be the same as fixedDelay.

            C is wrong because the scheduled execution is NOT skipped. With fixedRate,
            Spring attempts to maintain the fixed schedule:

            ```
            Scheduled at:  10:00, 10:01, 10:02, 10:03, ...
            Executes at:   10:00, 10:01, 10:02, 10:03, ...

            If 10:00 execution takes 90 seconds:
            - 10:01 execution still happens
            - 10:02 execution still happens
            - 10:03 execution still happens

            None are skipped!
            ```

            If you want skipping behavior, you need to implement it manually (Solution 2 above).

            D is wrong because Spring does NOT throw an exception by default. It simply
            starts the next execution:

            ```java
            // No exception thrown - just starts new execution
            @Scheduled(fixedRate = 60000)
            public void processTask() {
                Thread.sleep(90000);
            }

            // Multiple executions run concurrently
            // No error, no warning (unless you add logging)
            ```

            If you want Spring to prevent overlapping, you must explicitly configure it.

            **Choosing Between fixedRate and fixedDelay:**

            **Use fixedRate when:**
            - Task completes quickly (well under the interval)
            - You want consistent execution times (every hour on the hour)
            - Example: Health check every 30 seconds

            ```java
            @Scheduled(fixedRate = 30000)
            public void healthCheck() {
                // Quick check (< 1 second)
                pingService.check();
            }
            ```

            **Use fixedDelay when:**
            - Task duration is variable or long
            - You want guaranteed gap between executions
            - Example: Data sync that might take varying time

            ```java
            @Scheduled(fixedDelay = 300000)  // 5 minutes after completion
            public void syncExternalData() {
                // Might take 1-10 minutes depending on data volume
                externalApi.fetchAndSync();
            }
            ```

            **Comparison Table:**
            ```
            fixedRate = 60000:
            Start: 10:00:00
            End:   10:01:30 (90 sec duration)
            Next:  10:01:00 (may overlap with previous!)

            fixedDelay = 60000:
            Start: 10:00:00
            End:   10:01:30 (90 sec duration)
            Next:  10:02:30 (60 sec after end of previous)
            ```

            **Additional: initialDelay**
            ```java
            // Don't run immediately on startup - wait 30 seconds
            @Scheduled(initialDelay = 30000, fixedRate = 60000)
            public void processTask() {
                // Gives application time to fully initialize
            }
            ```

            **Key Takeaway:**

            For tasks with variable or long execution times, use fixedDelay to prevent
            overlapping executions. Use fixedRate only when you're certain the task
            completes well within the interval, or when you've implemented synchronization
            to prevent overlapping.
            """
        );

        question.addChoice("A", "Next execution starts immediately, causing overlapping executions and potential resource exhaustion");
        question.addChoice("B", "Spring automatically waits for previous execution to complete before starting next one");
        question.addChoice("C", "Next scheduled execution is skipped, and the following one runs at the next interval");
        question.addChoice("D", "Spring throws SchedulingException and disables the scheduled task");

        return question;
    }
}
