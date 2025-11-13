package com.socraticjava.content.epoch4;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 4.2: Writing JUnit Tests
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-4-lesson-2", "Lesson 4.2: Writing Your First JUnit Tests")
            .addTheory("The Anatomy of a JUnit Test",
                "A JUnit test has three parts: ARRANGE, ACT, ASSERT\n\n" +
                "@Test\n" +
                "public void testAdd() {\n" +
                "    // ARRANGE: Set up test data\n" +
                "    Calculator calc = new Calculator();\n" +
                "    \n" +
                "    // ACT: Call the method being tested\n" +
                "    int result = calc.add(2, 3);\n" +
                "    \n" +
                "    // ASSERT: Verify the result\n" +
                "    assertEquals(5, result);\n" +
                "}\n\n" +
                "KEY ANNOTATIONS:\n" +
                "@Test - Marks a method as a test\n" +
                "@BeforeEach - Runs before each test (setup)\n" +
                "@AfterEach - Runs after each test (cleanup)\n" +
                "@BeforeAll - Runs once before all tests\n" +
                "@AfterAll - Runs once after all tests")
            .addTheory("Common Assertion Methods",
                "JUnit provides assertion methods to verify results:\n\n" +
                "EQUALITY:\n" +
                "assertEquals(expected, actual);\n" +
                "assertEquals(5, calculator.add(2, 3));\n\n" +
                "BOOLEAN:\n" +
                "assertTrue(condition);\n" +
                "assertTrue(list.isEmpty());\n" +
                "assertFalse(condition);\n" +
                "assertFalse(list.contains(\"X\"));\n\n" +
                "NULL CHECKS:\n" +
                "assertNull(object);\n" +
                "assertNotNull(object);\n\n" +
                "EXCEPTIONS:\n" +
                "assertThrows(IllegalArgumentException.class, () -> {\n" +
                "    calculator.divide(10, 0);\n" +
                "});\n\n" +
                "ARRAYS:\n" +
                "assertArrayEquals(expectedArray, actualArray);\n\n" +
                "CUSTOM MESSAGE:\n" +
                "assertEquals(5, result, \"Addition failed\");\n" +
                "// If fails, shows: \"Addition failed\"")
            .addAnalogy("Tests are Like Scientific Experiments",
                "HYPOTHESIS (What you expect):\n" +
                "\"When I add 2 + 3, I should get 5\"\n\n" +
                "EXPERIMENT (The test):\n" +
                "@Test\n" +
                "public void testAddition() {\n" +
                "    int result = add(2, 3);\n" +
                "    assertEquals(5, result);  // Verify hypothesis\n" +
                "}\n\n" +
                "RESULT:\n" +
                "✓ Test passes → Hypothesis confirmed\n" +
                "✗ Test fails → Something's wrong, investigate!\n\n" +
                "Just like science: tests help you understand if your code \"theory\" is correct.")
            .addKeyPoint("Test Naming Conventions",
                "GOOD TEST NAMES describe WHAT is being tested:\n\n" +
                "❌ Bad names:\n" +
                "test1()\n" +
                "testStuff()\n" +
                "myTest()\n\n" +
                "✓ Good names:\n" +
                "testAddPositiveNumbers()\n" +
                "testDivideByZeroThrowsException()\n" +
                "testEmptyListReturnsZero()\n\n" +
                "PATTERN:\n" +
                "test[MethodName][Scenario][ExpectedResult]\n\n" +
                "Examples:\n" +
                "testCalculateDiscountWithValidCouponReturnsReducedPrice()\n" +
                "testGetUserWithInvalidIdReturnsNull()\n" +
                "testSortEmptyArrayReturnsEmptyArray()\n\n" +
                "Good names make it OBVIOUS what broke when a test fails!")
            .addExample("Real Test Class Example",
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n\n" +
                "class StringUtilsTest {\n" +
                "    \n" +
                "    @Test\n" +
                "    void testReverseWithNormalString() {\n" +
                "        String result = StringUtils.reverse(\"hello\");\n" +
                "        assertEquals(\"olleh\", result);\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void testReverseWithEmptyString() {\n" +
                "        String result = StringUtils.reverse(\"\");\n" +
                "        assertEquals(\"\", result);\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void testReverseWithNullThrowsException() {\n" +
                "        assertThrows(IllegalArgumentException.class, () -> {\n" +
                "            StringUtils.reverse(null);\n" +
                "        });\n" +
                "    }\n" +
                "}\n\n" +
                "Note: Test different scenarios (normal case, edge cases, error cases)")
            .addWarning("Don't Test Everything - Test What Matters",
                "❌ Don't test:\n" +
                "- Getters/setters (trivial code)\n" +
                "- External libraries (already tested)\n" +
                "- UI code (use different techniques)\n\n" +
                "✓ DO test:\n" +
                "- Business logic\n" +
                "- Complex calculations\n" +
                "- Edge cases (empty lists, null values, boundaries)\n" +
                "- Error handling\n\n" +
                "AIM FOR: 70-80% code coverage\n" +
                "NOT: 100% (diminishing returns, wastes time)")
            .addChallenge(createJUnitQuiz())
            .addChallenge(createCalculatorTestChallenge())
            .addChallenge(createStringTestChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createJUnitQuiz() {
        return new Challenge.Builder("epoch-4-lesson-2-quiz", "Understanding Assertions", ChallengeType.MULTIPLE_CHOICE)
            .description("Which assertion would you use to verify a method throws an exception?")
            .addMultipleChoiceOption("A) assertEquals()")
            .addMultipleChoiceOption("B) assertTrue()")
            .addMultipleChoiceOption("C) assertThrows()")
            .addMultipleChoiceOption("D) assertNull()")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createCalculatorTestChallenge() {
        return new Challenge.Builder("epoch-4-lesson-2-calculator", "Test Calculator Methods", ChallengeType.CONCEPTUAL)
            .description("Given a Calculator class with these methods:\n" +
                "- int add(int a, int b)\n" +
                "- int subtract(int a, int b)\n" +
                "- int multiply(int a, int b)\n\n" +
                "Which test scenarios should you write for the multiply method?\n" +
                "(Select the MOST important scenarios)")
            .addMultipleChoiceOption("A) Only test positive numbers")
            .addMultipleChoiceOption("B) Test positive numbers, negative numbers, zero, and multiplication by 1")
            .addMultipleChoiceOption("C) Test every possible number combination")
            .addMultipleChoiceOption("D) Don't test multiplication, it's too simple")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createStringTestChallenge() {
        return new Challenge.Builder("epoch-4-lesson-2-string", "Writing Test Logic", ChallengeType.CONCEPTUAL)
            .description("You have a method: boolean isPalindrome(String word)\n" +
                "It should return true if the word reads the same forwards and backwards.\n\n" +
                "Which test case is MOST important to include?")
            .addMultipleChoiceOption("A) Test with \"racecar\" (returns true)")
            .addMultipleChoiceOption("B) Test with \"hello\" (returns false)")
            .addMultipleChoiceOption("C) Test with empty string \"\"")
            .addMultipleChoiceOption("D) All of the above")
            .correctAnswer("D")
            .build();
    }
}
