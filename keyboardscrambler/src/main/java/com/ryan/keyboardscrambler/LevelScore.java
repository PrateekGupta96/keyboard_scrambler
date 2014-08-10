package com.ryan.keyboardscrambler;

/** The score for a level */

public class LevelScore {
    private final Level theLevel;
    private final double lettersPerSecond;
    private final int score;

    public LevelScore(com.ryan.keyboardscrambler.Level theLevel, double lettersPerSecond, int score) {
        this.theLevel = theLevel;
        this.lettersPerSecond = lettersPerSecond;
        this.score = score;
    }

    public Level getTheLevel() {
        return theLevel;
    }

    public double getLettersPerSecond() {
        return lettersPerSecond;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Level: " + theLevel.toString() +
                "\tLPS: " + lettersPerSecond +
                "\tScore: " + score;
    }
}
