package org.example.javafxdemo;

import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import static java.nio.file.Files.readAllLines;
import java.io.*;

public class Console {
    static UI UI;
    public Console(UI ui) {
        UI = ui;
    }
    public static boolean signedIn = false;
    static String input;
    static Path userPath = java.nio.file.Paths.get("src/main/java/org/example/javafxdemo/data/usernames.txt");
    static boolean usernameInUse = false;
    static int accountNumber;
    static int maxAccounts = 128;

    static void getInput(Runnable callback) {
        UI.textArea.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case KeyCode.ENTER:
                    System.out.println("-UserInput: Pressed Enter");
                    UI.preInput = UI.textArea.getText();
                    UI.lines = UI.preInput.split("\n");
                    if (UI.lines.length >= 1) {
                        input = UI.lines[UI.lines.length - 1];
                        System.out.println("-UserInput: " + input);
                        callback.run();
                    }
                    break;
                case KeyCode.ALT_GRAPH:
                    System.out.println("-UserALTGR:\nInput: " + input + "\nPathUser: " + userPath + "\nsignedIn: " + signedIn + "\naccountNumber: " + accountNumber);
                    break;
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
            }
        });
    }
    static void signUpUs() {
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
                        for (String line : readAllLines(userPath)) {
                            if (Objects.equals(line, input)) {
                                usernameInUse = true;
                                break;
                            }
                        }
                        if (usernameInUse) {
                            say("Username already in use, try a different one");
                            signUpUs();
                        } else {
                           for (String line : readAllLines(userPath)) {
                               accountNumber++;
                               if (Objects.equals(line, "-")) {
                                   List<String> linesUser = Files.readAllLines(userPath, StandardCharsets.UTF_8);
                                   linesUser.set(accountNumber - 1, input);
                                   Files.write(userPath, linesUser, StandardCharsets.UTF_8);
                                   break;
                               }
                           }
                           signUpPass();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    static void signUpPass() {
        say("Password:");
        getInput(() -> {

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
