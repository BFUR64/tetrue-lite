package com.teic.trueris;

import com.teic.trueris.display.JLine3Renderer;
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
        while (true) {
            renderer.clearScreen();

            renderer.putString(2, 1, "Tetrue Lite");
            renderer.putString(2, 3, "1. New Game");
            renderer.putString(2, 4, "2. About");
            renderer.putString(2, 5, "0. Exit");
            renderer.putString(2, 7, "Press the keys 1, 2, 0 to navigate.");

            renderer.flush();

            Key key = input.readInput();

            if (!key.hasCharacter()) {
                if (key.equals(Key.ESCAPE)) {
                    break;
                }

                continue;
            }

            if (key.matches('1')) {

            }
            else if (key.matches('2')) {
                renderer.clearScreen();

                renderer.putString(2, 1, "About");
                renderer.putString(2, 3, "Simple Tetrue clone by TEIC.");
                renderer.putString(2, 5, "Press any key to continue...");
                renderer.flush();

                input.readInput();
            }
            else if (key.matches('0')) {
                break;
            }
        }

        renderer.clearScreen();
        renderer.flush();
    }
}
