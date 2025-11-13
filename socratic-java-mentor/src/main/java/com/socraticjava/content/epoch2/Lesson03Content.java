package com.socraticjava.content.epoch2;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 2.3: Encapsulation and Access Modifiers
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-2-lesson-3", "Lesson 2.3: Encapsulation - Protecting Your Data")
            .addTheory("The Problem: Anyone Can Mess With Your Objects",
                "Remember our Car class?\n\n" +
                "class Car {\n" +
                "    String model;\n" +
                "    int speed;\n" +
                "}\n\n" +
                "What if someone does this?\n\n" +
                "Car myCar = new Car();\n" +
                "myCar.speed = -500;  // Negative speed?!\n" +
                "myCar.speed = 999999;  // Impossible speed!\n\n" +
                "Right now, ANYONE can directly change our object's fields to nonsensical values.\n" +
                "We need CONTROL over how our data gets accessed and modified.\n\n" +
                "This is where ENCAPSULATION comes in.")
            .addAnalogy("Encapsulation is Like a Bank Account",
                "WITHOUT ENCAPSULATION:\n" +
                "= Money sitting in a box on your desk\n" +
                "- Anyone can take money out\n" +
                "- Anyone can add fake money\n" +
                "- No security or validation\n\n" +
                "WITH ENCAPSULATION:\n" +
                "= Money in a bank account\n" +
                "- You can't directly touch the money\n" +
                "- Must use deposit() method (validates amount)\n" +
                "- Must use withdraw() method (checks balance)\n" +
                "- Bank controls access and enforces rules\n\n" +
                "In Java:\n" +
                "- Fields = private (like money in vault)\n" +
                "- Methods = public (like bank teller window)\n" +
                "- Methods validate before changing fields")
            .addTheory("Access Modifiers: public, private, protected",
                "Java has keywords that control WHO can access your code:\n\n" +
                "1. PUBLIC - Anyone can access\n" +
                "   public int speed;  // Any code anywhere can read/write\n\n" +
                "2. PRIVATE - Only THIS class can access\n" +
                "   private int speed;  // Only methods inside Car can access\n\n" +
                "3. PROTECTED - This class + subclasses (we'll learn later)\n\n" +
                "4. DEFAULT (no keyword) - Same package only\n\n" +
                "BEST PRACTICE:\n" +
                "- Fields: ALWAYS private\n" +
                "- Methods: public if others need them, private if internal only")
            .addTheory("Getters and Setters: Controlled Access",
                "Instead of public fields, use private fields + public methods:\n\n" +
                "class Car {\n" +
                "    private int speed;  // Hidden from outside\n" +
                "    \n" +
                "    // GETTER - Read the value\n" +
                "    public int getSpeed() {\n" +
                "        return speed;\n" +
                "    }\n" +
                "    \n" +
                "    // SETTER - Change the value (with validation!)\n" +
                "    public void setSpeed(int newSpeed) {\n" +
                "        if (newSpeed >= 0 && newSpeed <= 200) {\n" +
                "            speed = newSpeed;\n" +
                "        } else {\n" +
                "            System.out.println(\"Invalid speed!\");\n" +
                "        }\n" +
                "    }\n" +
                "}\n\n" +
                "Now:\n" +
                "Car myCar = new Car();\n" +
                "myCar.speed = -500;  // COMPILE ERROR! speed is private\n" +
                "myCar.setSpeed(-500);  // Prints \"Invalid speed!\", doesn't change\n" +
                "myCar.setSpeed(60);  // Works! Sets speed to 60\n" +
                "System.out.println(myCar.getSpeed());  // 60")
            .addKeyPoint("Why Encapsulation Matters",
                "1. DATA VALIDATION\n" +
                "   - Prevent invalid states (negative speed, empty names)\n" +
                "   - Enforce business rules\n\n" +
                "2. FLEXIBILITY\n" +
                "   - Change internal implementation without breaking other code\n" +
                "   - Example: Change from int to double, only modify inside class\n\n" +
                "3. DEBUGGING\n" +
                "   - Put breakpoints in setters to find who's changing data\n" +
                "   - Add logging to track changes\n\n" +
                "4. SECURITY\n" +
                "   - Hide sensitive data\n" +
                "   - Control who can modify critical fields\n\n" +
                "Professional code ALWAYS uses encapsulation!")
            .addChallenge(createEncapsulationQuiz())
            .addChallenge(createBankAccountChallenge())
            .addChallenge(createStudentChallenge())
            .addChallenge(createValidationChallenge())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createEncapsulationQuiz() {
        return new Challenge.Builder("epoch-2-lesson-3-quiz", "Understanding Encapsulation", ChallengeType.MULTIPLE_CHOICE)
            .description("Why should fields be private instead of public?")
            .addMultipleChoiceOption("A) It makes the code run faster")
            .addMultipleChoiceOption("B) It prevents direct access and allows validation through methods")
            .addMultipleChoiceOption("C) It uses less memory")
            .addMultipleChoiceOption("D) Private fields don't need getters and setters")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createBankAccountChallenge() {
        return new Challenge.Builder("epoch-2-lesson-3-bank", "Create Encapsulated BankAccount", ChallengeType.CODE_COMPLETION)
            .description("Create a BankAccount class with:\n" +
                "- private field: balance (double)\n" +
                "- constructor that sets initial balance\n" +
                "- getBalance() method\n" +
                "- deposit(double amount) method (only if amount > 0)\n" +
                "- withdraw(double amount) method (only if amount > 0 and balance sufficient)")
            .starterCode(
                "public class BankAccount {\n" +
                "    // Add private field here\n" +
                "    \n" +
                "    // Add constructor\n" +
                "    \n" +
                "    // Add getBalance method\n" +
                "    \n" +
                "    // Add deposit method\n" +
                "    \n" +
                "    // Add withdraw method\n" +
                "    \n" +
                "}")
            .addTestCase(new TestCase(
                "BankAccount acc = new BankAccount(100.0);\nreturn acc.getBalance();",
                "100.0",
                true
            ))
            .addTestCase(new TestCase(
                "BankAccount acc = new BankAccount(100.0);\nacc.deposit(50.0);\nreturn acc.getBalance();",
                "150.0",
                true
            ))
            .addTestCase(new TestCase(
                "BankAccount acc = new BankAccount(100.0);\nacc.withdraw(30.0);\nreturn acc.getBalance();",
                "70.0",
                true
            ))
            .addTestCase(new TestCase(
                "BankAccount acc = new BankAccount(100.0);\nacc.withdraw(200.0);\nreturn acc.getBalance();",
                "100.0",
                true,
                "Withdrawal should fail if balance insufficient"
            ))
            .addTestCase(new TestCase(
                "BankAccount acc = new BankAccount(100.0);\nacc.deposit(-50.0);\nreturn acc.getBalance();",
                "100.0",
                false,
                "Negative deposit should be rejected"
            ))
            .build();
    }

    private static Challenge createStudentChallenge() {
        return new Challenge.Builder("epoch-2-lesson-3-student", "Create Encapsulated Student", ChallengeType.CODE_COMPLETION)
            .description("Create a Student class with:\n" +
                "- private fields: name (String), grade (int)\n" +
                "- constructor\n" +
                "- getName() and getGrade() methods\n" +
                "- setGrade(int g) that only accepts grades 0-100")
            .starterCode(
                "public class Student {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Student s = new Student(\"Alice\", 85);\nreturn s.getName();",
                "Alice",
                true
            ))
            .addTestCase(new TestCase(
                "Student s = new Student(\"Bob\", 90);\nreturn s.getGrade();",
                "90",
                true
            ))
            .addTestCase(new TestCase(
                "Student s = new Student(\"Carol\", 80);\ns.setGrade(95);\nreturn s.getGrade();",
                "95",
                true
            ))
            .addTestCase(new TestCase(
                "Student s = new Student(\"Dave\", 75);\ns.setGrade(150);\nreturn s.getGrade();",
                "75",
                false,
                "Invalid grade (>100) should be rejected"
            ))
            .build();
    }

    private static Challenge createValidationChallenge() {
        return new Challenge.Builder("epoch-2-lesson-3-product", "Product with Validation", ChallengeType.FREE_CODING)
            .description("Create a Product class with:\n" +
                "- private fields: name (String), price (double)\n" +
                "- constructor\n" +
                "- getName() and getPrice() methods\n" +
                "- setPrice(double p) that only accepts prices > 0")
            .starterCode(
                "public class Product {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Product p = new Product(\"Laptop\", 999.99);\nreturn p.getName() + \"-\" + p.getPrice();",
                "Laptop-999.99",
                true
            ))
            .addTestCase(new TestCase(
                "Product p = new Product(\"Mouse\", 25.0);\np.setPrice(30.0);\nreturn p.getPrice();",
                "30.0",
                true
            ))
            .addTestCase(new TestCase(
                "Product p = new Product(\"Keyboard\", 50.0);\np.setPrice(-10.0);\nreturn p.getPrice();",
                "50.0",
                false,
                "Negative price should be rejected"
            ))
            .build();
    }
}
