package com.teic.trueris.game;

public class ScoreTracker {
    private int score;
    private boolean hasLineCleared;
    
    public void updateScore(boolean[] filledRows) {
        int totalFilledRows = 0;

        for (boolean filledRow : filledRows) {
            if (!filledRow) {
                continue;
            }

            totalFilledRows++;
        }

        if (totalFilledRows > 0) hasLineCleared = true;

        score += switch (totalFilledRows) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 600;
            case 4 -> 1500;
            default -> 0;
        };
    }

    public int getScore() {
        return score;
    }

    public boolean hasLineCleared() {
        return hasLineCleared;
    }

    public void setLineCleared(boolean value) {
        hasLineCleared = value;
    }
}
