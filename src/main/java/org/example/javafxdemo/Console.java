package org.example.javafxdemo;

import javafx.scene.input.KeyCode;

public class Console {
    static UI UI;
    public Console(UI ui) {
        UI = ui;
    }
    public static boolean signedIn = false;
    static String input;

    static void getInput(Runnable callback) {
        UI.textArea.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                System.out.println("-UserInput: Pressed Enter");
                UI.preInput = UI.textArea.getText();
                UI.lines = UI.preInput.split("\n");
                if (UI.lines.length >= 1) {
                    input = UI.lines[UI.lines.length - 1];
                    System.out.println("-UserInput: " + input);
                    callback.run();
                }
                UI.textArea.appendText("");
            } else if (ke.getCode().equals(KeyCode.ALT_GRAPH)) { //ALT_GRAPH -> Alt Gr
                System.out.println("-UserALTGR: " + input);
            }
        });
    }

    public static void console(String input) {
        if (signedIn) {
            switch (input) {
                case "help":
                    say("help - Show all available commands.\nexit - Close the program");
                    break;
                case "exit":
                    wait(250);
                    exit();
                default:
                    say("Unknown Command!");
            }
        } else {
            Console.signUpOrLogin();
        }
    }

    public static void signUpOrLogin() {
        say("Choose an option:\n[1] Sign up\n[2] Log in");
        getInput(() -> {
            var message = switch (input) {
                case "1" -> "1";
                case "2" -> "2";
                default -> "Invalid choice";
            };
            say(message);
        });
    }

    static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void say(String text) {
        UI.textArea.appendText(text + "\n");
    }

    static void exit() {
        System.exit(0);
    }
}
