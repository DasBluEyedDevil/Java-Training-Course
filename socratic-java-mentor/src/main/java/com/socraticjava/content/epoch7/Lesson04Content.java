package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 7.4: Dependency Injection and Spring Beans
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-4", "Lesson 7.4: Dependency Injection - The Heart of Spring")
            .addTheory("The Problem: Classes That Create Their Own Dependencies",
                "Without dependency injection:\n\n" +
                "public class UserController {\n" +
                "    private UserService userService = new UserService();  // BAD!\n" +
                "    \n" +
                "    public User getUser(Long id) {\n" +
                "        return userService.findById(id);\n" +
                "    }\n" +
                "}\n\n" +
                "PROBLEMS:\n" +
                "❌ Tight Coupling - UserController is glued to UserService\n" +
                "❌ Hard to Test - Can't replace UserService with a mock\n" +
                "❌ Inflexible - Can't swap implementations\n" +
                "❌ Manual Lifecycle - You manage object creation\n\n" +
                "What if UserService needs a database connection?\n" +
                "Now UserController must manage that too!\n\n" +
                "Solution: DEPENDENCY INJECTION (DI)")
            .addAnalogy("Dependency Injection is Like a Restaurant Kitchen",
                "WITHOUT DI (You Make Everything):\n" +
                "- Build your own stove\n" +
                "- Raise your own chickens\n" +
                "- Grow your own vegetables\n" +
                "- Make your own plates\n" +
                "- THEN cook dinner\n\n" +
                "This is like 'new UserService()' - you manage everything!\n\n" +
                "WITH DI (Restaurant Provides Everything):\n" +
                "- Professional stove (already installed)\n" +
                "- Fresh ingredients (delivered daily)\n" +
                "- Clean dishes (washed and ready)\n" +
                "- Trained staff\n\n" +
                "You just: COOK THE MEAL\n\n" +
                "Spring Container = Kitchen Manager:\n" +
                "1. Knows about all beans (objects it manages)\n" +
                "2. Creates them when needed\n" +
                "3. Injects them where required\n" +
                "4. Manages lifecycle (startup, shutdown)")
            .addTheory("Spring Boot 3 Best Practice: Constructor Injection",
                "Mark classes as Spring beans:\n\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    private final UserRepository userRepository;\n" +
                "    \n" +
                "    // Constructor injection (BEST PRACTICE)\n" +
                "    public UserService(UserRepository userRepository) {\n" +
                "        this.userRepository = userRepository;\n" +
                "    }\n" +
                "    \n" +
                "    public User findById(Long id) {\n" +
                "        return userRepository.findById(id).orElse(null);\n" +
                "    }\n" +
                "}\n\n" +
                "Then inject into controllers:\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/users\")\n" +
                "public class UserController {\n" +
                "    private final UserService userService;\n" +
                "    \n" +
                "    // Spring automatically injects UserService\n" +
                "    // No @Autowired needed for single constructor!\n" +
                "    public UserController(UserService userService) {\n" +
                "        this.userService = userService;\n" +
                "    }\n" +
                "    \n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public User getUser(@PathVariable Long id) {\n" +
                "        return userService.findById(id);\n" +
                "    }\n" +
                "}\n\n" +
                "The full stack:\n" +
                "Controller → Service → Repository → Database\n" +
                "Spring wires this chain automatically!")
            .addTheory("Spring Stereotype Annotations",
                "These tell Spring to manage a class as a bean:\n\n" +
                "@Component - Generic bean (utility classes)\n" +
                "@Service - Business logic layer\n" +
                "@Repository - Data access layer\n" +
                "@Controller - Web MVC controller (returns HTML)\n" +
                "@RestController - REST API controller (returns JSON)\n\n" +
                "Example:\n\n" +
                "@Service\n" +
                "public class EmailService {\n" +
                "    // Spring manages this\n" +
                "}\n\n" +
                "@Repository\n" +
                "public interface UserRepository extends JpaRepository<User, Long> {\n" +
                "    // Spring implements this\n" +
                "}\n\n" +
                "All of these are variations of @Component!")
            .addTheory("Why Constructor Injection with 'final'?",
                "@Service\n" +
                "public class OrderService {\n" +
                "    private final UserService userService;      // final = immutable\n" +
                "    private final PaymentService paymentService;\n" +
                "    \n" +
                "    public OrderService(UserService userService, \n" +
                "                       PaymentService paymentService) {\n" +
                "        this.userService = userService;\n" +
                "        this.paymentService = paymentService;\n" +
                "    }\n" +
                "}\n\n" +
                "BENEFITS OF 'final':\n" +
                "✓ Guarantees field is set exactly once\n" +
                "✓ Thread-safe\n" +
                "✓ Makes dependencies explicit\n" +
                "✓ Prevents accidental reassignment\n\n" +
                "When Spring starts:\n" +
                "1. Scans for @Component, @Service, etc.\n" +
                "2. Creates instances\n" +
                "3. Calls constructors with required dependencies\n" +
                "4. Stores beans in Application Context")
            .addWarning("Avoid Field Injection (Old Way)",
                "❌ OLD STYLE - Not recommended in Spring Boot 3:\n\n" +
                "@Service\n" +
                "public class OldStyleService {\n" +
                "    @Autowired\n" +
                "    private UserRepository userRepository;  // Field injection\n" +
                "}\n\n" +
                "WHY AVOID?\n" +
                "- Cannot make field 'final'\n" +
                "- Harder to test (requires reflection)\n" +
                "- Dependencies not explicit\n" +
                "- Nullable references possible\n\n" +
                "✓ MODERN STYLE - Use constructor injection:\n\n" +
                "@Service\n" +
                "public class ModernService {\n" +
                "    private final UserRepository userRepository;\n" +
                "    \n" +
                "    public ModernService(UserRepository userRepository) {\n" +
                "        this.userRepository = userRepository;\n" +
                "    }\n" +
                "}")
            .addKeyPoint("When @Autowired IS Still Needed",
                "Only when you have MULTIPLE constructors:\n\n" +
                "@Service\n" +
                "public class FlexibleService {\n" +
                "    private final UserService userService;\n" +
                "    private final Optional<CacheService> cacheService;\n" +
                "    \n" +
                "    // Tell Spring to use THIS constructor\n" +
                "    @Autowired\n" +
                "    public FlexibleService(UserService userService,\n" +
                "                          @Autowired(required = false) CacheService cache) {\n" +
                "        this.userService = userService;\n" +
                "        this.cacheService = Optional.ofNullable(cache);\n" +
                "    }\n" +
                "    \n" +
                "    // Alternative constructor for testing\n" +
                "    public FlexibleService(UserService userService) {\n" +
                "        this(userService, null);\n" +
                "    }\n" +
                "}\n\n" +
                "BEAN SCOPES:\n" +
                "By default, beans are Singleton (one instance):\n\n" +
                "@Service  // Singleton by default\n" +
                "public class UserService { }\n\n" +
                "Other scopes:\n" +
                "@Service\n" +
                "@Scope(\"prototype\")  // New instance every time\n" +
                "public class PrototypeService { }\n\n" +
                "Most of the time, Singleton is what you want!")
            .addExample("Complete Multi-Layer Service",
                "Repository:\n\n" +
                "@Repository\n" +
                "public interface BookRepository extends JpaRepository<Book, Long> {\n" +
                "    // Spring auto-implements CRUD methods\n" +
                "}\n\n" +
                "Service:\n\n" +
                "@Service\n" +
                "public class BookService {\n" +
                "    private final BookRepository bookRepository;\n" +
                "    \n" +
                "    public BookService(BookRepository bookRepository) {\n" +
                "        this.bookRepository = bookRepository;\n" +
                "    }\n" +
                "    \n" +
                "    public List<Book> getAllBooks() {\n" +
                "        return bookRepository.findAll();\n" +
                "    }\n" +
                "    \n" +
                "    public Book saveBook(Book book) {\n" +
                "        return bookRepository.save(book);\n" +
                "    }\n" +
                "}\n\n" +
                "Controller:\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/books\")\n" +
                "public class BookController {\n" +
                "    private final BookService bookService;\n" +
                "    \n" +
                "    public BookController(BookService bookService) {\n" +
                "        this.bookService = bookService;\n" +
                "    }\n" +
                "    \n" +
                "    @GetMapping\n" +
                "    public List<Book> getAll() {\n" +
                "        return bookService.getAllBooks();\n" +
                "    }\n" +
                "    \n" +
                "    @PostMapping\n" +
                "    public Book create(@RequestBody Book book) {\n" +
                "        return bookService.saveBook(book);\n" +
                "    }\n" +
                "}\n\n" +
                "The Dependency Chain:\n" +
                "BookController → BookService → BookRepository → Database\n\n" +
                "Spring automatically wires everything!")
            .addChallenge(createDIQuiz())
            .addChallenge(createConstructorQuiz())
            .addChallenge(createAnnotationQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createDIQuiz() {
        return new Challenge.Builder("epoch-7-lesson-4-di", "Understanding DI", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the preferred way to inject dependencies in Spring Boot 3?")
            .addMultipleChoiceOption("A) Field injection with @Autowired")
            .addMultipleChoiceOption("B) Setter injection")
            .addMultipleChoiceOption("C) Constructor injection with final fields")
            .addMultipleChoiceOption("D) Static method injection")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createConstructorQuiz() {
        return new Challenge.Builder("epoch-7-lesson-4-final", "Why Use 'final'?", ChallengeType.MULTIPLE_CHOICE)
            .description("Why should you use 'final' with constructor-injected dependencies?")
            .addMultipleChoiceOption("A) It makes the code run faster")
            .addMultipleChoiceOption("B) It ensures the dependency is set exactly once and cannot be changed")
            .addMultipleChoiceOption("C) It's required by Spring Boot 3")
            .addMultipleChoiceOption("D) It reduces memory usage")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createAnnotationQuiz() {
        return new Challenge.Builder("epoch-7-lesson-4-annotations", "Spring Annotations", ChallengeType.MULTIPLE_CHOICE)
            .description("Which annotation tells Spring to manage a class for business logic?")
            .addMultipleChoiceOption("A) @Component")
            .addMultipleChoiceOption("B) @Service")
            .addMultipleChoiceOption("C) @Repository")
            .addMultipleChoiceOption("D) @Bean")
            .correctAnswer("B")
            .build();
    }
}
