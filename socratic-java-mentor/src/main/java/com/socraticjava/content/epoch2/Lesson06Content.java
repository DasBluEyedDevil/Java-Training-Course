package com.socraticjava.content.epoch2;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 2.6: Abstract Classes and Interfaces
 */
public class Lesson06Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-2-lesson-6", "Lesson 2.6: Abstract Classes & Interfaces - Enforcing Contracts")
            .addTheory("The Problem: Incomplete Parent Classes",
                "Remember our Animal example?\n\n" +
                "class Animal {\n" +
                "    public void makeSound() {\n" +
                "        System.out.println(\"Generic sound\");  // What IS a generic sound?\n" +
                "    }\n" +
                "}\n\n" +
                "Problems:\n" +
                "1. There's no such thing as a \"generic animal\" - it's too abstract\n" +
                "2. What if someone creates: Animal a = new Animal()?\n" +
                "3. What if a subclass FORGETS to override makeSound()?\n\n" +
                "We need a way to say:\n" +
                "- \"Animal is a concept, not a real thing\" (can't instantiate)\n" +
                "- \"Every subclass MUST implement makeSound()\" (enforce contract)\n\n" +
                "Solution: ABSTRACT CLASSES")
            .addTheory("Abstract Classes: Templates with Rules",
                "ABSTRACT CLASS = Class that CANNOT be instantiated\n" +
                "ABSTRACT METHOD = Method with NO implementation (body)\n\n" +
                "abstract class Animal {  // Can't do: new Animal()\n" +
                "    protected String name;\n" +
                "    \n" +
                "    public Animal(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "    \n" +
                "    // ABSTRACT METHOD - no body, must be overridden\n" +
                "    public abstract void makeSound();\n" +
                "    \n" +
                "    // CONCRETE METHOD - has implementation\n" +
                "    public void sleep() {\n" +
                "        System.out.println(name + \" is sleeping.\");\n" +
                "    }\n" +
                "}\n\n" +
                "class Dog extends Animal {\n" +
                "    public Dog(String name) {\n" +
                "        super(name);\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public void makeSound() {  // MUST implement this!\n" +
                "        System.out.println(\"Woof!\");\n" +
                "    }\n" +
                "}\n\n" +
                "// Animal a = new Animal(\"Generic\");  // COMPILE ERROR!\n" +
                "Dog d = new Dog(\"Buddy\");  // Works!\n" +
                "d.makeSound();  // \"Woof!\"\n" +
                "d.sleep();  // \"Buddy is sleeping.\" (inherited)")
            .addAnalogy("Abstract Class is Like a Cake Recipe Template",
                "ABSTRACT CLASS = Recipe template with mandatory steps:\n\n" +
                "Recipe Template:\n" +
                "1. Prepare batter (ABSTRACT - each cake type does it differently)\n" +
                "2. Pour into pan (CONCRETE - same for all cakes)\n" +
                "3. Bake (CONCRETE - same for all)\n" +
                "4. Decorate (ABSTRACT - each cake type unique)\n\n" +
                "You can't make a \"generic cake\" from the template.\n" +
                "You must make: Chocolate Cake, Vanilla Cake, etc.\n" +
                "Each MUST specify how to prepare batter and decorate.\n\n" +
                "JAVA:\n" +
                "abstract class Recipe {  // Can't instantiate\n" +
                "    abstract void prepareBatter();  // MUST implement\n" +
                "    void pourIntoPan() { ... }  // Inherited as-is\n" +
                "    void bake() { ... }  // Inherited as-is\n" +
                "    abstract void decorate();  // MUST implement\n" +
                "}\n\n" +
                "class ChocolateCake extends Recipe {\n" +
                "    void prepareBatter() { /* chocolate batter */ }\n" +
                "    void decorate() { /* chocolate frosting */ }\n" +
                "}")
            .addTheory("Interfaces: Pure Contracts",
                "INTERFACE = 100% abstract contract (no implementation)\n" +
                "Before Java 8, interfaces had ONLY abstract methods.\n\n" +
                "interface Flyable {\n" +
                "    void fly();  // public abstract (automatically)\n" +
                "    void land();\n" +
                "}\n\n" +
                "class Bird implements Flyable {\n" +
                "    @Override\n" +
                "    public void fly() {\n" +
                "        System.out.println(\"Flapping wings!\");\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public void land() {\n" +
                "        System.out.println(\"Landing on branch.\");\n" +
                "    }\n" +
                "}\n\n" +
                "class Airplane implements Flyable {\n" +
                "    @Override\n" +
                "    public void fly() {\n" +
                "        System.out.println(\"Jet engines roaring!\");\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public void land() {\n" +
                "        System.out.println(\"Landing on runway.\");\n" +
                "    }\n" +
                "}\n\n" +
                "KEY: Bird and Airplane are NOT related by inheritance,\n" +
                "but BOTH can be treated as Flyable!")
            .addTheory("Abstract Class vs Interface - When to Use What?",
                "ABSTRACT CLASS:\n" +
                "✓ IS-A relationship (Dog IS-A Animal)\n" +
                "✓ Share CODE between subclasses (common fields, methods)\n" +
                "✓ Has state (fields)\n" +
                "✓ Single inheritance only\n\n" +
                "Example:\n" +
                "abstract class Animal {  // Common state and behavior\n" +
                "    String name;\n" +
                "    abstract void makeSound();\n" +
                "    void sleep() { ... }  // Shared implementation\n" +
                "}\n\n" +
                "INTERFACE:\n" +
                "✓ CAN-DO relationship (Bird CAN fly, Dog CAN swim)\n" +
                "✓ Define BEHAVIOR contract, no implementation (pre-Java 8)\n" +
                "✓ No state (no fields, except constants)\n" +
                "✓ Multiple implementation allowed!\n\n" +
                "Example:\n" +
                "interface Flyable { void fly(); }\n" +
                "interface Swimmable { void swim(); }\n\n" +
                "class Duck implements Flyable, Swimmable {  // Multiple!\n" +
                "    public void fly() { ... }\n" +
                "    public void swim() { ... }\n" +
                "}")
            .addKeyPoint("Multiple Interfaces: Java's Alternative to Multiple Inheritance",
                "Java doesn't allow:\n" +
                "class Dog extends Animal, Robot { }  // ERROR!\n\n" +
                "But DOES allow:\n" +
                "class RobotDog extends Robot implements Walkable, Barkable { }\n\n" +
                "Real example:\n" +
                "interface Swimmable { void swim(); }\n" +
                "interface Flyable { void fly(); }\n" +
                "interface Walkable { void walk(); }\n\n" +
                "class Duck extends Animal implements Flyable, Swimmable, Walkable {\n" +
                "    public void fly() { System.out.println(\"Flying\"); }\n" +
                "    public void swim() { System.out.println(\"Swimming\"); }\n" +
                "    public void walk() { System.out.println(\"Walking\"); }\n" +
                "}\n\n" +
                "Now Duck can be used as:\n" +
                "- Animal (inheritance)\n" +
                "- Flyable (interface)\n" +
                "- Swimmable (interface)\n" +
                "- Walkable (interface)\n\n" +
                "This is POWERFUL for flexible design!")
            .addChallenge(createAbstractQuiz())
            .addChallenge(createShapeChallenge())
            .addChallenge(createPaymentChallenge())
            .addChallenge(createMultipleInterfaceChallenge())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createAbstractQuiz() {
        return new Challenge.Builder("epoch-2-lesson-6-quiz", "Abstract Class vs Interface", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the main difference between an abstract class and an interface?")
            .addMultipleChoiceOption("A) Abstract classes can't have methods")
            .addMultipleChoiceOption("B) Interfaces can't have any methods")
            .addMultipleChoiceOption("C) A class can extend one abstract class but implement multiple interfaces")
            .addMultipleChoiceOption("D) There is no difference")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createShapeChallenge() {
        return new Challenge.Builder("epoch-2-lesson-6-shape", "Abstract Shape Class", ChallengeType.CODE_COMPLETION)
            .description("Create:\n" +
                "1. Abstract class Shape with: String color field, constructor, abstract method getArea()\n" +
                "2. Circle class extending Shape with: double radius, constructor, getArea() implementation\n" +
                "   (area = π * r²)")
            .starterCode(
                "abstract class Shape {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Circle extends Shape {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Circle c = new Circle(\"red\", 5.0);\nreturn c.color;",
                "red",
                true
            ))
            .addTestCase(new TestCase(
                "Circle c = new Circle(\"blue\", 10.0);\nreturn Math.abs(c.getArea() - 314.159) < 1.0;",
                "true",
                true,
                "Circle area should be approximately π * 100"
            ))
            .addTestCase(new TestCase(
                "Circle c = new Circle(\"green\", 3.0);\nreturn c.getArea() > 28.0 && c.getArea() < 29.0;",
                "true",
                true
            ))
            .build();
    }

    private static Challenge createPaymentChallenge() {
        return new Challenge.Builder("epoch-2-lesson-6-payment", "Payment Interface", ChallengeType.CODE_COMPLETION)
            .description("Create:\n" +
                "1. Interface Payable with method: double calculatePayment()\n" +
                "2. Class Employee implementing Payable with: double salary field, constructor,\n" +
                "   calculatePayment() returning salary\n" +
                "3. Class Contractor implementing Payable with: double hourlyRate, int hoursWorked,\n" +
                "   constructor, calculatePayment() returning hourlyRate * hoursWorked")
            .starterCode(
                "interface Payable {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Employee implements Payable {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Contractor implements Payable {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Employee e = new Employee(5000.0);\nreturn e.calculatePayment();",
                "5000.0",
                true
            ))
            .addTestCase(new TestCase(
                "Contractor c = new Contractor(50.0, 40);\nreturn c.calculatePayment();",
                "2000.0",
                true
            ))
            .addTestCase(new TestCase(
                "Payable p = new Employee(3000.0);\nreturn p.calculatePayment();",
                "3000.0",
                true,
                "Polymorphism: Payable variable holding Employee"
            ))
            .build();
    }

    private static Challenge createMultipleInterfaceChallenge() {
        return new Challenge.Builder("epoch-2-lesson-6-multiple", "Multiple Interfaces", ChallengeType.FREE_CODING)
            .description("Create:\n" +
                "1. Interface Drivable with method: void drive()\n" +
                "2. Interface Refuelable with method: void refuel()\n" +
                "3. Class Car implementing BOTH interfaces\n" +
                "   - drive() should return \"Driving car\"\n" +
                "   - refuel() should return \"Refueling car\"")
            .starterCode(
                "interface Drivable {\n" +
                "    String drive();\n" +
                "}\n\n" +
                "interface Refuelable {\n" +
                "    String refuel();\n" +
                "}\n\n" +
                "class Car implements Drivable, Refuelable {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Car c = new Car();\nreturn c.drive();",
                "Driving car",
                true
            ))
            .addTestCase(new TestCase(
                "Car c = new Car();\nreturn c.refuel();",
                "Refueling car",
                true
            ))
            .addTestCase(new TestCase(
                "Drivable d = new Car();\nreturn d.drive();",
                "Driving car",
                true,
                "Polymorphism with Drivable interface"
            ))
            .addTestCase(new TestCase(
                "Refuelable r = new Car();\nreturn r.refuel();",
                "Refueling car",
                false,
                "Polymorphism with Refuelable interface"
            ))
            .build();
    }
}
