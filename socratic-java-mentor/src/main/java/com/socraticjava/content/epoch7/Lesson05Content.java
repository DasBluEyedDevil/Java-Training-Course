package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 7.5: Application Properties and Configuration Management
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-5", "Lesson 7.5: Configuration - Making Your App Flexible")
            .addTheory("The Problem: Hardcoded Values Everywhere",
                "Bad code with hardcoded values:\n\n" +
                "@Service\n" +
                "public class EmailService {\n" +
                "    private String smtpHost = \"smtp.gmail.com\";     // Hardcoded!\n" +
                "    private int smtpPort = 587;                      // Hardcoded!\n" +
                "    private String apiKey = \"abc123xyz\";            // Hardcoded!\n" +
                "    \n" +
                "    public void sendEmail(String to, String message) {\n" +
                "        // Use hardcoded values...\n" +
                "    }\n" +
                "}\n\n" +
                "PROBLEMS:\n" +
                "❌ Different values for dev/test/production\n" +
                "❌ Must recompile to change values\n" +
                "❌ Secrets exposed in code (security risk!)\n" +
                "❌ Can't configure without modifying source\n\n" +
                "Real apps need:\n" +
                "- Dev: localhost:3306 for database\n" +
                "- Test: test-server:3306\n" +
                "- Production: prod-server.com:3306\n\n" +
                "Solution: EXTERNAL CONFIGURATION")
            .addAnalogy("Configuration is Like a TV Remote Settings Menu",
                "HARDCODED (No Remote):\n" +
                "- Brightness welded to 100%\n" +
                "- Volume stuck at 50\n" +
                "- Want to change? Open TV, solder new circuits!\n\n" +
                "This is like hardcoding values in Java code.\n\n" +
                "CONFIGURABLE (Settings Menu):\n" +
                "- Brightness: adjustable 0-100%\n" +
                "- Volume: adjustable 0-100\n" +
                "- Language: English, Spanish, French...\n" +
                "- Change anytime WITHOUT opening the TV!\n\n" +
                "This is Spring Boot configuration:\n" +
                "- Values in application.properties or application.yml\n" +
                "- Change without recompiling code\n" +
                "- Different settings for different environments")
            .addTheory("application.properties - The Configuration File",
                "Spring Boot reads configuration from application.properties:\n\n" +
                "Location: src/main/resources/application.properties\n\n" +
                "# Database configuration\n" +
                "spring.datasource.url=jdbc:mysql://localhost:3306/mydb\n" +
                "spring.datasource.username=root\n" +
                "spring.datasource.password=secret\n\n" +
                "# Server configuration\n" +
                "server.port=8080\n\n" +
                "# Custom application properties\n" +
                "app.name=My Awesome App\n" +
                "app.version=1.0.0\n" +
                "app.email.host=smtp.gmail.com\n" +
                "app.email.port=587\n\n" +
                "Spring automatically applies these settings!\n\n" +
                "COMMON PROPERTIES:\n" +
                "- server.port: Change default port (8080)\n" +
                "- spring.datasource.*: Database connection\n" +
                "- spring.jpa.hibernate.ddl-auto: create, update, validate\n" +
                "- logging.level.*: Control log levels")
            .addTheory("application.yml - The Modern Alternative",
                "YAML format is more readable for complex configs:\n\n" +
                "Location: src/main/resources/application.yml\n\n" +
                "spring:\n" +
                "  datasource:\n" +
                "    url: jdbc:mysql://localhost:3306/mydb\n" +
                "    username: root\n" +
                "    password: secret\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: update\n" +
                "    show-sql: true\n\n" +
                "server:\n" +
                "  port: 8080\n\n" +
                "app:\n" +
                "  name: My Awesome App\n" +
                "  version: 1.0.0\n" +
                "  email:\n" +
                "    host: smtp.gmail.com\n" +
                "    port: 587\n" +
                "    username: user@example.com\n\n" +
                "YAML BENEFITS:\n" +
                "✓ Hierarchical structure (easier to read)\n" +
                "✓ No repetition of prefixes\n" +
                "✓ Supports lists and complex objects\n" +
                "✓ Preferred for modern Spring Boot apps\n\n" +
                "You can use EITHER .properties OR .yml (not both!)")
            .addTheory("@Value - Simple Property Injection",
                "@Value injects single values from configuration:\n\n" +
                "application.yml:\n" +
                "app:\n" +
                "  name: My App\n" +
                "  max-users: 100\n\n" +
                "Java code:\n\n" +
                "@Service\n" +
                "public class AppService {\n" +
                "    \n" +
                "    @Value(\"${app.name}\")\n" +
                "    private String appName;\n" +
                "    \n" +
                "    @Value(\"${app.max-users}\")\n" +
                "    private int maxUsers;\n" +
                "    \n" +
                "    @Value(\"${app.feature.enabled:false}\")  // Default value\n" +
                "    private boolean featureEnabled;\n" +
                "    \n" +
                "    public void printConfig() {\n" +
                "        System.out.println(\"App: \" + appName);\n" +
                "        System.out.println(\"Max users: \" + maxUsers);\n" +
                "    }\n" +
                "}\n\n" +
                "SYNTAX:\n" +
                "@Value(\"${property.name}\") - Read from config\n" +
                "@Value(\"${property.name:default}\") - With default value\n\n" +
                "USE @Value FOR:\n" +
                "✓ Simple, individual properties\n" +
                "✓ One or two values\n\n" +
                "DON'T USE @Value FOR:\n" +
                "✗ Complex, grouped configurations\n" +
                "✗ Many related properties")
            .addTheory("@ConfigurationProperties - Modern Best Practice (Spring Boot 3)",
                "For complex configs, use @ConfigurationProperties with Records:\n\n" +
                "application.yml:\n" +
                "app:\n" +
                "  email:\n" +
                "    host: smtp.gmail.com\n" +
                "    port: 587\n" +
                "    username: user@example.com\n" +
                "    password: secret123\n" +
                "  features:\n" +
                "    signup-enabled: true\n" +
                "    max-upload-size: 10485760\n\n" +
                "Java Record (Java 17+):\n\n" +
                "@ConfigurationProperties(prefix = \"app.email\")\n" +
                "public record EmailProperties(\n" +
                "    String host,\n" +
                "    int port,\n" +
                "    String username,\n" +
                "    String password\n" +
                ") { }\n\n" +
                "Enable in main class:\n\n" +
                "@SpringBootApplication\n" +
                "@ConfigurationPropertiesScan  // Scan for @ConfigurationProperties\n" +
                "public class MyApplication {\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(MyApplication.class, args);\n" +
                "    }\n" +
                "}\n\n" +
                "Use in services:\n\n" +
                "@Service\n" +
                "public class EmailService {\n" +
                "    private final EmailProperties emailProps;\n" +
                "    \n" +
                "    public EmailService(EmailProperties emailProps) {\n" +
                "        this.emailProps = emailProps;\n" +
                "    }\n" +
                "    \n" +
                "    public void sendEmail(String to, String message) {\n" +
                "        String host = emailProps.host();  // smtp.gmail.com\n" +
                "        int port = emailProps.port();      // 587\n" +
                "        // Send email...\n" +
                "    }\n" +
                "}")
            .addKeyPoint("Why @ConfigurationProperties is Better Than @Value",
                "@VALUE (Old Way):\n" +
                "@Service\n" +
                "public class EmailService {\n" +
                "    @Value(\"${app.email.host}\")\n" +
                "    private String host;\n" +
                "    \n" +
                "    @Value(\"${app.email.port}\")\n" +
                "    private int port;\n" +
                "    \n" +
                "    @Value(\"${app.email.username}\")\n" +
                "    private String username;\n" +
                "    \n" +
                "    @Value(\"${app.email.password}\")\n" +
                "    private String password;\n" +
                "    \n" +
                "    // 4 separate annotations, scattered!\n" +
                "}\n\n" +
                "@CONFIGURATIONPROPERTIES (Modern Way):\n" +
                "@ConfigurationProperties(prefix = \"app.email\")\n" +
                "public record EmailProperties(\n" +
                "    String host,\n" +
                "    int port,\n" +
                "    String username,\n" +
                "    String password\n" +
                ") { }\n\n" +
                "BENEFITS:\n" +
                "✓ TYPE-SAFE: Compile-time checking\n" +
                "✓ GROUPED: All related properties together\n" +
                "✓ IMMUTABLE: Records are immutable by default\n" +
                "✓ VALIDATION: Can use @Validated annotations\n" +
                "✓ AUTOCOMPLETE: IDE helps with property names\n" +
                "✓ REUSABLE: Inject same config into multiple services")
            .addTheory("Validation with @ConfigurationProperties",
                "Add validation to configuration:\n\n" +
                "import jakarta.validation.constraints.*;\n\n" +
                "@ConfigurationProperties(prefix = \"app.email\")\n" +
                "@Validated  // Enable validation\n" +
                "public record EmailProperties(\n" +
                "    @NotBlank(message = \"Email host is required\")\n" +
                "    String host,\n" +
                "    \n" +
                "    @Min(value = 1, message = \"Port must be positive\")\n" +
                "    @Max(value = 65535, message = \"Port must be valid\")\n" +
                "    int port,\n" +
                "    \n" +
                "    @Email(message = \"Invalid email format\")\n" +
                "    String username,\n" +
                "    \n" +
                "    @Size(min = 8, message = \"Password must be at least 8 characters\")\n" +
                "    String password\n" +
                ") { }\n\n" +
                "If configuration is invalid, Spring Boot FAILS TO START:\n" +
                "- Shows clear error message\n" +
                "- Prevents running with bad config\n" +
                "- Catches errors early!")
            .addTheory("Environment-Specific Configuration",
                "Different configs for dev, test, production:\n\n" +
                "FILE STRUCTURE:\n" +
                "src/main/resources/\n" +
                "  ├── application.yml              (default)\n" +
                "  ├── application-dev.yml          (development)\n" +
                "  ├── application-test.yml         (testing)\n" +
                "  └── application-prod.yml         (production)\n\n" +
                "application.yml (default):\n" +
                "spring:\n" +
                "  profiles:\n" +
                "    active: dev  # Default profile\n\n" +
                "application-dev.yml:\n" +
                "spring:\n" +
                "  datasource:\n" +
                "    url: jdbc:mysql://localhost:3306/dev_db\n" +
                "server:\n" +
                "  port: 8080\n\n" +
                "application-prod.yml:\n" +
                "spring:\n" +
                "  datasource:\n" +
                "    url: jdbc:mysql://prod-server:3306/prod_db\n" +
                "server:\n" +
                "  port: 80\n\n" +
                "ACTIVATE PROFILE:\n" +
                "1. In application.yml: spring.profiles.active=prod\n" +
                "2. Command line: java -jar app.jar --spring.profiles.active=prod\n" +
                "3. Environment variable: SPRING_PROFILES_ACTIVE=prod\n\n" +
                "Spring merges default + active profile configs!")
            .addWarning("Never Commit Secrets to Git!",
                "❌ BAD - Secrets in application.yml:\n" +
                "app:\n" +
                "  database:\n" +
                "    password: mySecretPassword123  # DON'T DO THIS!\n" +
                "  api:\n" +
                "    key: sk_live_abc123xyz  # SECURITY RISK!\n\n" +
                "✓ GOOD - Use environment variables:\n\n" +
                "application.yml:\n" +
                "app:\n" +
                "  database:\n" +
                "    password: ${DB_PASSWORD}  # Read from environment\n" +
                "  api:\n" +
                "    key: ${API_KEY}\n\n" +
                "Set environment variables:\n" +
                "export DB_PASSWORD=mySecretPassword123\n" +
                "export API_KEY=sk_live_abc123xyz\n\n" +
                "OR use application-local.yml (gitignored):\n\n" +
                ".gitignore:\n" +
                "application-local.yml\n\n" +
                "BEST PRACTICES:\n" +
                "✓ Use environment variables for secrets\n" +
                "✓ Use .env files (not committed to git)\n" +
                "✓ Use secret management (AWS Secrets Manager, HashiCorp Vault)\n" +
                "✓ Never commit passwords, API keys, tokens")
            .addExample("Complete Configuration Example",
                "application.yml:\n\n" +
                "spring:\n" +
                "  application:\n" +
                "    name: book-store-api\n" +
                "  datasource:\n" +
                "    url: jdbc:mysql://localhost:3306/bookstore\n" +
                "    username: ${DB_USER:root}\n" +
                "    password: ${DB_PASSWORD:password}\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: update\n" +
                "    show-sql: true\n\n" +
                "server:\n" +
                "  port: 8080\n\n" +
                "app:\n" +
                "  features:\n" +
                "    signup-enabled: true\n" +
                "    max-users: 1000\n" +
                "  email:\n" +
                "    host: smtp.gmail.com\n" +
                "    port: 587\n" +
                "    username: ${EMAIL_USER}\n" +
                "    password: ${EMAIL_PASSWORD}\n\n" +
                "Configuration classes:\n\n" +
                "@ConfigurationProperties(prefix = \"app.features\")\n" +
                "public record FeaturesConfig(\n" +
                "    boolean signupEnabled,\n" +
                "    int maxUsers\n" +
                ") { }\n\n" +
                "@ConfigurationProperties(prefix = \"app.email\")\n" +
                "@Validated\n" +
                "public record EmailConfig(\n" +
                "    @NotBlank String host,\n" +
                "    @Min(1) @Max(65535) int port,\n" +
                "    @NotBlank String username,\n" +
                "    @NotBlank String password\n" +
                ") { }\n\n" +
                "Main application:\n\n" +
                "@SpringBootApplication\n" +
                "@ConfigurationPropertiesScan\n" +
                "public class BookStoreApplication {\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(BookStoreApplication.class, args);\n" +
                "    }\n" +
                "}\n\n" +
                "Using in service:\n\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    private final FeaturesConfig features;\n" +
                "    \n" +
                "    public UserService(FeaturesConfig features) {\n" +
                "        this.features = features;\n" +
                "    }\n" +
                "    \n" +
                "    public boolean canSignup() {\n" +
                "        return features.signupEnabled();\n" +
                "    }\n" +
                "}")
            .addChallenge(createConfigQuiz())
            .addChallenge(createYamlQuiz())
            .addChallenge(createConfigPropertiesQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createConfigQuiz() {
        return new Challenge.Builder("epoch-7-lesson-5-config", "Configuration Basics", ChallengeType.MULTIPLE_CHOICE)
            .description("Where should you store application configuration in Spring Boot?")
            .addMultipleChoiceOption("A) Hardcoded in Java classes")
            .addMultipleChoiceOption("B) In application.properties or application.yml")
            .addMultipleChoiceOption("C) In database tables")
            .addMultipleChoiceOption("D) In comments")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createYamlQuiz() {
        return new Challenge.Builder("epoch-7-lesson-5-yaml", "YAML vs Properties", ChallengeType.MULTIPLE_CHOICE)
            .description("What is an advantage of YAML (.yml) over .properties files?")
            .addMultipleChoiceOption("A) YAML is faster to process")
            .addMultipleChoiceOption("B) YAML uses less disk space")
            .addMultipleChoiceOption("C) YAML has hierarchical structure, easier to read for complex configs")
            .addMultipleChoiceOption("D) YAML is required for Spring Boot 3")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createConfigPropertiesQuiz() {
        return new Challenge.Builder("epoch-7-lesson-5-configprops", "@ConfigurationProperties", ChallengeType.MULTIPLE_CHOICE)
            .description("Why is @ConfigurationProperties preferred over @Value for complex configurations in Spring Boot 3?")
            .addMultipleChoiceOption("A) @Value doesn't work in Spring Boot 3")
            .addMultipleChoiceOption("B) @ConfigurationProperties is type-safe, grouped, and supports validation")
            .addMultipleChoiceOption("C) @ConfigurationProperties is faster")
            .addMultipleChoiceOption("D) @Value requires more dependencies")
            .correctAnswer("B")
            .build();
    }
}
