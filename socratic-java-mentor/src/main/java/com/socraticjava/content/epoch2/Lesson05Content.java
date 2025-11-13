package com.socraticjava.content.epoch2;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 2.5: Polymorphism and Method Overriding
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-2-lesson-5", "Lesson 2.5: Polymorphism - One Interface, Many Forms")
            .addTheory("The Problem: Different Animals Make Different Sounds",
                "You have an Animal class with a makeSound() method:\n\n" +
                "class Animal {\n" +
                "    void makeSound() {\n" +
                "        System.out.println(\"Generic animal sound\");\n" +
                "    }\n" +
                "}\n\n" +
                "class Dog extends Animal {\n" +
                "    // Inherits makeSound(), but dogs don't make \"generic animal sounds\"!\n" +
                "}\n\n" +
                "class Cat extends Animal {\n" +
                "    // Same problem - cats meow, not generic sounds\n" +
                "}\n\n" +
                "We need a way for EACH subclass to have its OWN implementation\n" +
                "of makeSound() while still being treated as Animals.\n\n" +
                "Solution: METHOD OVERRIDING + POLYMORPHISM")
            .addAnalogy("Polymorphism is Like a Universal Remote",
                "UNIVERSAL REMOTE:\n" +
                "- Has a \"Power\" button\n" +
                "- Works for TV, DVD player, sound system\n" +
                "- SAME button, DIFFERENT behavior depending on device\n\n" +
                "POLYMORPHISM IN JAVA:\n" +
                "Animal[] animals = {new Dog(), new Cat(), new Bird()};\n\n" +
                "for (Animal a : animals) {\n" +
                "    a.makeSound();  // SAME method call\n" +
                "}\n\n" +
                "Output:\n" +
                "Woof!  (Dog's version)\n" +
                "Meow!  (Cat's version)\n" +
                "Chirp!  (Bird's version)\n\n" +
                "ONE interface (makeSound), MANY forms (each animal's unique sound).\n" +
                "That's poly (many) + morph (forms) = polymorphism!")
            .addTheory("Method Overriding with @Override",
                "OVERRIDING = Replacing a parent method with your own version\n\n" +
                "class Animal {\n" +
                "    public void makeSound() {\n" +
                "        System.out.println(\"Generic sound\");\n" +
                "    }\n" +
                "}\n\n" +
                "class Dog extends Animal {\n" +
                "    @Override  // ANNOTATION - tells Java \"I'm overriding\"\n" +
                "    public void makeSound() {\n" +
                "        System.out.println(\"Woof!\");\n" +
                "    }\n" +
                "}\n\n" +
                "class Cat extends Animal {\n" +
                "    @Override\n" +
                "    public void makeSound() {\n" +
                "        System.out.println(\"Meow!\");\n" +
                "    }\n" +
                "}\n\n" +
                "RULES FOR OVERRIDING:\n" +
                "1. Same method name\n" +
                "2. Same parameters (signature)\n" +
                "3. Same or more accessible (public > protected > default > private)\n" +
                "4. Use @Override annotation (optional but HIGHLY recommended)")
            .addTheory("Polymorphism in Action",
                "The MAGIC of polymorphism:\n\n" +
                "Animal myPet = new Dog();  // Dog IS-A Animal\n" +
                "myPet.makeSound();  // Calls Dog's version → \"Woof!\"\n\n" +
                "myPet = new Cat();  // Now it's a Cat\n" +
                "myPet.makeSound();  // Calls Cat's version → \"Meow!\"\n\n" +
                "This enables POWERFUL patterns:\n\n" +
                "void feedAnimal(Animal a) {\n" +
                "    System.out.println(\"Feeding...\");\n" +
                "    a.makeSound();  // Will call the RIGHT version automatically!\n" +
                "}\n\n" +
                "feedAnimal(new Dog());  // \"Feeding...\" then \"Woof!\"\n" +
                "feedAnimal(new Cat());  // \"Feeding...\" then \"Meow!\"\n" +
                "feedAnimal(new Bird());  // \"Feeding...\" then \"Chirp!\"\n\n" +
                "ONE method works with ALL Animal types!")
            .addKeyPoint("Dynamic Method Dispatch",
                "HOW does Java know which version to call?\n\n" +
                "Animal a = new Dog();\n" +
                "a.makeSound();  // Which makeSound()?\n\n" +
                "Java uses DYNAMIC DISPATCH (runtime polymorphism):\n" +
                "1. Looks at the ACTUAL object type (Dog)\n" +
                "2. Finds the method in Dog's class\n" +
                "3. Calls Dog's version, not Animal's\n\n" +
                "This happens at RUNTIME, not compile time!\n\n" +
                "KEY INSIGHT:\n" +
                "- Variable type (Animal) determines WHAT methods are available\n" +
                "- Object type (Dog) determines WHICH version gets called\n\n" +
                "This is the foundation of flexible, extensible code!")
            .addWarning("Overriding vs Overloading - Don't Confuse Them!",
                "OVERRIDING (this lesson):\n" +
                "- SAME method signature in child class\n" +
                "- REPLACES parent's version\n" +
                "- Requires inheritance\n\n" +
                "class Animal {\n" +
                "    void eat() { ... }\n" +
                "}\n" +
                "class Dog extends Animal {\n" +
                "    @Override\n" +
                "    void eat() { ... }  // OVERRIDING\n" +
                "}\n\n" +
                "OVERLOADING (Epoch 1):\n" +
                "- DIFFERENT parameters, same name\n" +
                "- ADDS new version\n" +
                "- Same class\n\n" +
                "class Calculator {\n" +
                "    int add(int a, int b) { ... }\n" +
                "    double add(double a, double b) { ... }  // OVERLOADING\n" +
                "}")
            .addChallenge(createPolymorphismQuiz())
            .addChallenge(createAnimalChallenge())
            .addChallenge(createShapeAreaChallenge())
            .addChallenge(createVehicleChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createPolymorphismQuiz() {
        return new Challenge.Builder("epoch-2-lesson-5-quiz", "Understanding Polymorphism", ChallengeType.MULTIPLE_CHOICE)
            .description("What happens when you call a method on a parent type variable holding a child object?\n\n" +
                "Animal a = new Dog();\n" +
                "a.makeSound();")
            .addMultipleChoiceOption("A) Compile error")
            .addMultipleChoiceOption("B) Calls Animal's version")
            .addMultipleChoiceOption("C) Calls Dog's overridden version")
            .addMultipleChoiceOption("D) Calls both versions")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createAnimalChallenge() {
        return new Challenge.Builder("epoch-2-lesson-5-animal", "Override Animal Sounds", ChallengeType.CODE_COMPLETION)
            .description("Complete the code:\n" +
                "1. Animal class has makeSound() that returns \"Some sound\"\n" +
                "2. Dog class overrides makeSound() to return \"Bark\"\n" +
                "3. Cat class overrides makeSound() to return \"Meow\"")
            .starterCode(
                "class Animal {\n" +
                "    public String makeSound() {\n" +
                "        return \"Some sound\";\n" +
                "    }\n" +
                "}\n\n" +
                "class Dog extends Animal {\n" +
                "    // Override makeSound here\n" +
                "}\n\n" +
                "class Cat extends Animal {\n" +
                "    // Override makeSound here\n" +
                "}")
            .addTestCase(new TestCase(
                "Animal a = new Animal();\nreturn a.makeSound();",
                "Some sound",
                true
            ))
            .addTestCase(new TestCase(
                "Dog d = new Dog();\nreturn d.makeSound();",
                "Bark",
                true
            ))
            .addTestCase(new TestCase(
                "Cat c = new Cat();\nreturn c.makeSound();",
                "Meow",
                true
            ))
            .addTestCase(new TestCase(
                "Animal a = new Dog();\nreturn a.makeSound();",
                "Bark",
                true,
                "Polymorphism: Animal variable holding Dog should call Dog's version"
            ))
            .build();
    }

    private static Challenge createShapeAreaChallenge() {
        return new Challenge.Builder("epoch-2-lesson-5-shape", "Polymorphic Shape Areas", ChallengeType.CODE_COMPLETION)
            .description("Create:\n" +
                "1. Shape class with getArea() returning 0.0\n" +
                "2. Circle class with radius field, overriding getArea() to return π * r²\n" +
                "3. Rectangle class with width and height, overriding getArea() to return width * height\n" +
                "Use Math.PI for π")
            .starterCode(
                "class Shape {\n" +
                "    public double getArea() {\n" +
                "        return 0.0;\n" +
                "    }\n" +
                "}\n\n" +
                "class Circle extends Shape {\n" +
                "    double radius;\n" +
                "    \n" +
                "    public Circle(double radius) {\n" +
                "        this.radius = radius;\n" +
                "    }\n" +
                "    \n" +
                "    // Override getArea here\n" +
                "}\n\n" +
                "class Rectangle extends Shape {\n" +
                "    int width, height;\n" +
                "    \n" +
                "    public Rectangle(int width, int height) {\n" +
                "        this.width = width;\n" +
                "        this.height = height;\n" +
                "    }\n" +
                "    \n" +
                "    // Override getArea here\n" +
                "}")
            .addTestCase(new TestCase(
                "Circle c = new Circle(5.0);\nreturn Math.abs(c.getArea() - 78.54) < 0.1;",
                "true",
                true,
                "Circle area = π * r² = π * 25 ≈ 78.54"
            ))
            .addTestCase(new TestCase(
                "Rectangle r = new Rectangle(4, 5);\nreturn r.getArea();",
                "20.0",
                true
            ))
            .addTestCase(new TestCase(
                "Shape s = new Circle(10.0);\nreturn s.getArea() > 300.0 && s.getArea() < 320.0;",
                "true",
                true,
                "Polymorphism: Shape variable with Circle object"
            ))
            .build();
    }

    private static Challenge createVehicleChallenge() {
        return new Challenge.Builder("epoch-2-lesson-5-vehicle", "Vehicle Start Methods", ChallengeType.FREE_CODING)
            .description("Create:\n" +
                "1. Vehicle class with start() method returning \"Starting vehicle\"\n" +
                "2. Car class overriding start() to return \"Turning key, engine starts\"\n" +
                "3. Motorcycle class overriding start() to return \"Kick-starting engine\"")
            .starterCode(
                "class Vehicle {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Car extends Vehicle {\n" +
                "    // Your code here\n" +
                "}\n\n" +
                "class Motorcycle extends Vehicle {\n" +
                "    // Your code here\n" +
                "}")
            .addTestCase(new TestCase(
                "Vehicle v = new Vehicle();\nreturn v.start();",
                "Starting vehicle",
                true
            ))
            .addTestCase(new TestCase(
                "Car c = new Car();\nreturn c.start();",
                "Turning key, engine starts",
                true
            ))
            .addTestCase(new TestCase(
                "Motorcycle m = new Motorcycle();\nreturn m.start();",
                "Kick-starting engine",
                true
            ))
            .addTestCase(new TestCase(
                "Vehicle v = new Car();\nreturn v.start();",
                "Turning key, engine starts",
                false,
                "Polymorphic call should invoke Car's version"
            ))
            .build();
    }
}
