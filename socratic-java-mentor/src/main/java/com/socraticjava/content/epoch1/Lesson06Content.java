package com.socraticjava.content.epoch1;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 1.6: Understanding Method Keywords (public, private, static, void)
 */
public class Lesson06Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-1-lesson-6", "Lesson 1.6: What Do 'public', 'static', and 'void' Mean?")
            .addTheory("The Mystery Keywords You've Been Ignoring",
                "You've been writing:\n\n" +
                "public static void main(String[] args)\n" +
                "public static int add(int a, int b)\n\n" +
                "And I told you: \"Just write 'public static' for now.\"\n\n" +
                "But what do these keywords ACTUALLY mean?\n" +
                "Why do we write them?\n" +
                "When do you use 'public' vs 'private'?\n" +
                "What's the difference between 'void' and 'int'?\n\n" +
                "Time to demystify these CRUCIAL keywords!")
            .addTheory("Part 1: Understanding 'void' vs Return Types",
                "'void' is NOT a type like 'int' or 'String'. It means \"returns NOTHING\".\n\n" +
                "VOID METHOD (performs action, returns nothing):\n\n" +
                "public static void sayHello() {\n" +
                "    System.out.println(\"Hello!\");\n" +
                "    // NO return statement\n" +
                "}\n\n" +
                "sayHello();  // Just does something, doesn't give back a value\n\n" +
                "RETURNING METHOD (calculates and returns value):\n\n" +
                "public static int add(int a, int b) {\n" +
                "    return a + b;  // MUST return an int\n" +
                "}\n\n" +
                "int result = add(5, 3);  // result gets the value 8\n\n" +
                "KEY DIFFERENCE:\n" +
                "- void = \"do something\" (print, save, modify)\n" +
                "- int/String/double/etc = \"calculate and give me back a value\"\n\n" +
                "Think of it like asking someone to do something:\n" +
                "- \"Clean your room!\" (void - just do it, no result)\n" +
                "- \"What's 2 + 2?\" (returns int - I need an answer back)")
            .addTheory("Part 2: Understanding 'public' vs 'private'",
                "'public' and 'private' control WHO can access your method.\n\n" +
                "PUBLIC = Everyone can use it:\n\n" +
                "public static int add(int a, int b) {\n" +
                "    return a + b;\n" +
                "}\n\n" +
                "// Any code, anywhere can call this:\n" +
                "int x = MathUtils.add(5, 3);  // Works!\n\n" +
                "PRIVATE = Only THIS class can use it:\n\n" +
                "private static int secretCalculation(int x) {\n" +
                "    return x * 2 + 10;  // Internal helper method\n" +
                "}\n\n" +
                "// Other classes CAN'T call this:\n" +
                "int y = MathUtils.secretCalculation(5);  // COMPILE ERROR!\n\n" +
                "WHY USE PRIVATE?\n" +
                "- Hide internal implementation details\n" +
                "- Prevent others from depending on internal code\n" +
                "- Make it easier to change later\n\n" +
                "RULE OF THUMB:\n" +
                "- Methods you want others to use: public\n" +
                "- Helper methods just for your class: private")
            .addAnalogy("public vs private: Restaurant Kitchen Analogy",
                "Imagine a restaurant:\n\n" +
                "PUBLIC methods = Menu items (customer-facing):\n" +
                "- orderPizza() ✓ Customer can call this\n" +
                "- orderSalad() ✓ Customer can call this\n" +
                "These are the INTERFACE the public uses.\n\n" +
                "PRIVATE methods = Kitchen processes (internal):\n" +
                "- chopVegetables() ✗ Customer can't call this\n" +
                "- preheartOven() ✗ Customer can't call this\n" +
                "These are IMPLEMENTATION details customers shouldn't worry about.\n\n" +
                "In code:\n\n" +
                "public class Restaurant {\n" +
                "    // PUBLIC - customers can call\n" +
                "    public void orderPizza(String type) {\n" +
                "        prepareOven();        // Call private helper\n" +
                "        makeDough();          // Call private helper\n" +
                "        addToppings(type);    // Call private helper\n" +
                "    }\n" +
                "    \n" +
                "    // PRIVATE - internal methods\n" +
                "    private void prepareOven() { ... }\n" +
                "    private void makeDough() { ... }\n" +
                "    private void addToppings(String type) { ... }\n" +
                "}")
            .addTheory("Part 3: Understanding 'static'",
                "'static' means the method belongs to the CLASS, not to individual objects.\n\n" +
                "STATIC METHOD (class-level, no object needed):\n\n" +
                "public static int add(int a, int b) {\n" +
                "    return a + b;\n" +
                "}\n\n" +
                "// Call directly on class:\n" +
                "int result = MathUtils.add(5, 3);  // No object needed!\n\n" +
                "NON-STATIC METHOD (instance-level, needs an object):\n\n" +
                "public class Dog {\n" +
                "    String name;\n" +
                "    \n" +
                "    public void bark() {  // NO 'static'\n" +
                "        System.out.println(name + \" says Woof!\");\n" +
                "    }\n" +
                "}\n\n" +
                "// Must create object first:\n" +
                "Dog myDog = new Dog();\n" +
                "myDog.name = \"Buddy\";\n" +
                "myDog.bark();  // \"Buddy says Woof!\"\n\n" +
                "WHEN TO USE STATIC:\n" +
                "✓ Utility methods (Math.sqrt, Integer.parseInt)\n" +
                "✓ Methods that don't need object data\n" +
                "✓ main() method (entry point)\n\n" +
                "WHEN TO USE NON-STATIC:\n" +
                "✓ Methods that work with object data\n" +
                "✓ Behavior specific to an instance\n" +
                "✓ Most methods in classes you create")
            .addAnalogy("static: Shared vs Personal",
                "STATIC = Shared tool (belongs to everyone):\n\n" +
                "Think of a PUBLIC calculator at school:\n" +
                "- Anyone can use it\n" +
                "- Doesn't belong to any one student\n" +
                "- Same calculator for all\n\n" +
                "Math.abs(-5)  // Use the shared Math class\n\n" +
                "NON-STATIC = Personal item (belongs to an instance):\n\n" +
                "Think of YOUR phone:\n" +
                "- Has YOUR contacts\n" +
                "- Stores YOUR photos\n" +
                "- Different from others' phones\n\n" +
                "myPhone.makeCall(\"Mom\")  // Your phone, your mom's number\n\n" +
                "In Java:\n\n" +
                "// STATIC - shared, no object needed\n" +
                "public static int add(int a, int b) {\n" +
                "    return a + b;  // Same for everyone\n" +
                "}\n" +
                "Calculator.add(2, 3);  // Just call it\n\n" +
                "// NON-STATIC - instance-specific\n" +
                "public class BankAccount {\n" +
                "    double balance;  // EACH account has its own balance\n" +
                "    \n" +
                "    public void deposit(double amount) {\n" +
                "        balance += amount;  // Modifies THIS account\n" +
                "    }\n" +
                "}\n" +
                "BankAccount aliceAccount = new BankAccount();\n" +
                "aliceAccount.deposit(100);  // Alice's balance changes")
            .addKeyPoint("Putting It All Together",
                "public static void main(String[] args)\n" +
                "  |      |     |    |     └─ Parameter: array of Strings\n" +
                "  |      |     |    └─ Name: main (special name, program entry)\n" +
                "  |      |     └─ Return type: void (doesn't return anything)\n" +
                "  |      └─ static: Can be called without creating an object\n" +
                "  └─ public: Must be accessible from anywhere (JVM needs to call it)\n\n" +
                "public static int add(int a, int b)\n" +
                "  |      |     |   |   └─ Parameters\n" +
                "  |      |     |   └─ Name\n" +
                "  |      |     └─ Return type: int (returns an integer)\n" +
                "  |      └─ static: Utility method, no object needed\n" +
                "  └─ public: Other classes can use this\n\n" +
                "private void updateDisplay()\n" +
                "  |      |    └─ Name\n" +
                "  |      └─ Return type: void (performs action, no return)\n" +
                "  └─ private: Only THIS class can call this\n\n" +
                "QUICK REFERENCE:\n" +
                "- void: Returns nothing\n" +
                "- int/String/etc: Returns that type\n" +
                "- public: Everyone can access\n" +
                "- private: Only this class can access\n" +
                "- static: Class-level, no object needed\n" +
                "- (no static): Instance-level, needs object")
            .addChallenge(createVoidQuiz())
            .addChallenge(createPublicPrivateQuiz())
            .addChallenge(createStaticQuiz())
            .addChallenge(createCombinedQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createVoidQuiz() {
        return new Challenge.Builder("epoch-1-lesson-6-void", "Understanding void", ChallengeType.MULTIPLE_CHOICE)
            .description("Which method would you use 'void' for?")
            .addMultipleChoiceOption("A) A method that calculates the sum of two numbers")
            .addMultipleChoiceOption("B) A method that prints a welcome message")
            .addMultipleChoiceOption("C) A method that returns the square of a number")
            .addMultipleChoiceOption("D) A method that converts temperature to Fahrenheit")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createPublicPrivateQuiz() {
        return new Challenge.Builder("epoch-1-lesson-6-access", "public vs private", ChallengeType.MULTIPLE_CHOICE)
            .description("When should you make a method private?")
            .addMultipleChoiceOption("A) When you want other classes to use it")
            .addMultipleChoiceOption("B) When it's a helper method only used inside your class")
            .addMultipleChoiceOption("C) When it's the main method")
            .addMultipleChoiceOption("D) Private methods don't work")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createStaticQuiz() {
        return new Challenge.Builder("epoch-1-lesson-6-static", "Understanding static", ChallengeType.MULTIPLE_CHOICE)
            .description("Which statement is TRUE about static methods?")
            .addMultipleChoiceOption("A) Static methods require creating an object first")
            .addMultipleChoiceOption("B) Static methods belong to the class, not individual objects")
            .addMultipleChoiceOption("C) You can never use static methods")
            .addMultipleChoiceOption("D) Static methods are slower than non-static")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createCombinedQuiz() {
        return new Challenge.Builder("epoch-1-lesson-6-combined", "Choosing the Right Signature", ChallengeType.MULTIPLE_CHOICE)
            .description("You need a utility method to check if a number is even. Anyone should be able to use it.\n" +
                "It takes an int and returns true/false.\n" +
                "No object is needed.\n\n" +
                "Which method signature is correct?")
            .addMultipleChoiceOption("A) private void isEven(int num)")
            .addMultipleChoiceOption("B) public static boolean isEven(int num)")
            .addMultipleChoiceOption("C) public int isEven()")
            .addMultipleChoiceOption("D) private static void isEven(boolean num)")
            .correctAnswer("B")
            .build();
    }
}
