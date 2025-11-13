package com.socraticjava.model;

/**
 * Types of challenges a student can encounter
 */
public enum ChallengeType {
    /**
     * Multiple choice question with predefined answers
     */
    MULTIPLE_CHOICE,

    /**
     * Student must complete partial code (fill in the blanks)
     */
    CODE_COMPLETION,

    /**
     * Student writes code from scratch, validated by test cases
     */
    FREE_CODING,

    /**
     * Conceptual question requiring text answer (not auto-validated)
     */
    CONCEPTUAL
}
