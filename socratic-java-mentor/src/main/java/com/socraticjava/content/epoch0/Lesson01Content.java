package com.socraticjava.content.epoch0;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 0.1: What is a Computer Program?
 * Conceptual lesson to introduce algorithmic thinking
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-0-lesson-1", "Lesson 0.1: What is a Computer Program?")
            .addTheory("The Problem",
                "Imagine you're trying to teach your younger sibling how to make a peanut butter " +
                "and jelly sandwich. You leave the kitchen for 5 minutes and return to find:\n\n" +
                "• The peanut butter jar is unopened\n" +
                "• They spread jelly on the outside of the bread\n" +
                "• The knife is on the floor\n" +
                "• They're confused why it didn't work\n\n" +
                "What went wrong? You assumed they knew things: \"spread\" means using a knife, " +
                "\"on the bread\" means the inside face, you need to open jars first. But your sibling " +
                "followed your words EXACTLY as you said them—no more, no less.\n\n" +
                "THIS IS HOW COMPUTERS WORK.\n\n" +
                "A computer is the most obedient, most literal \"sibling\" imaginable. It will do " +
                "exactly what you tell it—nothing more, nothing less. It has zero common sense. " +
                "It can't guess what you meant. It can't improvise.")
            .addAnalogy("Programming is Recipe Writing (But More Precise)",
                "Let's compare a recipe for humans vs. a recipe for a computer:\n\n" +
                "Recipe for a Human:\n" +
                "1. Preheat oven to 350°F\n" +
                "2. Mix the ingredients\n" +
                "3. Bake for 20 minutes\n\n" +
                "Why this works: Humans know what \"mix\" means. They know ovens have dials.\n\n" +
                "Recipe for a Computer:\n" +
                "1. LOCATE the oven dial\n" +
                "2. TURN the dial clockwise until display shows 350\n" +
                "3. WAIT until the light labeled \"READY\" turns green\n" +
                "4. OPEN the ingredient_box\n" +
                "5. FOR EACH item in ingredient_box:\n" +
                "   a. MEASURE the item using measuring_cup\n" +
                "   b. POUR into bowl labeled \"mixing_bowl\"\n" +
                "6. PICK UP the spoon labeled \"mixing_spoon\"\n" +
                "7. MOVE spoon in circular motion for 60 seconds\n" +
                "8. PLACE mixing_bowl into oven\n" +
                "9. START timer for 20 minutes\n" +
                "10. WAIT until timer beeps\n\n" +
                "Notice: Every action is explicit. Every object is identified. No assumptions are made.\n\n" +
                "This is programming. You're writing a recipe so detailed that something with " +
                "ZERO intelligence can follow it perfectly.")
            .addTheory("What is a Computer Program, Really?",
                "A computer program is:\n\n" +
                "1. A SEQUENCE OF INSTRUCTIONS (step 1, step 2, step 3...)\n" +
                "2. WRITTEN IN A LANGUAGE THE COMPUTER UNDERSTANDS (like Java, Python, C++)\n" +
                "3. THAT MANIPULATES DATA (numbers, text, images)\n" +
                "4. TO PRODUCE A RESULT (a calculation, a webpage, a video game)\n\n" +
                "Breaking it down:\n\n" +
                "Instructions: Computers execute instructions one at a time, in order. Examples:\n" +
                "• \"Store the number 5 in a box\"\n" +
                "• \"Add 3 to whatever is in the box\"\n" +
                "• \"Show the result on the screen\"\n\n" +
                "Language: Computers don't understand English. They understand machine code (0s and 1s). " +
                "But writing in 0s and 1s is torture, so we use programming languages like Java, which " +
                "look more like English. Java gets translated into machine code automatically.\n\n" +
                "Data: Programs work with information: numbers, text, images, videos, sounds.\n\n" +
                "Results: Every program has a purpose—calculating, displaying, storing, or transforming data.")
            .addKeyPoint("Key Insight: Computers Are Dumb (And That's Okay)",
                "Computers CANNOT:\n" +
                "✗ Guess what you meant\n" +
                "✗ Improvise or adapt\n" +
                "✗ Understand context\n" +
                "✗ Learn without being programmed to learn\n\n" +
                "But computers CAN:\n" +
                "✓ Follow instructions perfectly, billions of times per second\n" +
                "✓ Remember massive amounts of data flawlessly\n" +
                "✓ Repeat tasks without getting tired or bored\n" +
                "✓ Do exactly what you tell them, every single time\n\n" +
                "Your job as a programmer: Be the smart one. Give the instructions. " +
                "The computer will be the obedient one.")
            .addChallenge(createMultipleChoiceChallenge())
            .estimatedMinutes(15)
            .build();
    }

    private static Challenge createMultipleChoiceChallenge() {
        return new Challenge.Builder("epoch-0-lesson-1-quiz", "Understanding Computer Programs", ChallengeType.MULTIPLE_CHOICE)
            .description("Which of the following best describes what a computer program is?")
            .addMultipleChoiceOption("A) A list of suggestions for the computer to try out")
            .addMultipleChoiceOption("B) A detailed, step-by-step sequence of instructions that a computer follows exactly")
            .addMultipleChoiceOption("C) A general description of what you want the computer to do, which it figures out on its own")
            .addMultipleChoiceOption("D) A conversation with the computer where you explain your goals")
            .correctAnswer("B")
            .build();
    }
}
