package com.socraticjava.content.epoch3;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 3.5: Common Collection Operations
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-3-lesson-5", "Lesson 3.5: Sorting, Searching, and Collection Utilities")
            .addTheory("The Problem: Collections Need Common Operations",
                "You've learned:\n" +
                "- ArrayList for dynamic lists\n" +
                "- HashMap for key-value pairs\n" +
                "- LinkedList for queue-like structures\n\n" +
                "But you often need to:\n" +
                "- SORT a list (alphabetically, numerically)\n" +
                "- SEARCH for an element\n" +
                "- SHUFFLE elements\n" +
                "- Find MAX/MIN values\n" +
                "- REVERSE order\n\n" +
                "Good news: Java provides Collections utility class with methods for all of this!")
            .addTheory("Collections.sort() - Sorting Lists",
                "IMPORT:\n" +
                "import java.util.Collections;\n\n" +
                "SORTING NUMBERS:\n" +
                "ArrayList<Integer> numbers = new ArrayList<>();\n" +
                "numbers.add(5);\n" +
                "numbers.add(2);\n" +
                "numbers.add(8);\n" +
                "numbers.add(1);\n\n" +
                "Collections.sort(numbers);  // Sorts IN PLACE\n" +
                "// Now: [1, 2, 5, 8]\n\n" +
                "SORTING STRINGS:\n" +
                "ArrayList<String> names = new ArrayList<>();\n" +
                "names.add(\"Charlie\");\n" +
                "names.add(\"Alice\");\n" +
                "names.add(\"Bob\");\n\n" +
                "Collections.sort(names);  // Alphabetical\n" +
                "// Now: [\"Alice\", \"Bob\", \"Charlie\"]\n\n" +
                "REVERSE SORT:\n" +
                "Collections.sort(numbers, Collections.reverseOrder());\n" +
                "// Now: [8, 5, 2, 1]")
            .addTheory("Other Useful Collections Methods",
                "REVERSE:\n" +
                "Collections.reverse(list);  // Reverse the order\n\n" +
                "SHUFFLE:\n" +
                "Collections.shuffle(list);  // Randomize order\n\n" +
                "MAX AND MIN:\n" +
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\n" +
                "nums.add(2);\n" +
                "nums.add(9);\n" +
                "int max = Collections.max(nums);  // 9\n" +
                "int min = Collections.min(nums);  // 2\n\n" +
                "FREQUENCY:\n" +
                "ArrayList<String> items = new ArrayList<>();\n" +
                "items.add(\"apple\");\n" +
                "items.add(\"banana\");\n" +
                "items.add(\"apple\");\n" +
                "int count = Collections.frequency(items, \"apple\");  // 2\n\n" +
                "FILL:\n" +
                "Collections.fill(list, \"X\");  // Replace all with \"X\"")
            .addTheory("Enhanced For Loop (for-each)",
                "The EASIEST way to iterate through collections:\n\n" +
                "ArrayList<String> fruits = new ArrayList<>();\n" +
                "fruits.add(\"Apple\");\n" +
                "fruits.add(\"Banana\");\n" +
                "fruits.add(\"Cherry\");\n\n" +
                "// Traditional for loop\n" +
                "for (int i = 0; i < fruits.size(); i++) {\n" +
                "    System.out.println(fruits.get(i));\n" +
                "}\n\n" +
                "// Enhanced for loop (for-each)\n" +
                "for (String fruit : fruits) {  // Read as: \"for each fruit in fruits\"\n" +
                "    System.out.println(fruit);\n" +
                "}\n\n" +
                "Benefits:\n" +
                "- Cleaner syntax\n" +
                "- No index errors\n" +
                "- Works with ANY collection\n\n" +
                "Works with HashMap too:\n" +
                "HashMap<String, Integer> ages = new HashMap<>();\n" +
                "for (String key : ages.keySet()) {\n" +
                "    System.out.println(key + \": \" + ages.get(key));\n" +
                "}")
            .addKeyPoint("The Collection Interface Hierarchy",
                "All collections implement common interfaces:\n\n" +
                "Collection (interface)\n" +
                "  |\n" +
                "  ├─ List (interface)\n" +
                "  │   ├─ ArrayList (class)\n" +
                "  │   └─ LinkedList (class)\n" +
                "  │\n" +
                "  ├─ Set (interface) - No duplicates\n" +
                "  │   ├─ HashSet (class)\n" +
                "  │   └─ TreeSet (class) - Sorted\n" +
                "  │\n" +
                "  └─ Queue (interface)\n" +
                "      └─ LinkedList (class)\n\n" +
                "Map (separate hierarchy)\n" +
                "  ├─ HashMap (class)\n" +
                "  ├─ LinkedHashMap (maintains order)\n" +
                "  └─ TreeMap (sorted by key)\n\n" +
                "Common methods ALL collections share:\n" +
                "- add(element)\n" +
                "- remove(element)\n" +
                "- size()\n" +
                "- isEmpty()\n" +
                "- clear()\n" +
                "- contains(element)")
            .addExample("Real-World Example: Leaderboard",
                "Building a game leaderboard:\n\n" +
                "import java.util.*;\n\n" +
                "class Player {\n" +
                "    String name;\n" +
                "    int score;\n" +
                "    \n" +
                "    Player(String name, int score) {\n" +
                "        this.name = name;\n" +
                "        this.score = score;\n" +
                "    }\n" +
                "}\n\n" +
                "ArrayList<Player> players = new ArrayList<>();\n" +
                "players.add(new Player(\"Alice\", 500));\n" +
                "players.add(new Player(\"Bob\", 750));\n" +
                "players.add(new Player(\"Carol\", 300));\n\n" +
                "// Sort by score (highest first)\n" +
                "Collections.sort(players, (p1, p2) -> p2.score - p1.score);\n\n" +
                "// Display leaderboard\n" +
                "for (int i = 0; i < players.size(); i++) {\n" +
                "    Player p = players.get(i);\n" +
                "    System.out.println((i+1) + \". \" + p.name + \": \" + p.score);\n" +
                "}\n\n" +
                "Output:\n" +
                "1. Bob: 750\n" +
                "2. Alice: 500\n" +
                "3. Carol: 300")
            .addChallenge(createSortQuiz())
            .addChallenge(createSortChallenge())
            .addChallenge(createMaxMinChallenge())
            .addChallenge(createTopScoresChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createSortQuiz() {
        return new Challenge.Builder("epoch-3-lesson-5-quiz", "Understanding Sorting", ChallengeType.MULTIPLE_CHOICE)
            .description("What does Collections.sort() do to the original list?")
            .addMultipleChoiceOption("A) Creates a new sorted list, leaves original unchanged")
            .addMultipleChoiceOption("B) Sorts the original list in place")
            .addMultipleChoiceOption("C) Returns a sorted copy without changing the original")
            .addMultipleChoiceOption("D) Throws an error")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSortChallenge() {
        return new Challenge.Builder("epoch-3-lesson-5-sort", "Sort Numbers", ChallengeType.CODE_COMPLETION)
            .description("Create a class with method sortNumbers(ArrayList<Integer> nums) that:\n" +
                "- Sorts the list in ascending order\n" +
                "- Returns the sorted list\n" +
                "Use Collections.sort()")
            .starterCode(
                "import java.util.ArrayList;\n" +
                "import java.util.Collections;\n\n" +
                "public class Sorter {\n" +
                "    public static ArrayList<Integer> sortNumbers(ArrayList<Integer> nums) {\n" +
                "        // Your code here\n" +
                "    }\n" +
                "}")
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\nnums.add(2);\nnums.add(8);\nnums.add(1);\n" +
                "ArrayList<Integer> sorted = Sorter.sortNumbers(nums);\n" +
                "return sorted.get(0);",
                "1",
                true,
                "First element should be 1"
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\nnums.add(2);\nnums.add(8);\nnums.add(1);\n" +
                "ArrayList<Integer> sorted = Sorter.sortNumbers(nums);\n" +
                "return sorted.get(3);",
                "8",
                true,
                "Last element should be 8"
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(100);\nnums.add(50);\nnums.add(75);\n" +
                "ArrayList<Integer> sorted = Sorter.sortNumbers(nums);\n" +
                "return sorted.get(1);",
                "75",
                true
            ))
            .build();
    }

    private static Challenge createMaxMinChallenge() {
        return new Challenge.Builder("epoch-3-lesson-5-maxmin", "Find Max and Min", ChallengeType.CODE_COMPLETION)
            .description("Create a class with method getRange(ArrayList<Integer> nums) that:\n" +
                "- Returns the difference between max and min values\n" +
                "Use Collections.max() and Collections.min()")
            .starterCode(
                "import java.util.ArrayList;\n" +
                "import java.util.Collections;\n\n" +
                "public class RangeFinder {\n" +
                "    public static int getRange(ArrayList<Integer> nums) {\n" +
                "        // Your code here\n" +
                "    }\n" +
                "}")
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(5);\nnums.add(10);\nnums.add(2);\nnums.add(8);\n" +
                "return RangeFinder.getRange(nums);",
                "8",
                true,
                "Range = 10 - 2 = 8"
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> nums = new ArrayList<>();\n" +
                "nums.add(100);\nnums.add(50);\nnums.add(75);\n" +
                "return RangeFinder.getRange(nums);",
                "50",
                true,
                "Range = 100 - 50 = 50"
            ))
            .build();
    }

    private static Challenge createTopScoresChallenge() {
        return new Challenge.Builder("epoch-3-lesson-5-top", "Get Top Scores", ChallengeType.FREE_CODING)
            .description("Create a class with method getTopThree(ArrayList<Integer> scores) that:\n" +
                "- Returns a new ArrayList with the top 3 highest scores in descending order\n" +
                "- If less than 3 scores, return all of them sorted\n" +
                "Hint: Sort in reverse order and take first 3")
            .starterCode(
                "import java.util.ArrayList;\n" +
                "import java.util.Collections;\n\n" +
                "public class TopScores {\n" +
                "    public static ArrayList<Integer> getTopThree(ArrayList<Integer> scores) {\n" +
                "        // Your code here\n" +
                "    }\n" +
                "}")
            .addTestCase(new TestCase(
                "ArrayList<Integer> scores = new ArrayList<>();\n" +
                "scores.add(85);\nscores.add(92);\nscores.add(78);\n" +
                "scores.add(95);\nscores.add(88);\n" +
                "ArrayList<Integer> top = TopScores.getTopThree(scores);\n" +
                "return top.size();",
                "3",
                true,
                "Should return 3 elements"
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> scores = new ArrayList<>();\n" +
                "scores.add(85);\nscores.add(92);\nscores.add(78);\n" +
                "scores.add(95);\nscores.add(88);\n" +
                "ArrayList<Integer> top = TopScores.getTopThree(scores);\n" +
                "return top.get(0);",
                "95",
                true,
                "First should be highest: 95"
            ))
            .addTestCase(new TestCase(
                "ArrayList<Integer> scores = new ArrayList<>();\n" +
                "scores.add(85);\nscores.add(92);\nscores.add(78);\n" +
                "scores.add(95);\nscores.add(88);\n" +
                "ArrayList<Integer> top = TopScores.getTopThree(scores);\n" +
                "return top.get(2);",
                "88",
                false,
                "Third should be 88"
            ))
            .build();
    }
}
