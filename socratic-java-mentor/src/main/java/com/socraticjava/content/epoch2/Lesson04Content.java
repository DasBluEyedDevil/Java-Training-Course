package com.socraticjava.content.epoch2;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 2.4: Inheritance
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-2-lesson-4", "Lesson 2.4: Inheritance - Building on Existing Classes")
            .addTheory("The Problem: Duplicate Code Everywhere",
                "Imagine you need these classes:\n\n" +
                "class Dog {\n" +
                "    String name;\n" +
                "    int age;\n" +
                "    void eat() { System.out.println(\"Eating...\"); }\n" +
                "    void sleep() { System.out.println(\"Sleeping...\"); }\n" +
                "    void bark() { System.out.println(\"Woof!\"); }\n" +
                "}\n\n" +
                "class Cat {\n" +
                "    String name;  // DUPLICATE!\n" +
                "    int age;  // DUPLICATE!\n" +
                "    void eat() { System.out.println(\"Eating...\"); }  // DUPLICATE!\n" +
                "    void sleep() { System.out.println(\"Sleeping...\"); }  // DUPLICATE!\n" +
                "    void meow() { System.out.println(\"Meow!\"); }\n" +
                "}\n\n" +
                "This is HORRIBLE:\n" +
                "- Repeated code (name, age, eat, sleep)\n" +
                "- If you change eat(), must change in BOTH places\n" +
                "- Hard to maintain\n\n" +
                "Solution: INHERITANCE - share common code!")
            .addAnalogy("Inheritance is Like Family Traits",
                "BIOLOGICAL INHERITANCE:\n" +
                "- Your parents have: eyes, hair, height genes\n" +
                "- YOU inherit: eyes, hair, height from them\n" +
                "- You also have: your own unique features\n\n" +
                "JAVA INHERITANCE:\n" +
                "class Animal {  // PARENT (superclass)\n" +
                "    String name;\n" +
                "    void eat() { ... }\n" +
                "    void sleep() { ... }\n" +
                "}\n\n" +
                "class Dog extends Animal {  // CHILD (subclass)\n" +
                "    // Automatically has: name, eat(), sleep()\n" +
                "    void bark() { ... }  // PLUS its own method\n" +
                "}\n\n" +
                "Dog d = new Dog();\n" +
                "d.eat();  // From Animal\n" +
                "d.sleep();  // From Animal\n" +
                "d.bark();  // Dog's own method")
            .addTheory("The 'extends' Keyword",
                "SYNTAX:\n" +
                "class ChildClass extends ParentClass { ... }\n\n" +
                "EXAMPLE:\n" +
                "class Animal {\n" +
                "    protected String name;  // 'protected' = accessible to subclasses\n" +
                "    protected int age;\n" +
                "    \n" +
                "    public Animal(String name, int age) {\n" +
                "        this.name = name;\n" +
                "        this.age = age;\n" +
                "    }\n" +
                "    \n" +
                "    public void eat() {\n" +
                "        System.out.println(name + \" is eating.\");\n" +
                "    }\n" +
                "}\n\n" +
                "class Dog extends Animal {\n" +
                "    public Dog(String name, int age) {\n" +
                "        super(name, age);  // Call parent constructor\n" +
                "    }\n" +
                "    \n" +
                "    public void bark() {\n" +
                "        System.out.println(name + \" says Woof!\");  // Can use 'name'\n" +
                "    }\n" +
                "}\n\n" +
                "Usage:\n" +
                "Dog myDog = new Dog(\"Buddy\", 3);\n" +
                "myDog.eat();  // From Animal: \"Buddy is eating.\"\n" +
                "myDog.bark();  // From Dog: \"Buddy says Woof!\"")
            .addTheory("The 'super' Keyword",
                "'super' refers to the PARENT class:\n\n" +
                "1. CALLING PARENT CONSTRUCTOR:\n" +
                "   public Dog(String name, int age) {\n" +
                "       super(name, age);  // Must be FIRST line\n" +
                "   }\n\n" +
                "2. CALLING PARENT METHOD:\n" +
                "   public void eat() {\n" +
                "       super.eat();  // Call Animal's eat() first\n" +
                "       System.out.println(\"Dog is satisfied.\");\n" +
                "   }\n\n" +
                "3. ACCESSING PARENT FIELD:\n" +
                "   System.out.println(super.name);  // Usually just use 'name'\n\n" +
                "NOTE: If you don't call super(...) explicitly, Java tries to call\n" +
                "the parent's no-argument constructor automatically.")
            .addKeyPoint("Inheritance Best Practices",
                "1. IS-A RELATIONSHIP:\n" +
                "   - Dog IS-A Animal ✓ (inheritance makes sense)\n" +
                "   - Car IS-A Engine ✗ (Car HAS-A Engine, use composition instead)\n\n" +
                "2. INHERITANCE HIERARCHY:\n" +
                "   Object (built-in Java class)\n" +
                "     ↑\n" +
                "   Animal\n" +
                "     ↑\n" +
                "   Dog, Cat, Bird (all extend Animal)\n\n" +
                "3. EVERYTHING EXTENDS Object:\n" +
                "   - Every Java class automatically extends Object\n" +
                "   - Object provides: toString(), equals(), hashCode()\n\n" +
                "4. SINGLE INHERITANCE ONLY:\n" +
                "   - Java: class Dog extends Animal (ONE parent only)\n" +
                "   - Multiple inheritance causes problems (diamond problem)\n" +
                "   - Use interfaces for multiple \"contracts\" (later lesson)")
            .addChallenge(createInheritanceQuiz())
            .addChallenge(createVehicleChallenge())
            .addChallenge(createEmployeeChallenge())
            .addChallenge(createShapeChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createInheritanceQuiz() {
        return new Challenge.Builder("epoch-2-lesson-4-quiz", "Understanding Inheritance", ChallengeType.MULTIPLE_CHOICE)
            .description("What does the 'extends' keyword do?")
            .addMultipleChoiceOption("A) Creates a new class from scratch")
            .addMultipleChoiceOption("B) Makes a class inherit fields and methods from a parent class")
            .addMultipleChoiceOption("C) Deletes the parent class")
            .addMultipleChoiceOption("D) Creates multiple copies of a class")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createVehicleChallenge() {
        return new Challenge.Builder("epoch-2-lesson-4-vehicle", "Create Vehicle Hierarchy", ChallengeType.CODE_COMPLETION)
            .description("Create:\n" +
                "1. Vehicle class with: String brand, int year, and method getInfo() that returns \"brand (year)\"\n" +
                "2. Car class that extends Vehicle and adds: int doors field\n" +
                "   Constructor should accept brand, year, and doors")
            .starterCode(
                "class Vehicle {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Car extends Vehicle {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Vehicle v = new Vehicle(\"Generic\", 2020);\nreturn v.getInfo();",
                "Generic (2020)",
                true
            ))
            .addTestCase(new TestCase(
                "Car c = new Car(\"Toyota\", 2022, 4);\nreturn c.getInfo();",
                "Toyota (2022)",
                true,
                "Car should inherit getInfo() from Vehicle"
            ))
            .addTestCase(new TestCase(
                "Car c = new Car(\"Honda\", 2023, 2);\nreturn c.doors;",
                "2",
                true
            ))
            .build();
    }

    private static Challenge createEmployeeChallenge() {
        return new Challenge.Builder("epoch-2-lesson-4-employee", "Employee Hierarchy", ChallengeType.CODE_COMPLETION)
            .description("Create:\n" +
                "1. Person class with: String name, int age\n" +
                "2. Employee class extending Person with: double salary\n" +
                "3. Both should have constructors\n" +
                "4. Employee should have getSalary() method")
            .starterCode(
                "class Person {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Employee extends Person {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Person p = new Person(\"Alice\", 30);\nreturn p.name;",
                "Alice",
                true
            ))
            .addTestCase(new TestCase(
                "Employee e = new Employee(\"Bob\", 35, 50000.0);\nreturn e.name;",
                "Bob",
                true,
                "Employee should inherit name from Person"
            ))
            .addTestCase(new TestCase(
                "Employee e = new Employee(\"Carol\", 28, 60000.0);\nreturn e.getSalary();",
                "60000.0",
                true
            ))
            .build();
    }

    private static Challenge createShapeChallenge() {
        return new Challenge.Builder("epoch-2-lesson-4-shape", "Shape Hierarchy", ChallengeType.FREE_CODING)
            .description("Create:\n" +
                "1. Shape class with: String color field and constructor\n" +
                "2. Rectangle class extending Shape with: int width, int height\n" +
                "3. Rectangle constructor should call super(color) and set width/height\n" +
                "4. Rectangle should have getArea() method returning width * height")
            .starterCode(
                "class Shape {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Rectangle extends Shape {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Rectangle r = new Rectangle(\"red\", 5, 10);\nreturn r.color;",
                "red",
                true,
                "Rectangle should inherit color from Shape"
            ))
            .addTestCase(new TestCase(
                "Rectangle r = new Rectangle(\"blue\", 4, 6);\nreturn r.getArea();",
                "24",
                true
            ))
            .addTestCase(new TestCase(
                "Rectangle r = new Rectangle(\"green\", 7, 3);\nreturn r.getArea();",
                "21",
                true
            ))
            .build();
    }
}
