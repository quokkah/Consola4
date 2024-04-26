package org.example.javafxdemo;

import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import static java.nio.file.Files.readAllLines;

public class Console {
    static UI UI;
    public Console(UI ui) {
        UI = ui;
    }
    public static boolean signedIn = false;
    static String input;
    static Path pathUser = java.nio.file.Paths.get("src/main/java/org.example.javafxdemo/data/usernames.txt");
    static boolean usernameInUse = false;
    static int accountNumber;

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

    public static void signUpOrLogin() {
        say("Choose an option:\n[1] Sign up\n[2] Log in");
        getInput(() -> {
            switch (input) {
                case "1" -> signUpUs();
                case "2" -> logInUs();
                default -> say("Invalid choice");
            };
        });
    }
    static void signUpUs() {
        accountNumber = 0;
        usernameInUse = false;
        say("Username:");
        getInput(() -> {
            if (Objects.equals(input, "-back")) {
                signUpOrLogin();
            } else {
                if (input.startsWith("-")) {
                    say("Username cannot start with '-'");
                    signUpUs();
                } else {
                    try {
                        for (String line : readAllLines(pathUser)) {
                            if (Objects.equals(line, input)) {
                                usernameInUse = true;
                                break;
                            }
                        }
                        if (usernameInUse) {
                            say("Username already in use, try a different one");
                            signUpUs();
                        } else {
                            for (String line : readAllLines(pathUser)) {
                                if (line.isEmpty()) {
                                    Files.newBufferedWriter(pathUser , StandardOpenOption.TRUNCATE_EXISTING);
                                }
                                accountNumber++;
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    static void logInUs() {

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
