package org.example.javafxdemo;

import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import static java.nio.file.Files.readAllLines;

public class Console {
    static UI UI;
    public Console(UI ui) {
        UI = ui;
    }
    public static boolean signedIn = false;
    static String input;
    static Path userPath = java.nio.file.Paths.get("src/main/java/org/example/javafxdemo/data/usernames.txt");
    static Path passPath = java.nio.file.Paths.get("src/main/java/org/example/javafxdemo/data/passwords.txt");
    static boolean usernameInUse = false;
    static int accountNumber;

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
            if (dataReqs(input, "Username")) {
                try {
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                signUpUs();
            }
        });
    }
    static void signUpPass() {
        say("Password:");
        getInput(() -> {
            if (dataReqs(input, "Password")) {
                List<String> linesPass = null;
                try {
                    linesPass = Files.readAllLines(passPath, StandardCharsets.UTF_8);
                    linesPass.set(accountNumber - 1, input);
                    Files.write(passPath, linesPass, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                say("Password set, continue to home screen");
            } else {
                signUpPass();
            }
        });
    }
    static boolean dataReqs(String input, String passOrUser) {
        if (input.startsWith("-")) {
            say(passOrUser + " cannot start with '-'");
            return false;
        } else if (input.isEmpty()) {
            say(passOrUser + " cannot be empty");
            return false;
        } else if (input.contains(" ")) {
            say(passOrUser + " cannot contain spaces");
            return false;
        } else {
            if (Objects.equals(passOrUser, "Password")) {
                return true;
            } else {
                try {
                    for (String line : readAllLines(userPath)) {
                        if (Objects.equals(line, input)) {
                            say("Username already in use");
                            usernameInUse = true;
                            break;
                        }
                    }
                    return !usernameInUse;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
