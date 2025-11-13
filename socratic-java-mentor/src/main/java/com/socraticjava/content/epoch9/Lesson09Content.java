package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.9: Deploying Your Application to Production
 */
public class Lesson09Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-9", "Lesson 9.9: Deployment - From Local to Production")
            .addTheory("The Final Step: Deployment",
                "Your app works locally. Now make it accessible to the world!\n\n" +
                "DEPLOYMENT JOURNEY:\n\n" +
                "1. LOCAL DEVELOPMENT:\n" +
                "   - ./mvnw spring-boot:run\n" +
                "   - Only you can access it\n" +
                "   - Database on localhost\n\n" +
                "2. CONTAINERIZATION (Docker):\n" +
                "   - Package app + database together\n" +
                "   - Runs the same everywhere\n" +
                "   - Easy to deploy\n\n" +
                "3. PRODUCTION ENVIRONMENT:\n" +
                "   - Cloud server (AWS, Azure, Heroku)\n" +
                "   - Real domain (taskmanager.com)\n" +
                "   - HTTPS encryption\n" +
                "   - Always available (99.9% uptime)\n\n" +
                "GOAL: Deploy your task manager so others can use it!")
            .addAnalogy("Deployment is Like Publishing a Book",
                "WRITING (Development):\n" +
                "Draft on your computer\n" +
                "Only you can read it\n" +
                "Like: Running on localhost:8080\n\n" +
                "PRINTING (Building Docker image):\n" +
                "Convert manuscript to printed books\n" +
                "Same content, portable format\n" +
                "Like: docker build creates image\n\n" +
                "BOOKSTORE (Cloud server):\n" +
                "Place books on shelves\n" +
                "Anyone can buy them\n" +
                "Like: Deploy to AWS/Heroku\n\n" +
                "ISBN (Domain name):\n" +
                "taskmanager.com instead of localhost:8080\n" +
                "Like: Custom domain\n\n" +
                "DISTRIBUTION (CI/CD):\n" +
                "Automatic printing when you update draft\n" +
                "Like: Auto-deploy on git push")
            .addTheory("Step 1: Create Production-Ready Dockerfile",
                "Create: Dockerfile (in project root)\n\n" +
                "# Multi-stage build for smaller image (2024-2025 best practice)\n" +
                "FROM eclipse-temurin:21-jdk-alpine AS builder\n" +
                "WORKDIR /app\n" +
                "\n" +
                "# Copy Maven wrapper and pom.xml first (for layer caching)\n" +
                "COPY .mvn/ .mvn/\n" +
                "COPY mvnw pom.xml ./\n" +
                "\n" +
                "# Download dependencies (cached if pom.xml unchanged)\n" +
                "RUN ./mvnw dependency:go-offline\n" +
                "\n" +
                "# Copy source code\n" +
                "COPY src ./src\n" +
                "\n" +
                "# Build application\n" +
                "RUN ./mvnw clean package -DskipTests\n" +
                "\n" +
                "# Production stage - smaller image\n" +
                "FROM eclipse-temurin:21-jre-alpine\n" +
                "WORKDIR /app\n" +
                "\n" +
                "# Create non-root user for security\n" +
                "RUN addgroup -S spring && adduser -S spring -G spring\n" +
                "USER spring:spring\n" +
                "\n" +
                "# Copy JAR from builder\n" +
                "COPY --from=builder /app/target/*.jar app.jar\n" +
                "\n" +
                "# Expose port\n" +
                "EXPOSE 8080\n" +
                "\n" +
                "# Health check\n" +
                "HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \\\n" +
                "  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1\n" +
                "\n" +
                "# Run application\n" +
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n\n" +
                "KEY FEATURES:\n" +
                "✓ Multi-stage build (smaller final image)\n" +
                "✓ Layer caching (faster builds)\n" +
                "✓ Non-root user (security)\n" +
                "✓ Health check (monitoring)\n" +
                "✓ Alpine Linux (lightweight)")
            .addTheory("Step 2: Create Docker Compose Configuration",
                "Create: docker-compose.yml\n\n" +
                "version: '3.8'\n\n" +
                "services:\n" +
                "  # PostgreSQL Database\n" +
                "  db:\n" +
                "    image: postgres:15-alpine\n" +
                "    container_name: taskmanager-db\n" +
                "    environment:\n" +
                "      POSTGRES_DB: taskmanager\n" +
                "      POSTGRES_USER: ${DB_USER:-taskuser}\n" +
                "      POSTGRES_PASSWORD: ${DB_PASSWORD:?Database password required}\n" +
                "    volumes:\n" +
                "      - postgres-data:/var/lib/postgresql/data\n" +
                "    ports:\n" +
                "      - \"5432:5432\"\n" +
                "    healthcheck:\n" +
                "      test: [\"CMD-SHELL\", \"pg_isready -U taskuser\"]\n" +
                "      interval: 10s\n" +
                "      timeout: 5s\n" +
                "      retries: 5\n" +
                "    networks:\n" +
                "      - taskmanager-network\n" +
                "\n" +
                "  # Spring Boot Application\n" +
                "  app:\n" +
                "    build:\n" +
                "      context: .\n" +
                "      dockerfile: Dockerfile\n" +
                "    container_name: taskmanager-app\n" +
                "    environment:\n" +
                "      SPRING_PROFILES_ACTIVE: prod\n" +
                "      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/taskmanager\n" +
                "      SPRING_DATASOURCE_USERNAME: ${DB_USER:-taskuser}\n" +
                "      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD:?Database password required}\n" +
                "      SPRING_JPA_HIBERNATE_DDL_AUTO: validate\n" +
                "      SERVER_PORT: 8080\n" +
                "    ports:\n" +
                "      - \"8080:8080\"\n" +
                "    depends_on:\n" +
                "      db:\n" +
                "        condition: service_healthy\n" +
                "    restart: unless-stopped\n" +
                "    networks:\n" +
                "      - taskmanager-network\n" +
                "\n" +
                "volumes:\n" +
                "  postgres-data:\n" +
                "\n" +
                "networks:\n" +
                "  taskmanager-network:\n" +
                "    driver: bridge\n\n" +
                "KEY FEATURES:\n" +
                "✓ Environment variables (secure secrets)\n" +
                "✓ Health checks (wait for database)\n" +
                "✓ Named volumes (persistent data)\n" +
                "✓ Custom network (container communication)\n" +
                "✓ Restart policy (automatic recovery)")
            .addKeyPoint("Step 3: Environment Variables for Production",
                "Create: .env (NEVER commit this file!)\n\n" +
                "# Database Configuration\n" +
                "DB_USER=taskuser\n" +
                "DB_PASSWORD=super-secure-password-change-this\n\n" +
                "# Application Configuration\n" +
                "SPRING_PROFILES_ACTIVE=prod\n\n" +
                "Add to .gitignore:\n" +
                ".env\n" +
                "*.env\n\n" +
                "Create: .env.example (commit this one)\n\n" +
                "# Database Configuration\n" +
                "DB_USER=taskuser\n" +
                "DB_PASSWORD=your-secure-password-here\n\n" +
                "# Application Configuration\n" +
                "SPRING_PROFILES_ACTIVE=prod\n\n" +
                "WHY?\n" +
                "✓ .env contains real secrets (never in Git)\n" +
                "✓ .env.example shows what variables are needed\n" +
                "✓ Team members copy .env.example to .env\n" +
                "✓ Each environment has different .env file\n\n" +
                "PRODUCTION SECRETS:\n" +
                "On AWS/Azure/Heroku:\n" +
                "- Set environment variables in dashboard\n" +
                "- Never hardcode in docker-compose.yml\n" +
                "- Use secrets management (AWS Secrets Manager, etc.)")
            .addTheory("Step 4: Deploy with Docker Compose",
                "BUILD AND RUN:\n\n" +
                "# Build the Docker image\n" +
                "docker compose build\n\n" +
                "# Start all services (app + database)\n" +
                "docker compose up -d\n\n" +
                "# View logs\n" +
                "docker compose logs -f app\n\n" +
                "# Check status\n" +
                "docker compose ps\n\n" +
                "Expected output:\n" +
                "NAME                  IMAGE              STATUS\n" +
                "taskmanager-db        postgres:15-alpine  Up (healthy)\n" +
                "taskmanager-app       taskmanager_app     Up\n\n" +
                "ACCESS APPLICATION:\n" +
                "http://localhost:8080\n\n" +
                "VERIFY HEALTH:\n" +
                "curl http://localhost:8080/actuator/health\n" +
                "{\n" +
                "  \"status\": \"UP\",\n" +
                "  \"components\": {\n" +
                "    \"db\": {\"status\": \"UP\"},\n" +
                "    \"diskSpace\": {\"status\": \"UP\"}\n" +
                "  }\n" +
                "}\n\n" +
                "USEFUL COMMANDS:\n" +
                "docker compose down          # Stop all services\n" +
                "docker compose down -v       # Stop and delete volumes\n" +
                "docker compose restart app   # Restart app only\n" +
                "docker compose exec app sh   # Shell into container\n" +
                "docker compose logs db       # Database logs")
            .addTheory("Step 5: Cloud Deployment Options (2024-2025)",
                "OPTION 1: HEROKU (Easiest, Free Tier)\n\n" +
                "1. Install Heroku CLI:\n" +
                "   curl https://cli-assets.heroku.com/install.sh | sh\n\n" +
                "2. Login:\n" +
                "   heroku login\n\n" +
                "3. Create app:\n" +
                "   heroku create taskmanager-yourname\n\n" +
                "4. Add PostgreSQL:\n" +
                "   heroku addons:create heroku-postgresql:mini\n\n" +
                "5. Set environment variables:\n" +
                "   heroku config:set SPRING_PROFILES_ACTIVE=prod\n\n" +
                "6. Deploy:\n" +
                "   git push heroku main\n\n" +
                "7. Open app:\n" +
                "   heroku open\n\n" +
                "URL: https://taskmanager-yourname.herokuapp.com\n\n" +
                "OPTION 2: AWS (Professional, Scalable)\n\n" +
                "Using AWS Elastic Beanstalk:\n" +
                "1. Package app: ./mvnw package\n" +
                "2. Create Elastic Beanstalk application\n" +
                "3. Upload JAR file\n" +
                "4. Add RDS PostgreSQL database\n" +
                "5. Configure environment variables\n\n" +
                "OPTION 3: DIGITAL OCEAN (Simple, Affordable)\n\n" +
                "Using App Platform:\n" +
                "1. Connect GitHub repository\n" +
                "2. Configure build command: ./mvnw package\n" +
                "3. Add managed PostgreSQL database\n" +
                "4. Auto-deploys on git push\n\n" +
                "OPTION 4: RAILWAY.APP (Modern, Developer-Friendly)\n\n" +
                "1. Connect GitHub repo\n" +
                "2. Add PostgreSQL service\n" +
                "3. Auto-detects Spring Boot\n" +
                "4. Deploys automatically\n\n" +
                "COST COMPARISON (2024-2025):\n" +
                "Heroku: Free tier (limited hours) → $7/month\n" +
                "Railway: $5/month credit → $20+/month\n" +
                "DigitalOcean: $5/month → $12+/month\n" +
                "AWS: Pay-as-you-go (can be free tier eligible)")
            .addTheory("Step 6: Production Checklist",
                "BEFORE DEPLOYING TO PRODUCTION:\n\n" +
                "SECURITY:\n" +
                "☐ All passwords use BCrypt hashing\n" +
                "☐ No secrets in Git repository\n" +
                "☐ HTTPS enabled (not HTTP)\n" +
                "☐ CORS configured for specific domain\n" +
                "☐ SQL injection protection (JPA handles this)\n" +
                "☐ XSS protection (frontend escapes HTML)\n" +
                "☐ Rate limiting on login endpoint\n\n" +
                "DATABASE:\n" +
                "☐ ddl-auto set to 'validate' (NOT create-drop)\n" +
                "☐ Database backups configured\n" +
                "☐ Connection pool configured\n" +
                "☐ Indexes on frequently queried columns\n\n" +
                "MONITORING:\n" +
                "☐ Spring Boot Actuator enabled\n" +
                "☐ Health check endpoint working\n" +
                "☐ Logging configured (not just console)\n" +
                "☐ Error tracking (Sentry, Rollbar)\n" +
                "☐ Uptime monitoring (UptimeRobot, Pingdom)\n\n" +
                "PERFORMANCE:\n" +
                "☐ Database queries optimized (no N+1)\n" +
                "☐ Static files cached\n" +
                "☐ Gzip compression enabled\n" +
                "☐ Connection timeouts configured\n\n" +
                "TESTING:\n" +
                "☐ All tests passing (./mvnw test)\n" +
                "☐ Manual testing in production-like environment\n" +
                "☐ Load testing (optional but recommended)\n\n" +
                "DOCUMENTATION:\n" +
                "☐ README with setup instructions\n" +
                "☐ API documentation (optional)\n" +
                "☐ Environment variables documented")
            .addExample("Step 7: CI/CD with GitHub Actions",
                "Automate deployment on every push to main:\n\n" +
                "Create: .github/workflows/deploy.yml\n\n" +
                "name: Deploy to Production\n\n" +
                "on:\n" +
                "  push:\n" +
                "    branches: [ main ]\n\n" +
                "jobs:\n" +
                "  test:\n" +
                "    runs-on: ubuntu-latest\n" +
                "    steps:\n" +
                "      - uses: actions/checkout@v4\n" +
                "      \n" +
                "      - name: Set up JDK 21\n" +
                "        uses: actions/setup-java@v4\n" +
                "        with:\n" +
                "          java-version: '21'\n" +
                "          distribution: 'temurin'\n" +
                "      \n" +
                "      - name: Run tests\n" +
                "        run: ./mvnw test\n" +
                "\n" +
                "  deploy:\n" +
                "    needs: test\n" +
                "    runs-on: ubuntu-latest\n" +
                "    if: github.ref == 'refs/heads/main'\n" +
                "    steps:\n" +
                "      - uses: actions/checkout@v4\n" +
                "      \n" +
                "      - name: Deploy to Heroku\n" +
                "        uses: akhileshns/heroku-deploy@v3.13.15\n" +
                "        with:\n" +
                "          heroku_api_key: ${{ secrets.HEROKU_API_KEY }}\n" +
                "          heroku_app_name: \"taskmanager-yourname\"\n" +
                "          heroku_email: \"your.email@example.com\"\n\n" +
                "WORKFLOW:\n" +
                "1. Push code to GitHub\n" +
                "2. GitHub Actions runs tests\n" +
                "3. If tests pass, deploys to Heroku\n" +
                "4. Users see new version automatically\n\n" +
                "ADD HEROKU API KEY:\n" +
                "1. Get key: heroku auth:token\n" +
                "2. GitHub → Settings → Secrets → New secret\n" +
                "3. Name: HEROKU_API_KEY\n" +
                "4. Value: paste token")
            .addWarning("Common Deployment Mistakes",
                "❌ MISTAKE 1: Using development profile in production\n" +
                "ddl-auto: create-drop (deletes all data on restart!)\n" +
                "FIX: SPRING_PROFILES_ACTIVE=prod, ddl-auto: validate\n\n" +
                "❌ MISTAKE 2: Hardcoding database URL\n" +
                "url: jdbc:postgresql://localhost:5432/db\n" +
                "FIX: Use environment variables\n\n" +
                "❌ MISTAKE 3: No health checks\n" +
                "Container appears running but app is crashed\n" +
                "FIX: Add HEALTHCHECK in Dockerfile\n\n" +
                "❌ MISTAKE 4: Root user in container\n" +
                "Security vulnerability\n" +
                "FIX: Create non-root user in Dockerfile\n\n" +
                "❌ MISTAKE 5: Exposing sensitive endpoints\n" +
                "/actuator/env shows all environment variables!\n" +
                "FIX: Restrict actuator endpoints in production\n\n" +
                "❌ MISTAKE 6: No database backups\n" +
                "Data loss if server fails\n" +
                "FIX: Configure automated backups\n\n" +
                "❌ MISTAKE 7: Ignoring logs\n" +
                "App fails, no idea why\n" +
                "FIX: Set up log monitoring (Papertrail, Loggly)\n\n" +
                "❌ MISTAKE 8: No rollback plan\n" +
                "Deployment breaks everything\n" +
                "FIX: Keep previous version, test before full rollout")
            .addKeyPoint("Best Practices Summary (2024-2025)",
                "CONTAINERIZATION:\n" +
                "✓ Use multi-stage Docker builds\n" +
                "✓ Run as non-root user\n" +
                "✓ Use Alpine Linux for smaller images\n" +
                "✓ Add health checks\n" +
                "✓ Use .dockerignore (exclude target/, node_modules/)\n\n" +
                "CONFIGURATION:\n" +
                "✓ Environment variables for all secrets\n" +
                "✓ Different profiles (dev, test, prod)\n" +
                "✓ Never commit .env files\n" +
                "✓ Document required env vars in .env.example\n\n" +
                "PRODUCTION:\n" +
                "✓ Use managed database (don't self-host)\n" +
                "✓ Enable HTTPS (Let's Encrypt is free)\n" +
                "✓ Set up monitoring and alerts\n" +
                "✓ Configure automated backups\n" +
                "✓ Use CDN for static files\n\n" +
                "CI/CD:\n" +
                "✓ Automate testing (GitHub Actions, GitLab CI)\n" +
                "✓ Auto-deploy on merge to main\n" +
                "✓ Test before deploy\n" +
                "✓ Easy rollback mechanism\n\n" +
                "MONITORING:\n" +
                "✓ Health check endpoints\n" +
                "✓ Application performance monitoring (APM)\n" +
                "✓ Error tracking (Sentry)\n" +
                "✓ Uptime monitoring\n" +
                "✓ Log aggregation (ELK, Papertrail)\n\n" +
                "NEXT STEPS:\n" +
                "- Add custom domain\n" +
                "- Implement caching (Redis)\n" +
                "- Add email notifications\n" +
                "- Implement JWT for better auth\n" +
                "- Add API rate limiting\n" +
                "- Set up staging environment")
            .addChallenge(createDockerQuiz())
            .addChallenge(createEnvironmentQuiz())
            .addChallenge(createProductionQuiz())
            .estimatedMinutes(60)
            .build();
    }

    private static Challenge createDockerQuiz() {
        return new Challenge.Builder("epoch-9-lesson-9-docker", "Docker Deployment", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the benefit of multi-stage Docker builds?")
            .addMultipleChoiceOption("A) Faster deployment")
            .addMultipleChoiceOption("B) Smaller final image size")
            .addMultipleChoiceOption("C) More security")
            .addMultipleChoiceOption("D) Better performance")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createEnvironmentQuiz() {
        return new Challenge.Builder("epoch-9-lesson-9-environment", "Environment Variables", ChallengeType.MULTIPLE_CHOICE)
            .description("Where should production secrets be stored?")
            .addMultipleChoiceOption("A) In application.yml")
            .addMultipleChoiceOption("B) In the source code")
            .addMultipleChoiceOption("C) In environment variables")
            .addMultipleChoiceOption("D) In a .txt file")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createProductionQuiz() {
        return new Challenge.Builder("epoch-9-lesson-9-production", "Production Configuration", ChallengeType.MULTIPLE_CHOICE)
            .description("What should ddl-auto be set to in production?")
            .addMultipleChoiceOption("A) create")
            .addMultipleChoiceOption("B) create-drop")
            .addMultipleChoiceOption("C) update")
            .addMultipleChoiceOption("D) validate")
            .correctAnswer("D")
            .build();
    }
}
