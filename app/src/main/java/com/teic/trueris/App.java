package com.teic.trueris;

import com.teic.trueris.display.JLine3Renderer;
import com.teic.trueris.display.Renderer;
import com.teic.trueris.input.Key;
import com.teic.trueris.input.Input;
import com.teic.trueris.input.JLine3Input;

import java.io.IOException;

public class App {
    private final JLine3Renderer renderer;
    private final Input input;

    public static void main(String[] args) {
        try (
                JLine3Renderer renderer = new JLine3Renderer();
                Input input = new JLine3Input(renderer.getTerminal());
        ) {

            App app = new App(renderer, input);
            app.start();

        } catch (IOException error) {
            System.out.println("Failed!");
        }
    }

    public App(JLine3Renderer renderer, Input input) {
        this.renderer = renderer;
        this.input = input;
    }

    private void start() {
        String value = "";

        while (true) {
            renderer.clearScreen();

            renderer.putString(2, 1, "Tetrue Lite");
            renderer.putString(2, 3, "1. New Game");
            renderer.putString(2, 4, "2. About");
            renderer.putString(2, 5, "0. Exit");
            renderer.putString(2, 7, "Press the keys 1, 2, 0 to navigate.");
            renderer.putString(2, 10, "");

            renderer.putString(2, 8, "Key: " + value);

            Key key = input.readInput();

            if (!key.hasCharacter()) {
                if (key.matches(Key.UP)) {
                    break;
                }
                else if (key.matches(Key.ESCAPE)) {
                    break;
                }

                continue;
            }

            value = String.valueOf(key.getCharacter());
        }
    }
}
