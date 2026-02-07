package com.teic.trueris.game;

public class ScoreTracker {
    private int score;
    
    public void updateScore(boolean[] filledRows) {
        int totalFilledRows = 0;

        for (int row = 0; row < filledRows.length; row++) {
            if (!filledRows[row]) {
                continue;
            }

            totalFilledRows++;
        }

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
}
