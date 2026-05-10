package com.teic.trueris;

import com.teic.trueris.display.JLine3Renderer;
import com.teic.trueris.display.Renderer;
import com.teic.trueris.input.Action;
import com.teic.trueris.input.Input;
import com.teic.trueris.input.JLine3Input;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try (
            JLine3Renderer render = new JLine3Renderer();
        ) {
            Input input = new JLine3Input(render.getTerminal());

            render.clearScreen();
            render.putString(1, 5, "Hello World!");
            render.flush();

            Action action = input.readInput();

            String word = switch (action) {
                case CLOCKWISE_ROTATE -> "clock";
                case COUNTER_CLOCKWISE_ROTATE -> "counter";
                case UP -> "up";
                case DOWN -> "down";
                case LEFT -> "left";
                case RIGHT -> "right";
                case QUIT -> "quit";
                case NONE -> "none";
                case ENTER -> "enter";
            };

            render.putString(1, 6, "Input: " + word);
            render.flush();
        }
        catch (IOException error) {
            System.out.println("Failed!");
        }
    }
}
