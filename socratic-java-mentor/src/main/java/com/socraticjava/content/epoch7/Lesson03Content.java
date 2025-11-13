package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 7.3: Spring Data JPA - Database Integration
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-3", "Lesson 7.3: Spring Data JPA - No More SQL!")
            .addTheory("The Problem: Writing SQL is Repetitive",
                "Every entity needs similar code:\n\n" +
                "findAll(), findById(), save(), deleteById()...\n\n" +
                "Writing JDBC code:\n" +
                "- PreparedStatement for every query\n" +
                "- ResultSet parsing\n" +
                "- Connection management\n" +
                "- Hundreds of lines of boilerplate!\n\n" +
                "Spring Data JPA eliminates this:\n" +
                "- Auto-generates common queries\n" +
                "- Maps Java objects to database tables\n" +
                "- Handles connections automatically\n" +
                "- You write interfaces, Spring implements them!")
            .addTheory("Creating an Entity",
                "@Entity tells JPA this maps to a database table:\n\n" +
                "@Entity\n" +
                "@Table(name = \"users\")  // Optional: custom table name\n" +
                "public class User {\n" +
                "    \n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @Column(name = \"full_name\", nullable = false, length = 100)\n" +
                "    private String name;\n" +
                "    \n" +
                "    @Column(nullable = false)\n" +
                "    private int age;\n" +
                "    \n" +
                "    @Column(unique = true)\n" +
                "    private String email;\n" +
                "    \n" +
                "    // Getters and setters\n" +
                "}\n\n" +
                "@Entity - Marks as database entity\n" +
                "@Id - Primary key\n" +
                "@GeneratedValue - Auto-increment\n" +
                "@Column - Column properties")
            .addTheory("Creating a Repository",
                "Extend JpaRepository - Spring does the rest:\n\n" +
                "public interface UserRepository extends JpaRepository<User, Long> {\n" +
                "    // That's it! Spring generates implementation\n" +
                "}\n\n" +
                "FREE METHODS (no code needed):\n" +
                "- findAll() → SELECT * FROM users\n" +
                "- findById(id) → SELECT * FROM users WHERE id = ?\n" +
                "- save(user) → INSERT or UPDATE\n" +
                "- deleteById(id) → DELETE FROM users WHERE id = ?\n" +
                "- count() → SELECT COUNT(*) FROM users\n" +
                "- existsById(id) → Check if exists\n\n" +
                "Usage in service:\n\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    @Autowired\n" +
                "    private UserRepository userRepository;\n" +
                "    \n" +
                "    public List<User> getAllUsers() {\n" +
                "        return userRepository.findAll();\n" +
                "    }\n" +
                "    \n" +
                "    public User saveUser(User user) {\n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}")
            .addTheory("Custom Query Methods",
                "Spring Data JPA generates queries from method names!\n\n" +
                "public interface UserRepository extends JpaRepository<User, Long> {\n" +
                "    \n" +
                "    // Find by name\n" +
                "    List<User> findByName(String name);\n" +
                "    // SELECT * FROM users WHERE name = ?\n" +
                "    \n" +
                "    // Find by age greater than\n" +
                "    List<User> findByAgeGreaterThan(int age);\n" +
                "    // SELECT * FROM users WHERE age > ?\n" +
                "    \n" +
                "    // Find by email containing\n" +
                "    List<User> findByEmailContaining(String keyword);\n" +
                "    // SELECT * FROM users WHERE email LIKE %?%\n" +
                "    \n" +
                "    // Find by name and age\n" +
                "    List<User> findByNameAndAge(String name, int age);\n" +
                "    // SELECT * FROM users WHERE name = ? AND age = ?\n" +
                "    \n" +
                "    // Check if exists\n" +
                "    boolean existsByEmail(String email);\n" +
                "    \n" +
                "    // Count by age\n" +
                "    long countByAge(int age);\n" +
                "}\n\n" +
                "METHOD NAME KEYWORDS:\n" +
                "- findBy, getBy, queryBy\n" +
                "- And, Or\n" +
                "- GreaterThan, LessThan, Between\n" +
                "- Like, Containing, StartingWith, EndingWith\n" +
                "- OrderBy...Asc, OrderBy...Desc")
            .addTheory("Custom JPQL Queries",
                "For complex queries, write JPQL (like SQL but for Java objects):\n\n" +
                "public interface UserRepository extends JpaRepository<User, Long> {\n" +
                "    \n" +
                "    @Query(\"SELECT u FROM User u WHERE u.age > :minAge AND u.age < :maxAge\")\n" +
                "    List<User> findUsersByAgeRange(@Param(\"minAge\") int min, @Param(\"maxAge\") int max);\n" +
                "    \n" +
                "    @Query(\"SELECT u FROM User u WHERE u.name LIKE %:keyword%\")\n" +
                "    List<User> searchByName(@Param(\"keyword\") String keyword);\n" +
                "    \n" +
                "    @Query(value = \"SELECT * FROM users WHERE age > ?1\", nativeQuery = true)\n" +
                "    List<User> findUsingNativeSQL(int age);\n" +
                "}\n\n" +
                "@Query - Custom JPQL query\n" +
                "@Param - Named parameter\n" +
                "nativeQuery = true - Use actual SQL instead of JPQL")
            .addKeyPoint("Service Layer Pattern",
                "BEST PRACTICE: Controller → Service → Repository\n\n" +
                "@RestController\n" +
                "public class UserController {\n" +
                "    @Autowired\n" +
                "    private UserService userService;  // Not Repository!\n" +
                "    \n" +
                "    @GetMapping(\"/api/users\")\n" +
                "    public List<User> getAll() {\n" +
                "        return userService.getAllUsers();\n" +
                "    }\n" +
                "}\n\n" +
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
                "        // Business logic here\n" +
                "        validateUser(user);\n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}\n\n" +
                "WHY?\n" +
                "- Separation of concerns\n" +
                "- Business logic in service, not controller\n" +
                "- Easier to test\n" +
                "- Reusable services")
            .addChallenge(createJPAQuiz())
            .addChallenge(createRepositoryQuiz())
            .addChallenge(createQueryMethodQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createJPAQuiz() {
        return new Challenge.Builder("epoch-7-lesson-3-jpa", "Understanding JPA", ChallengeType.MULTIPLE_CHOICE)
            .description("What does @Entity annotation do?")
            .addMultipleChoiceOption("A) Creates a REST endpoint")
            .addMultipleChoiceOption("B) Marks a class as a database entity that maps to a table")
            .addMultipleChoiceOption("C) Handles HTTP requests")
            .addMultipleChoiceOption("D) Validates input data")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createRepositoryQuiz() {
        return new Challenge.Builder("epoch-7-lesson-3-repository", "JpaRepository", ChallengeType.MULTIPLE_CHOICE)
            .description("What does extending JpaRepository give you?")
            .addMultipleChoiceOption("A) Nothing, you must implement all methods")
            .addMultipleChoiceOption("B) Common CRUD methods automatically implemented by Spring")
            .addMultipleChoiceOption("C) Only findAll() method")
            .addMultipleChoiceOption("D) Database connection only")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createQueryMethodQuiz() {
        return new Challenge.Builder("epoch-7-lesson-3-querymethod", "Query Method Names", ChallengeType.MULTIPLE_CHOICE)
            .description("What does findByNameAndAge(String name, int age) do?")
            .addMultipleChoiceOption("A) Finds users where name OR age matches")
            .addMultipleChoiceOption("B) Finds users where name AND age both match")
            .addMultipleChoiceOption("C) Throws an error")
            .addMultipleChoiceOption("D) Finds only by name, ignores age")
            .correctAnswer("B")
            .build();
    }
}
