package com.socraticjava.content.epoch3;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 3.2: ArrayList - Dynamic Arrays
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-3-lesson-2", "Lesson 3.2: ArrayList - Arrays That Grow")
            .addTheory("The Problem: Fixed-Size Arrays",
                "Remember arrays?\n\n" +
                "int[] numbers = new int[5];  // Size is FIXED at 5\n\n" +
                "Problems:\n" +
                "1. What if you need to add a 6th element? Can't do it!\n" +
                "2. What if you need to remove an element? Have to shift manually\n" +
                "3. No built-in methods for common operations\n\n" +
                "Example:\n" +
                "You're building a to-do list app. How many tasks will users have?\n" +
                "- Could be 0\n" +
                "- Could be 5\n" +
                "- Could be 100\n\n" +
                "You CAN'T predict the size!\n\n" +
                "Solution: ArrayList - a dynamic, resizable array!")
            .addAnalogy("ArrayList is Like an Accordion Folder",
                "FIXED ARRAY:\n" +
                "= Box with 5 compartments\n" +
                "- Can hold exactly 5 items\n" +
                "- Want to add a 6th? Too bad!\n" +
                "- Want to remove one? Empty slot remains\n\n" +
                "ARRAYLIST:\n" +
                "= Accordion folder\n" +
                "- Starts with some capacity\n" +
                "- Add items → expands automatically\n" +
                "- Remove items → shrinks, no gaps\n" +
                "- Has methods: add(), remove(), get(), size()\n\n" +
                "ArrayList handles resizing for you!")
            .addTheory("ArrayList Syntax and Operations",
                "IMPORT:\n" +
                "import java.util.ArrayList;\n\n" +
                "CREATING:\n" +
                "ArrayList<String> names = new ArrayList<>();\n" +
                "// <String> = type parameter (ArrayList of Strings)\n\n" +
                "ADDING ELEMENTS:\n" +
                "names.add(\"Alice\");  // [\"Alice\"]\n" +
                "names.add(\"Bob\");    // [\"Alice\", \"Bob\"]\n" +
                "names.add(\"Carol\");  // [\"Alice\", \"Bob\", \"Carol\"]\n\n" +
                "ACCESSING:\n" +
                "names.get(0);  // \"Alice\" (like array[0])\n" +
                "names.get(1);  // \"Bob\"\n\n" +
                "SIZE:\n" +
                "names.size();  // 3 (NOT .length like arrays)\n\n" +
                "REMOVING:\n" +
                "names.remove(1);  // Remove \"Bob\"\n" +
                "// Now: [\"Alice\", \"Carol\"] - automatically shifts!\n\n" +
                "CHECKING:\n" +
                "names.contains(\"Alice\");  // true\n" +
                "names.isEmpty();  // false")
            .addTheory("ArrayList vs Array: Key Differences",
                "ARRAY:\n" +
                "int[] arr = new int[5];\n" +
                "- Fixed size: 5 elements forever\n" +
                "- Access: arr[0]\n" +
                "- Length: arr.length\n" +
                "- Can hold primitives (int, double, char)\n\n" +
                "ARRAYLIST:\n" +
                "ArrayList<Integer> list = new ArrayList<>();\n" +
                "- Dynamic size: grows/shrinks\n" +
                "- Access: list.get(0)\n" +
                "- Length: list.size()\n" +
                "- Only holds objects (Integer, String, not int)\n\n" +
                "AUTOBOXING (automatic conversion):\n" +
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);  // int 5 → Integer 5 (autoboxing)\n" +
                "int x = nums.get(0);  // Integer → int (auto-unboxing)")
            .addKeyPoint("Common ArrayList Methods",
                "ADDING:\n" +
                "- add(item) → adds to end\n" +
                "- add(index, item) → inserts at position\n\n" +
                "REMOVING:\n" +
                "- remove(index) → removes at position\n" +
                "- remove(object) → removes first occurrence\n" +
                "- clear() → removes all\n\n" +
                "ACCESSING:\n" +
                "- get(index) → retrieves element\n" +
                "- set(index, item) → replaces element\n\n" +
                "QUERYING:\n" +
                "- size() → number of elements\n" +
                "- isEmpty() → true if empty\n" +
                "- contains(item) → true if found\n" +
                "- indexOf(item) → position of item (-1 if not found)\n\n" +
                "LOOPING:\n" +
                "for (int i = 0; i < list.size(); i++) {\n" +
                "    System.out.println(list.get(i));\n" +
                "}\n\n" +
                "// Enhanced for loop (easier!)\n" +
                "for (String item : list) {\n" +
                "    System.out.println(item);\n" +
                "}")
            .addChallenge(createArrayListQuiz())
            .addChallenge(createToDoListChallenge())
            .addChallenge(createNumbersChallenge())
            .addChallenge(createSearchChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createArrayListQuiz() {
        return new Challenge.Builder("epoch-3-lesson-2-quiz", "ArrayList vs Array", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the main advantage of ArrayList over arrays?")
            .addMultipleChoiceOption("A) ArrayList is faster")
            .addMultipleChoiceOption("B) ArrayList can dynamically grow and shrink")
            .addMultipleChoiceOption("C) ArrayList uses less memory")
            .addMultipleChoiceOption("D) ArrayList can only hold primitives")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createToDoListChallenge() {
        return new Challenge.Builder("epoch-3-lesson-2-todo", "Simple To-Do List", ChallengeType.CODE_COMPLETION)
            .description("Create a ToDoList class with:\n" +
                "- ArrayList<String> field named tasks\n" +
                "- Constructor that initializes tasks\n" +
                "- addTask(String task) method\n" +
                "- getTaskCount() method returning number of tasks\n" +
                "- getTask(int index) method")
            .starterCode(
                "import java.util.ArrayList;\n\n" +
                "public class ToDoList {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "ToDoList list = new ToDoList();\nreturn list.getTaskCount();",
                "0",
                true
            ))
            .addTestCase(new TestCase(
                "ToDoList list = new ToDoList();\nlist.addTask(\"Buy milk\");\nreturn list.getTaskCount();",
                "1",
                true
            ))
            .addTestCase(new TestCase(
                "ToDoList list = new ToDoList();\nlist.addTask(\"Task 1\");\nlist.addTask(\"Task 2\");\nreturn list.getTask(1);",
                "Task 2",
                true
            ))
            .build();
    }

    private static Challenge createNumbersChallenge() {
        return new Challenge.Builder("epoch-3-lesson-2-numbers", "Sum of ArrayList", ChallengeType.CODE_COMPLETION)
            .description("Create a class with a method sumList(ArrayList<Integer> nums) that:\n" +
                "- Takes an ArrayList of integers\n" +
                "- Returns the sum of all numbers")
            .starterCode(
                "import java.util.ArrayList;\n\n" +
                "public class ListCalculator {\n" +
                "    public static int sumList(ArrayList<Integer> nums) {\n" +
                "        // Your code here\n" +
                "    }\n" +
                "}")
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\nnums.add(10);\nnums.add(15);\n" +
                "return ListCalculator.sumList(nums);",
                "30",
                true
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(1);\nnums.add(2);\nnums.add(3);\nnums.add(4);\n" +
                "return ListCalculator.sumList(nums);",
                "10",
                true
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "return ListCalculator.sumList(nums);",
                "0",
                true,
                "Empty list should return 0"
            ))
            .build();
    }

    private static Challenge createSearchChallenge() {
        return new Challenge.Builder("epoch-3-lesson-2-search", "Find and Remove", ChallengeType.FREE_CODING)
            .description("Create a class with method removeNegatives(ArrayList<Integer> nums) that:\n" +
                "- Takes an ArrayList of integers\n" +
                "- Removes all negative numbers\n" +
                "- Returns the modified list")
            .starterCode(
                "import java.util.ArrayList;\n\n" +
                "public class ListFilter {\n" +
                "    public static ArrayList<Integer> removeNegatives(ArrayList<Integer> nums) {\n" +
                "        // Your code here\n" +
                "    }\n" +
                "}")
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\nnums.add(-3);\nnums.add(10);\nnums.add(-1);\n" +
                "ArrayList<Integer> result = ListFilter.removeNegatives(nums);\n" +
                "return result.size();",
                "2",
                true,
                "Should have 2 elements remaining (5 and 10)"
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\nnums.add(-3);\nnums.add(10);\nnums.add(-1);\n" +
                "ArrayList<Integer> result = ListFilter.removeNegatives(nums);\n" +
                "return result.get(0) + result.get(1);",
                "15",
                false,
                "Should contain 5 and 10 (sum = 15)"
            ))
            .build();
    }
}
