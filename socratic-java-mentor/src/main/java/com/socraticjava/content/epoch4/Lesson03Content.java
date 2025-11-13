package com.socraticjava.content.epoch4;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 4.3: Test-Driven Development (TDD)
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-4-lesson-3", "Lesson 4.3: Test-Driven Development - Write Tests First!")
            .addTheory("The Traditional Approach (and Its Problems)",
                "TYPICAL development workflow:\n\n" +
                "1. Write code\n" +
                "2. Run it, see if it works\n" +
                "3. Find bugs\n" +
                "4. Fix bugs\n" +
                "5. Maybe write tests later (if you have time)\n\n" +
                "PROBLEMS:\n" +
                "❌ Tests are an afterthought\n" +
                "❌ You might forget edge cases\n" +
                "❌ Code might be hard to test (not designed for it)\n" +
                "❌ Tests often get skipped (\"no time\")\n\n" +
                "What if there was a BETTER way?")
            .addTheory("Test-Driven Development (TDD) - A Revolutionary Approach",
                "TDD FLIPS the process:\n\n" +
                "1. Write a FAILING test first (describes what you want)\n" +
                "2. Write MINIMAL code to make test pass\n" +
                "3. Refactor (improve code quality)\n" +
                "4. Repeat\n\n" +
                "The cycle: RED → GREEN → REFACTOR\n\n" +
                "🔴 RED: Write a failing test\n" +
                "@Test\n" +
                "void testCalculateDiscount() {\n" +
                "    double result = PriceCalculator.calculateDiscount(100, 10);\n" +
                "    assertEquals(90, result);\n" +
                "}\n" +
                "// Fails: PriceCalculator doesn't exist yet!\n\n" +
                "🟢 GREEN: Write just enough code to pass\n" +
                "public class PriceCalculator {\n" +
                "    public static double calculateDiscount(double price, double percent) {\n" +
                "        return price - (price * percent / 100);\n" +
                "    }\n" +
                "}\n" +
                "// Test passes!\n\n" +
                "🔵 REFACTOR: Clean up code\n" +
                "// Code is already clean, move to next test")
            .addAnalogy("TDD is Like Building with a Blueprint",
                "BUILDING WITHOUT BLUEPRINT (traditional):\n" +
                "- Start nailing boards together\n" +
                "- Hope it looks like a house\n" +
                "- Realize door is in wrong place\n" +
                "- Tear down, rebuild\n\n" +
                "BUILDING WITH BLUEPRINT (TDD):\n" +
                "- Draw blueprint first (the test)\n" +
                "- Blueprint shows exactly what you need\n" +
                "- Build to match blueprint\n" +
                "- Verify it matches blueprint\n\n" +
                "TDD TESTS = Blueprints for your code\n" +
                "They specify EXACTLY what the code should do BEFORE you write it.")
            .addKeyPoint("Benefits of TDD",
                "1. FEWER BUGS\n" +
                "   - Tests written first catch issues immediately\n" +
                "   - You think through edge cases upfront\n\n" +
                "2. BETTER DESIGN\n" +
                "   - Code is automatically testable\n" +
                "   - Forces you to write modular, decoupled code\n\n" +
                "3. LIVING DOCUMENTATION\n" +
                "   - Tests show how to use your code\n" +
                "   - Examples of every feature\n\n" +
                "4. CONFIDENCE TO REFACTOR\n" +
                "   - Change code freely\n" +
                "   - Tests verify nothing broke\n\n" +
                "5. LESS DEBUGGING\n" +
                "   - Catch bugs in seconds, not hours\n" +
                "   - Know exactly what's broken\n\n" +
                "Professional developers use TDD for critical code.")
            .addExample("TDD Example: Building a Password Validator",
                "STEP 1: Write first test (RED)\n" +
                "@Test\n" +
                "void testPasswordTooShort() {\n" +
                "    assertFalse(PasswordValidator.isValid(\"abc\"));\n" +
                "}\n" +
                "// Fails: PasswordValidator doesn't exist\n\n" +
                "STEP 2: Write minimal code (GREEN)\n" +
                "public class PasswordValidator {\n" +
                "    public static boolean isValid(String password) {\n" +
                "        return password.length() >= 8;\n" +
                "    }\n" +
                "}\n" +
                "// Test passes!\n\n" +
                "STEP 3: Add another test (RED)\n" +
                "@Test\n" +
                "void testPasswordMustHaveNumber() {\n" +
                "    assertFalse(PasswordValidator.isValid(\"abcdefgh\"));\n" +
                "}\n" +
                "// Fails: password \"abcdefgh\" is considered valid\n\n" +
                "STEP 4: Update code (GREEN)\n" +
                "public static boolean isValid(String password) {\n" +
                "    if (password.length() < 8) return false;\n" +
                "    return password.matches(\".*\\\\d.*\");  // Must contain digit\n" +
                "}\n" +
                "// Both tests pass!\n\n" +
                "STEP 5: Continue with more tests\n" +
                "@Test\n" +
                "void testPasswordMustHaveUppercase() { ... }\n" +
                "@Test\n" +
                "void testValidPassword() { ... }\n\n" +
                "Build feature incrementally, one test at a time!")
            .addWarning("Common TDD Mistakes",
                "❌ Writing too much code at once\n" +
                "   - Only write code to pass the CURRENT test\n\n" +
                "❌ Skipping the refactor step\n" +
                "   - Clean code is as important as working code\n\n" +
                "❌ Testing implementation details\n" +
                "   - Test BEHAVIOR, not internal structure\n\n" +
                "❌ Not running tests frequently\n" +
                "   - Run tests after every small change\n\n" +
                "❌ Writing tests that depend on each other\n" +
                "   - Each test should be independent\n\n" +
                "✓ Good TDD: Small steps, frequent feedback")
            .addChallenge(createTDDQuiz())
            .addChallenge(createRedGreenRefactorQuiz())
            .addChallenge(createBenefitsQuiz())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createTDDQuiz() {
        return new Challenge.Builder("epoch-4-lesson-3-quiz", "Understanding TDD", ChallengeType.MULTIPLE_CHOICE)
            .description("In TDD, when do you write the test?")
            .addMultipleChoiceOption("A) After writing all the code")
            .addMultipleChoiceOption("B) Before writing the code it tests")
            .addMultipleChoiceOption("C) Only if you have time")
            .addMultipleChoiceOption("D) Tests are optional in TDD")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createRedGreenRefactorQuiz() {
        return new Challenge.Builder("epoch-4-lesson-3-cycle", "TDD Cycle", ChallengeType.MULTIPLE_CHOICE)
            .description("What does the RED phase of TDD mean?")
            .addMultipleChoiceOption("A) Write code that compiles")
            .addMultipleChoiceOption("B) Write a failing test")
            .addMultipleChoiceOption("C) Refactor the code")
            .addMultipleChoiceOption("D) Delete bad code")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createBenefitsQuiz() {
        return new Challenge.Builder("epoch-4-lesson-3-benefits", "TDD Benefits", ChallengeType.MULTIPLE_CHOICE)
            .description("Which is NOT a benefit of TDD?")
            .addMultipleChoiceOption("A) Fewer bugs")
            .addMultipleChoiceOption("B) Better code design")
            .addMultipleChoiceOption("C) Code runs faster")
            .addMultipleChoiceOption("D) Living documentation")
            .correctAnswer("C")
            .build();
    }
}
