package com.socraticjava.content.epoch8;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 8.5: Deployment and Production Readiness
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-8-lesson-5", "Lesson 8.5: Deployment - From Laptop to Production")
            .addTheory("The Journey from Development to Production",
                "Your application works on your laptop. Great!\n" +
                "But how do users actually use it?\n\n" +
                "THE DEPLOYMENT PIPELINE:\n\n" +
                "1. DEVELOPMENT (Your laptop):\n" +
                "   - Run: ./mvnw spring-boot:run\n" +
                "   - Database: localhost:5432\n" +
                "   - Hot reload enabled\n" +
                "   - Debug mode on\n\n" +
                "2. BUILD (Create executable):\n" +
                "   - Package as JAR file\n" +
                "   - All dependencies included\n" +
                "   - Single file to deploy\n\n" +
                "3. TEST ENVIRONMENT (Pre-production):\n" +
                "   - Test database\n" +
                "   - Similar to production\n" +
                "   - Catch bugs before users see them\n\n" +
                "4. PRODUCTION (The real thing):\n" +
                "   - Real database with real data\n" +
                "   - Real users\n" +
                "   - Must be reliable, secure, fast\n" +
                "   - Logging and monitoring essential\n\n" +
                "GOAL: Make deployment smooth, repeatable, and safe!")
            .addAnalogy("Deployment is Like Opening a Restaurant",
                "DEVELOPMENT (Test kitchen at home):\n" +
                "You're experimenting with recipes in your home kitchen\n" +
                "Like: Running on localhost, making changes\n\n" +
                "BUILD (Prepare for opening):\n" +
                "Package all your recipes, ingredients, equipment\n" +
                "Like: mvn package creates myapp.jar\n\n" +
                "TEST ENVIRONMENT (Soft opening):\n" +
                "Invite friends and family to test the restaurant\n" +
                "Like: Deploy to staging server, run tests\n\n" +
                "PRODUCTION (Grand opening):\n" +
                "Real customers, real money, real reviews\n" +
                "Like: Deploy to production server, monitor everything\n\n" +
                "CONFIGURATION:\n" +
                "Home kitchen uses different equipment than restaurant\n" +
                "Like: Dev uses localhost, prod uses real database URL\n" +
                "Solution: Environment variables (swap out settings)")
            .addTheory("Building a Production JAR",
                "Spring Boot creates a 'fat JAR' - everything in one file:\n\n" +
                "Build with Maven:\n" +
                "./mvnw clean package\n\n" +
                "This creates:\n" +
                "target/myapp-0.0.1-SNAPSHOT.jar\n\n" +
                "Run the JAR:\n" +
                "java -jar target/myapp-0.0.1-SNAPSHOT.jar\n\n" +
                "What's inside the JAR?\n" +
                "- Your compiled code (.class files)\n" +
                "- All dependencies (Spring, database drivers, etc.)\n" +
                "- Embedded web server (Tomcat)\n" +
                "- Configuration files (application.yml)\n\n" +
                "RESULT: One file that runs anywhere with Java installed!\n\n" +
                "Skip tests during build (faster, but risky):\n" +
                "./mvnw clean package -DskipTests\n\n" +
                "Build with specific profile:\n" +
                "./mvnw clean package -Pprod\n\n" +
                "Rename JAR to something simpler:\n" +
                "In pom.xml:\n" +
                "<build>\n" +
                "    <finalName>myapp</finalName>\n" +
                "</build>\n\n" +
                "Now creates: target/myapp.jar")
            .addTheory("Environment-Specific Configuration",
                "DON'T hardcode production values!\n\n" +
                "BAD:\n" +
                "spring.datasource.url=jdbc:postgresql://prod-server:5432/db\n" +
                "spring.datasource.password=supersecret123\n\n" +
                "GOOD: Use Spring Profiles\n\n" +
                "application.yml (default/shared config):\n" +
                "spring:\n" +
                "  application:\n" +
                "    name: myapp\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: validate  # Safe default\n\n" +
                "application-dev.yml (development):\n" +
                "spring:\n" +
                "  datasource:\n" +
                "    url: jdbc:postgresql://localhost:5432/myapp_dev\n" +
                "    username: dev_user\n" +
                "    password: dev_password\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: create-drop  # Recreate DB each time\n" +
                "    show-sql: true  # Show all SQL queries\n\n" +
                "application-prod.yml (production):\n" +
                "spring:\n" +
                "  datasource:\n" +
                "    url: ${DATABASE_URL}  # From environment variable!\n" +
                "    username: ${DATABASE_USERNAME}\n" +
                "    password: ${DATABASE_PASSWORD}\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: validate  # NEVER recreate production DB!\n" +
                "    show-sql: false  # Don't log SQL (performance)\n\n" +
                "Activate profile when running:\n" +
                "java -jar myapp.jar --spring.profiles.active=prod\n\n" +
                "Or set environment variable:\n" +
                "export SPRING_PROFILES_ACTIVE=prod\n" +
                "java -jar myapp.jar")
            .addKeyPoint("Environment Variables for Secrets",
                "NEVER commit secrets to Git!\n\n" +
                "❌ BAD - Hardcoded in application.yml:\n" +
                "spring.datasource.password=mysecretpassword\n" +
                "jwt.secret=my-super-secret-key-12345\n\n" +
                "✓ GOOD - Environment variables:\n" +
                "spring.datasource.password=${DATABASE_PASSWORD}\n" +
                "jwt.secret=${JWT_SECRET}\n\n" +
                "Set on server:\n" +
                "export DATABASE_PASSWORD='actual-secret-password'\n" +
                "export JWT_SECRET='actual-jwt-secret-key'\n" +
                "java -jar myapp.jar\n\n" +
                "Or pass directly:\n" +
                "java -jar myapp.jar \\\n" +
                "  --spring.datasource.password=secret123 \\\n" +
                "  --jwt.secret=my-jwt-key\n\n" +
                "Spring Boot automatically converts:\n" +
                "DATABASE_URL → spring.datasource.url\n" +
                "DATABASE_USERNAME → spring.datasource.username\n" +
                "DATABASE_PASSWORD → spring.datasource.password\n\n" +
                "Why environment variables?\n" +
                "✓ Different values per environment (dev, test, prod)\n" +
                "✓ Secrets not in Git (security)\n" +
                "✓ Easy to change without rebuilding\n" +
                "✓ Cloud platforms (AWS, Azure, Heroku) support them")
            .addTheory("Docker Deployment (Modern Standard)",
                "Docker packages your app + Java + OS into one container\n\n" +
                "Benefits:\n" +
                "✓ Runs the same everywhere (your laptop, AWS, Azure)\n" +
                "✓ No 'it works on my machine' problems\n" +
                "✓ Easy to scale (run 10 copies)\n" +
                "✓ Isolated from other apps\n\n" +
                "Simple Dockerfile:\n" +
                "# Use official Java image\n" +
                "FROM eclipse-temurin:17-jre\n\n" +
                "# Set working directory\n" +
                "WORKDIR /app\n\n" +
                "# Copy JAR file\n" +
                "COPY target/myapp.jar app.jar\n\n" +
                "# Expose port\n" +
                "EXPOSE 8080\n\n" +
                "# Run application\n" +
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n\n" +
                "Build Docker image:\n" +
                "docker build -t myapp:1.0 .\n\n" +
                "Run container:\n" +
                "docker run -p 8080:8080 \\\n" +
                "  -e DATABASE_URL=jdbc:postgresql://db:5432/myapp \\\n" +
                "  -e DATABASE_PASSWORD=secret \\\n" +
                "  myapp:1.0\n\n" +
                "Access app: http://localhost:8080")
            .addTheory("Multi-Stage Docker Build (Best Practice 2024-2025)",
                "Build and run in same Dockerfile - smaller final image:\n\n" +
                "# Stage 1: Build\n" +
                "FROM eclipse-temurin:17-jdk as builder\n" +
                "WORKDIR /app\n" +
                "COPY pom.xml .\n" +
                "COPY src ./src\n" +
                "RUN ./mvnw clean package -DskipTests\n\n" +
                "# Stage 2: Run\n" +
                "FROM eclipse-temurin:17-jre\n" +
                "WORKDIR /app\n" +
                "COPY --from=builder /app/target/myapp.jar app.jar\n" +
                "EXPOSE 8080\n" +
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n\n" +
                "Why multi-stage?\n" +
                "✓ Smaller image (no Maven, no source code)\n" +
                "✓ Faster downloads\n" +
                "✓ More secure (fewer tools = less attack surface)\n\n" +
                "Image sizes:\n" +
                "- Single stage with JDK: ~500 MB\n" +
                "- Multi-stage with JRE: ~250 MB\n\n" +
                "Layer optimization (Spring Boot 2.3+):\n" +
                "Separate layers for dependencies vs your code:\n\n" +
                "# Extract layers\n" +
                "FROM eclipse-temurin:17-jdk as builder\n" +
                "WORKDIR /app\n" +
                "COPY target/myapp.jar app.jar\n" +
                "RUN java -Djarmode=layertools -jar app.jar extract\n\n" +
                "# Run with layers\n" +
                "FROM eclipse-temurin:17-jre\n" +
                "WORKDIR /app\n" +
                "COPY --from=builder /app/dependencies/ ./\n" +
                "COPY --from=builder /app/spring-boot-loader/ ./\n" +
                "COPY --from=builder /app/snapshot-dependencies/ ./\n" +
                "COPY --from=builder /app/application/ ./\n" +
                "ENTRYPOINT [\"java\", \"org.springframework.boot.loader.JarLauncher\"]\n\n" +
                "Benefit: Dependencies cached, only your code layer changes!")
            .addTheory("Production-Ready Features",
                "Add Spring Boot Actuator for monitoring:\n\n" +
                "pom.xml:\n" +
                "<dependency>\n" +
                "    <groupId>org.springframework.boot</groupId>\n" +
                "    <artifactId>spring-boot-starter-actuator</artifactId>\n" +
                "</dependency>\n\n" +
                "application-prod.yml:\n" +
                "management:\n" +
                "  endpoints:\n" +
                "    web:\n" +
                "      exposure:\n" +
                "        include: health,info,metrics\n" +
                "  endpoint:\n" +
                "    health:\n" +
                "      show-details: when-authorized\n\n" +
                "Endpoints:\n" +
                "GET /actuator/health - Is app running?\n" +
                "{\n" +
                "  \"status\": \"UP\",\n" +
                "  \"components\": {\n" +
                "    \"db\": {\"status\": \"UP\"},\n" +
                "    \"diskSpace\": {\"status\": \"UP\"}\n" +
                "  }\n" +
                "}\n\n" +
                "GET /actuator/info - App information\n" +
                "GET /actuator/metrics - Performance metrics\n\n" +
                "Custom health check:\n" +
                "@Component\n" +
                "public class DatabaseHealthIndicator implements HealthIndicator {\n" +
                "    private final UserRepository userRepository;\n" +
                "    \n" +
                "    public DatabaseHealthIndicator(UserRepository userRepository) {\n" +
                "        this.userRepository = userRepository;\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public Health health() {\n" +
                "        try {\n" +
                "            long count = userRepository.count();\n" +
                "            return Health.up()\n" +
                "                .withDetail(\"database\", \"reachable\")\n" +
                "                .withDetail(\"userCount\", count)\n" +
                "                .build();\n" +
                "        } catch (Exception e) {\n" +
                "            return Health.down()\n" +
                "                .withDetail(\"error\", e.getMessage())\n" +
                "                .build();\n" +
                "        }\n" +
                "    }\n" +
                "}")
            .addWarning("Production Security Checklist",
                "Before deploying to production:\n\n" +
                "✓ HTTPS only (no HTTP)\n" +
                "server.ssl.enabled=true\n\n" +
                "✓ Hide actuator endpoints from public\n" +
                "management.endpoints.web.exposure.include=health\n" +
                "# Or require authentication\n\n" +
                "✓ Disable debug mode\n" +
                "logging.level.root=INFO  # Not DEBUG\n\n" +
                "✓ Don't show SQL queries\n" +
                "spring.jpa.show-sql=false\n\n" +
                "✓ Validate database schema only\n" +
                "spring.jpa.hibernate.ddl-auto=validate  # NOT create-drop!\n\n" +
                "✓ Use environment variables for secrets\n" +
                "# Never hardcode passwords\n\n" +
                "✓ Enable CORS only for your frontend\n" +
                "@CrossOrigin(origins = \"https://myapp.com\")\n" +
                "# NOT @CrossOrigin(\"*\")\n\n" +
                "✓ Set reasonable rate limits\n" +
                "# Prevent abuse\n\n" +
                "✓ Log to files, not just console\n" +
                "logging.file.name=/var/log/myapp.log\n\n" +
                "✓ Set up monitoring and alerts\n" +
                "# Know when things break!")
            .addExample("Complete Production Deployment",
                "STEP 1: Build the application\n" +
                "./mvnw clean package\n\n" +
                "STEP 2: Create Dockerfile\n" +
                "FROM eclipse-temurin:17-jre\n" +
                "WORKDIR /app\n" +
                "COPY target/myapp.jar app.jar\n" +
                "EXPOSE 8080\n" +
                "HEALTHCHECK --interval=30s --timeout=3s \\\n" +
                "  CMD curl -f http://localhost:8080/actuator/health || exit 1\n" +
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n\n" +
                "STEP 3: Create docker-compose.yml\n" +
                "version: '3.8'\n" +
                "services:\n" +
                "  db:\n" +
                "    image: postgres:15\n" +
                "    environment:\n" +
                "      POSTGRES_DB: myapp\n" +
                "      POSTGRES_USER: appuser\n" +
                "      POSTGRES_PASSWORD: ${DB_PASSWORD}\n" +
                "    volumes:\n" +
                "      - postgres-data:/var/lib/postgresql/data\n" +
                "    \n" +
                "  app:\n" +
                "    build: .\n" +
                "    ports:\n" +
                "      - \"8080:8080\"\n" +
                "    environment:\n" +
                "      SPRING_PROFILES_ACTIVE: prod\n" +
                "      DATABASE_URL: jdbc:postgresql://db:5432/myapp\n" +
                "      DATABASE_USERNAME: appuser\n" +
                "      DATABASE_PASSWORD: ${DB_PASSWORD}\n" +
                "    depends_on:\n" +
                "      - db\n" +
                "    restart: unless-stopped\n\n" +
                "volumes:\n" +
                "  postgres-data:\n\n" +
                "STEP 4: Create .env file (DON'T commit this!)\n" +
                "DB_PASSWORD=your-secure-password-here\n\n" +
                "STEP 5: Deploy\n" +
                "docker-compose up -d\n\n" +
                "STEP 6: Check health\n" +
                "curl http://localhost:8080/actuator/health\n\n" +
                "STEP 7: View logs\n" +
                "docker-compose logs -f app\n\n" +
                "STEP 8: Update application\n" +
                "./mvnw clean package\n" +
                "docker-compose build app\n" +
                "docker-compose up -d app")
            .addTheory("Logging in Production",
                "Configure proper logging:\n\n" +
                "application-prod.yml:\n" +
                "logging:\n" +
                "  level:\n" +
                "    root: INFO\n" +
                "    com.myapp: INFO\n" +
                "    org.springframework.web: WARN\n" +
                "  file:\n" +
                "    name: /var/log/myapp/application.log\n" +
                "  pattern:\n" +
                "    console: \"%d{yyyy-MM-dd HH:mm:ss} - %msg%n\"\n" +
                "    file: \"%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n\"\n\n" +
                "Use structured logging:\n\n" +
                "@RestController\n" +
                "public class UserController {\n" +
                "    private static final Logger logger = \n" +
                "        LoggerFactory.getLogger(UserController.class);\n" +
                "    \n" +
                "    @PostMapping(\"/api/users\")\n" +
                "    public User create(@RequestBody User user) {\n" +
                "        logger.info(\"Creating user: email={}\", user.getEmail());\n" +
                "        try {\n" +
                "            User saved = userService.create(user);\n" +
                "            logger.info(\"User created: id={}, email={}\", \n" +
                "                       saved.getId(), saved.getEmail());\n" +
                "            return saved;\n" +
                "        } catch (DuplicateResourceException e) {\n" +
                "            logger.warn(\"Duplicate user creation attempt: email={}\", \n" +
                "                       user.getEmail());\n" +
                "            throw e;\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(\"Failed to create user: email={}\", \n" +
                "                        user.getEmail(), e);\n" +
                "            throw e;\n" +
                "        }\n" +
                "    }\n" +
                "}\n\n" +
                "NEVER log sensitive data:\n" +
                "❌ logger.info(\"User: {}\", user.getPassword());\n" +
                "❌ logger.info(\"Credit card: {}\", creditCard);\n" +
                "✓ logger.info(\"User authenticated: id={}\", user.getId());")
            .addChallenge(createDeploymentQuiz())
            .addChallenge(createDockerQuiz())
            .addChallenge(createSecurityQuiz())
            .estimatedMinutes(55)
            .build();
    }

    private static Challenge createDeploymentQuiz() {
        return new Challenge.Builder("epoch-8-lesson-5-deployment", "Deployment Methods", ChallengeType.MULTIPLE_CHOICE)
            .description("What command builds a Spring Boot application into a JAR file?")
            .addMultipleChoiceOption("A) ./mvnw spring-boot:run")
            .addMultipleChoiceOption("B) ./mvnw clean package")
            .addMultipleChoiceOption("C) ./mvnw deploy")
            .addMultipleChoiceOption("D) java -jar myapp.jar")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createDockerQuiz() {
        return new Challenge.Builder("epoch-8-lesson-5-docker", "Docker Best Practices", ChallengeType.MULTIPLE_CHOICE)
            .description("Why use multi-stage Docker builds?")
            .addMultipleChoiceOption("A) To run multiple applications")
            .addMultipleChoiceOption("B) To create smaller, more secure images")
            .addMultipleChoiceOption("C) To deploy to multiple servers")
            .addMultipleChoiceOption("D) To build multiple JAR files")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSecurityQuiz() {
        return new Challenge.Builder("epoch-8-lesson-5-security", "Production Security", ChallengeType.MULTIPLE_CHOICE)
            .description("What should you NEVER do in production?")
            .addMultipleChoiceOption("A) Use environment variables")
            .addMultipleChoiceOption("B) Enable health check endpoints")
            .addMultipleChoiceOption("C) Set ddl-auto to create-drop")
            .addMultipleChoiceOption("D) Use INFO level logging")
            .correctAnswer("C")
            .build();
    }
}
