package com.socraticjava.content.epoch3;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 3.4: LinkedList and Collection Performance
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-3-lesson-4", "Lesson 3.4: LinkedList - A Different Kind of List")
            .addTheory("The Problem: ArrayList Insertion is Slow",
                "ArrayList stores elements in a continuous block:\n\n" +
                "[0] [1] [2] [3] [4]\n\n" +
                "What happens when you INSERT in the middle?\n\n" +
                "list.add(2, \"NEW\");  // Insert at index 2\n\n" +
                "ArrayList must:\n" +
                "1. Shift element at index 2 to index 3\n" +
                "2. Shift element at index 3 to index 4\n" +
                "3. Shift element at index 4 to index 5\n" +
                "4. THEN insert \"NEW\" at index 2\n\n" +
                "For 1 million elements, this is SLOW! O(n) time\n\n" +
                "LinkedList solves this differently!")
            .addAnalogy("LinkedList is Like a Chain of People Holding Hands",
                "ARRAYLIST:\n" +
                "= People standing in numbered spots\n" +
                "[Person 0] [Person 1] [Person 2] [Person 3]\n" +
                "- To add someone in middle: everyone shifts spots\n" +
                "- Access by position: very fast (just go to spot 2)\n\n" +
                "LINKEDLIST:\n" +
                "= Chain of people holding hands\n" +
                "(Alice) → (Bob) → (Carol) → (Dave)\n" +
                "- Each person holds hand of next person\n" +
                "- To insert: break one handhold, insert new person, reconnect\n" +
                "- Access by position: must walk from start (slower)\n\n" +
                "LinkedList: Fast insertion, slower access\n" +
                "ArrayList: Slower insertion, fast access")
            .addTheory("LinkedList Syntax",
                "IMPORT:\n" +
                "import java.util.LinkedList;\n\n" +
                "CREATING:\n" +
                "LinkedList<String> names = new LinkedList<>();\n\n" +
                "LinkedList has ALL ArrayList methods:\n" +
                "names.add(\"Alice\");\n" +
                "names.add(\"Bob\");\n" +
                "names.get(0);  // \"Alice\"\n" +
                "names.remove(1);  // Remove Bob\n\n" +
                "PLUS special methods for ends:\n" +
                "names.addFirst(\"First\");  // Add to beginning\n" +
                "names.addLast(\"Last\");    // Add to end\n" +
                "names.getFirst();  // Get first element\n" +
                "names.getLast();   // Get last element\n" +
                "names.removeFirst();  // Remove first\n" +
                "names.removeLast();   // Remove last\n\n" +
                "These operations are O(1) - VERY FAST!")
            .addTheory("ArrayList vs LinkedList: Performance Comparison",
                "OPERATION          | ArrayList | LinkedList\n" +
                "-------------------|-----------|-----------\n" +
                "get(index)         | O(1) ✓    | O(n) ✗\n" +
                "add(element)       | O(1) ✓    | O(1) ✓\n" +
                "add(index, elem)   | O(n) ✗    | O(n) ~\n" +
                "addFirst(elem)     | O(n) ✗    | O(1) ✓\n" +
                "addLast(elem)      | O(1) ✓    | O(1) ✓\n" +
                "remove(index)      | O(n) ✗    | O(n) ~\n" +
                "removeFirst()      | O(n) ✗    | O(1) ✓\n" +
                "removeLast()       | O(1) ✓    | O(1) ✓\n\n" +
                "O(1) = Constant time (FAST)\n" +
                "O(n) = Linear time (scales with size)\n\n" +
                "SUMMARY:\n" +
                "ArrayList: Fast random access, slow insertions\n" +
                "LinkedList: Slow random access, fast insertions at ends")
            .addKeyPoint("When to Use Each Collection",
                "USE ARRAYLIST WHEN:\n" +
                "✓ You access elements by index frequently\n" +
                "✓ You mostly add to the end\n" +
                "✓ You iterate through all elements\n" +
                "✓ MOST COMMON USE CASE (default choice)\n\n" +
                "Example: Displaying a list of products\n\n" +
                "USE LINKEDLIST WHEN:\n" +
                "✓ You frequently insert/remove at the beginning\n" +
                "✓ You implement a Queue (FIFO)\n" +
                "✓ You implement a Stack (LIFO)\n" +
                "✓ You rarely access by index\n\n" +
                "Example: Task queue where tasks are added/removed from front\n\n" +
                "USE HASHMAP WHEN:\n" +
                "✓ You need key-value pairs\n" +
                "✓ You need fast lookup by key\n" +
                "✓ Order doesn't matter\n\n" +
                "Example: User profiles by username\n\n" +
                "RULE OF THUMB: Start with ArrayList unless you have a reason not to!")
            .addExample("Queue Example with LinkedList",
                "A QUEUE is FIFO (First In, First Out) like a line at a store:\n\n" +
                "LinkedList<String> queue = new LinkedList<>();\n\n" +
                "// People join the line (add to end)\n" +
                "queue.addLast(\"Alice\");\n" +
                "queue.addLast(\"Bob\");\n" +
                "queue.addLast(\"Carol\");\n\n" +
                "// Serve customers (remove from front)\n" +
                "String first = queue.removeFirst();  // \"Alice\"\n" +
                "String second = queue.removeFirst(); // \"Bob\"\n\n" +
                "// Who's next?\n" +
                "String next = queue.getFirst();  // \"Carol\" (peek without removing)\n\n" +
                "LinkedList is PERFECT for queues because:\n" +
                "- addLast() is O(1)\n" +
                "- removeFirst() is O(1)\n" +
                "Both operations are FAST!")
            .addChallenge(createLinkedListQuiz())
            .addChallenge(createQueueChallenge())
            .addChallenge(createPerformanceQuiz())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createLinkedListQuiz() {
        return new Challenge.Builder("epoch-3-lesson-4-quiz", "LinkedList vs ArrayList", ChallengeType.MULTIPLE_CHOICE)
            .description("When is LinkedList better than ArrayList?")
            .addMultipleChoiceOption("A) When you need to access elements by index frequently")
            .addMultipleChoiceOption("B) When you need to insert/remove elements at the beginning frequently")
            .addMultipleChoiceOption("C) LinkedList is always better")
            .addMultipleChoiceOption("D) When you need to store key-value pairs")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createQueueChallenge() {
        return new Challenge.Builder("epoch-3-lesson-4-queue", "Simple Task Queue", ChallengeType.CODE_COMPLETION)
            .description("Create a TaskQueue class with:\n" +
                "- LinkedList<String> field named tasks\n" +
                "- Constructor that initializes tasks\n" +
                "- enqueue(String task) method (add to end)\n" +
                "- dequeue() method (remove and return first task, or null if empty)\n" +
                "- size() method")
            .starterCode(
                "import java.util.LinkedList;\n\n" +
                "public class TaskQueue {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "TaskQueue q = new TaskQueue();\n" +
                "q.enqueue(\"Task 1\");\n" +
                "q.enqueue(\"Task 2\");\n" +
                "return q.size();",
                "2",
                true
            ))
            .addTestCase(new TestCase(
                "TaskQueue q = new TaskQueue();\n" +
                "q.enqueue(\"First\");\n" +
                "q.enqueue(\"Second\");\n" +
                "return q.dequeue();",
                "First",
                true,
                "Dequeue should return first-in element"
            ))
            .addTestCase(new TestCase(
                "TaskQueue q = new TaskQueue();\n" +
                "q.enqueue(\"A\");\n" +
                "q.enqueue(\"B\");\n" +
                "q.dequeue();\n" +
                "return q.dequeue();",
                "B",
                true,
                "Second dequeue should return second element"
            ))
            .addTestCase(new TestCase(
                "TaskQueue q = new TaskQueue();\n" +
                "return q.dequeue();",
                "null",
                false,
                "Empty queue should return null"
            ))
            .build();
    }

    private static Challenge createPerformanceQuiz() {
        return new Challenge.Builder("epoch-3-lesson-4-performance", "Collection Choice", ChallengeType.MULTIPLE_CHOICE)
            .description("You're building a music player that needs to:\n" +
                "- Store a playlist of songs\n" +
                "- Jump to any song by position\n" +
                "- Rarely insert in the middle\n\n" +
                "Which collection is best?")
            .addMultipleChoiceOption("A) LinkedList")
            .addMultipleChoiceOption("B) ArrayList")
            .addMultipleChoiceOption("C) HashMap")
            .addMultipleChoiceOption("D) Array")
            .correctAnswer("B")
            .build();
    }
}
