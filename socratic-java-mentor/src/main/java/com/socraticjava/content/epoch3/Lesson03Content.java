package com.socraticjava.content.epoch3;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 3.3: HashMap - Key-Value Storage
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-3-lesson-3", "Lesson 3.3: HashMap - Looking Up by Name")
            .addTheory("The Problem: Finding Data by Index is Limiting",
                "With ArrayList, you access by INDEX:\n\n" +
                "ArrayList<String> students = new ArrayList<>();\n" +
                "students.add(\"Alice\");  // index 0\n" +
                "students.add(\"Bob\");    // index 1\n\n" +
                "To find Bob, you need to:\n" +
                "- Loop through entire list\n" +
                "- Check each name\n" +
                "- Remember which index was Bob\n\n" +
                "Real-world scenarios:\n" +
                "- Look up a phone number by NAME (not position)\n" +
                "- Find product price by SKU code\n" +
                "- Get user profile by username\n\n" +
                "You need: KEY → VALUE lookup\n\n" +
                "Solution: HashMap!")
            .addAnalogy("HashMap is Like a Dictionary",
                "ENGLISH DICTIONARY:\n" +
                "- Look up \"apple\" (KEY)\n" +
                "- Get definition: \"a round fruit...\" (VALUE)\n" +
                "- Don't need to know page number\n" +
                "- Direct lookup!\n\n" +
                "JAVA HASHMAP:\n" +
                "HashMap<String, String> phonebook = new HashMap<>();\n" +
                "phonebook.put(\"Alice\", \"555-1234\");  // KEY → VALUE\n" +
                "phonebook.put(\"Bob\", \"555-5678\");\n\n" +
                "String aliceNumber = phonebook.get(\"Alice\");  // \"555-1234\"\n\n" +
                "Fast lookup by KEY, not index!")
            .addTheory("HashMap Syntax and Operations",
                "IMPORT:\n" +
                "import java.util.HashMap;\n\n" +
                "CREATING:\n" +
                "HashMap<String, Integer> ages = new HashMap<>();\n" +
                "// <KeyType, ValueType>\n\n" +
                "ADDING/UPDATING:\n" +
                "ages.put(\"Alice\", 25);  // Add Alice → 25\n" +
                "ages.put(\"Bob\", 30);    // Add Bob → 30\n" +
                "ages.put(\"Alice\", 26);  // UPDATE Alice to 26 (overwrites)\n\n" +
                "GETTING VALUES:\n" +
                "ages.get(\"Alice\");  // 26\n" +
                "ages.get(\"Bob\");    // 30\n" +
                "ages.get(\"Carol\");  // null (not found)\n\n" +
                "CHECKING:\n" +
                "ages.containsKey(\"Alice\");  // true\n" +
                "ages.containsValue(30);  // true\n" +
                "ages.isEmpty();  // false\n\n" +
                "REMOVING:\n" +
                "ages.remove(\"Bob\");  // Remove Bob's entry\n\n" +
                "SIZE:\n" +
                "ages.size();  // 1 (only Alice remains)")
            .addTheory("Looping Through a HashMap",
                "You can iterate over KEYS, VALUES, or ENTRIES:\n\n" +
                "HashMap<String, Integer> scores = new HashMap<>();\n" +
                "scores.put(\"Alice\", 95);\n" +
                "scores.put(\"Bob\", 87);\n" +
                "scores.put(\"Carol\", 92);\n\n" +
                "// METHOD 1: Loop through keys\n" +
                "for (String name : scores.keySet()) {\n" +
                "    System.out.println(name + \": \" + scores.get(name));\n" +
                "}\n\n" +
                "// METHOD 2: Loop through entries (KEY-VALUE pairs)\n" +
                "for (Map.Entry<String, Integer> entry : scores.entrySet()) {\n" +
                "    System.out.println(entry.getKey() + \": \" + entry.getValue());\n" +
                "}\n\n" +
                "// METHOD 3: Loop through values only\n" +
                "for (Integer score : scores.values()) {\n" +
                "    System.out.println(score);\n" +
                "}\n\n" +
                "Note: HashMap does NOT maintain order (use LinkedHashMap for order)")
            .addKeyPoint("HashMap Performance and Use Cases",
                "PERFORMANCE:\n" +
                "- put(key, value): O(1) average - FAST!\n" +
                "- get(key): O(1) average - FAST!\n" +
                "- containsKey(key): O(1) average - FAST!\n\n" +
                "Compare to ArrayList search: O(n) - must check every element\n\n" +
                "WHEN TO USE HASHMAP:\n" +
                "✓ Need fast lookup by key\n" +
                "✓ Key-value associations (username → profile)\n" +
                "✓ Counting occurrences (word → count)\n" +
                "✓ Caching results (input → output)\n\n" +
                "WHEN NOT TO USE:\n" +
                "✗ Need to maintain order (use LinkedHashMap)\n" +
                "✗ Need sorting (use TreeMap)\n" +
                "✗ Just storing a list of items (use ArrayList)")
            .addWarning("HashMap Keys Must Be Unique",
                "HashMap<String, Integer> map = new HashMap<>();\n" +
                "map.put(\"Alice\", 25);\n" +
                "map.put(\"Bob\", 30);\n" +
                "map.put(\"Alice\", 26);  // OVERWRITES previous Alice value!\n\n" +
                "System.out.println(map.get(\"Alice\"));  // 26 (not 25)\n" +
                "System.out.println(map.size());  // 2 (not 3)\n\n" +
                "Each KEY can only appear ONCE.\n" +
                "If you put() with an existing key, it REPLACES the value.\n\n" +
                "But VALUES can be duplicated:\n" +
                "map.put(\"Carol\", 30);  // Same value as Bob - totally fine!")
            .addChallenge(createHashMapQuiz())
            .addChallenge(createPhonebookChallenge())
            .addChallenge(createInventoryChallenge())
            .addChallenge(createWordCountChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createHashMapQuiz() {
        return new Challenge.Builder("epoch-3-lesson-3-quiz", "Understanding HashMap", ChallengeType.MULTIPLE_CHOICE)
            .description("What happens if you put() a key that already exists in a HashMap?")
            .addMultipleChoiceOption("A) It throws an error")
            .addMultipleChoiceOption("B) It adds a second entry with the same key")
            .addMultipleChoiceOption("C) It replaces the old value with the new value")
            .addMultipleChoiceOption("D) It does nothing")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createPhonebookChallenge() {
        return new Challenge.Builder("epoch-3-lesson-3-phonebook", "Simple Phonebook", ChallengeType.CODE_COMPLETION)
            .description("Create a Phonebook class with:\n" +
                "- HashMap<String, String> field named contacts (name → phone)\n" +
                "- Constructor that initializes contacts\n" +
                "- addContact(String name, String phone) method\n" +
                "- getPhone(String name) method returning the phone number")
            .starterCode(
                "import java.util.HashMap;\n\n" +
                "public class Phonebook {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Phonebook pb = new Phonebook();\n" +
                "pb.addContact(\"Alice\", \"555-1234\");\n" +
                "return pb.getPhone(\"Alice\");",
                "555-1234",
                true
            ))
            .addTestCase(new TestCase(
                "Phonebook pb = new Phonebook();\n" +
                "pb.addContact(\"Bob\", \"555-5678\");\n" +
                "pb.addContact(\"Carol\", \"555-9999\");\n" +
                "return pb.getPhone(\"Carol\");",
                "555-9999",
                true
            ))
            .addTestCase(new TestCase(
                "Phonebook pb = new Phonebook();\n" +
                "pb.addContact(\"Dave\", \"555-0000\");\n" +
                "return pb.getPhone(\"NonExistent\");",
                "null",
                true,
                "Should return null for non-existent name"
            ))
            .build();
    }

    private static Challenge createInventoryChallenge() {
        return new Challenge.Builder("epoch-3-lesson-3-inventory", "Product Inventory", ChallengeType.CODE_COMPLETION)
            .description("Create an Inventory class with:\n" +
                "- HashMap<String, Integer> field named stock (product → quantity)\n" +
                "- Constructor that initializes stock\n" +
                "- addProduct(String product, int quantity) method\n" +
                "- getStock(String product) method returning quantity (or 0 if not found)\n" +
                "- hasProduct(String product) method returning boolean")
            .starterCode(
                "import java.util.HashMap;\n\n" +
                "public class Inventory {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Inventory inv = new Inventory();\n" +
                "inv.addProduct(\"Apple\", 50);\n" +
                "return inv.getStock(\"Apple\");",
                "50",
                true
            ))
            .addTestCase(new TestCase(
                "Inventory inv = new Inventory();\n" +
                "inv.addProduct(\"Orange\", 30);\n" +
                "return inv.hasProduct(\"Orange\");",
                "true",
                true
            ))
            .addTestCase(new TestCase(
                "Inventory inv = new Inventory();\n" +
                "return inv.hasProduct(\"Banana\");",
                "false",
                true
            ))
            .addTestCase(new TestCase(
                "Inventory inv = new Inventory();\n" +
                "return inv.getStock(\"NonExistent\");",
                "0",
                false,
                "Should return 0 for non-existent products"
            ))
            .build();
    }

    private static Challenge createWordCountChallenge() {
        return new Challenge.Builder("epoch-3-lesson-3-wordcount", "Word Frequency Counter", ChallengeType.FREE_CODING)
            .description("Create a class with method countWords(String[] words) that:\n" +
                "- Takes an array of words\n" +
                "- Returns HashMap<String, Integer> where key=word, value=count\n" +
                "Example: [\"apple\", \"banana\", \"apple\"] → {\"apple\": 2, \"banana\": 1}")
            .starterCode(
                "import java.util.HashMap;\n\n" +
                "public class WordCounter {\n" +
                "    public static HashMap<String, Integer> countWords(String[] words) {\n" +
                "        // Your code here\n" +
                "    }\n" +
                "}")
            .addTestCase(new TestCase(
                "String[] words = {\"apple\", \"banana\", \"apple\"};\n" +
                "HashMap<String, Integer> result = WordCounter.countWords(words);\n" +
                "return result.get(\"apple\");",
                "2",
                true
            ))
            .addTestCase(new TestCase(
                "String[] words = {\"apple\", \"banana\", \"apple\"};\n" +
                "HashMap<String, Integer> result = WordCounter.countWords(words);\n" +
                "return result.get(\"banana\");",
                "1",
                true
            ))
            .addTestCase(new TestCase(
                "String[] words = {\"cat\", \"dog\", \"cat\", \"bird\", \"dog\", \"cat\"};\n" +
                "HashMap<String, Integer> result = WordCounter.countWords(words);\n" +
                "return result.get(\"cat\");",
                "3",
                false
            ))
            .build();
    }
}
