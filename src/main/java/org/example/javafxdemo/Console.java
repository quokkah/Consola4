package org.example.javafxdemo;

import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    static boolean usernameExists = false;
    static int accountNumber;
    static String correctPass;

    /*static void getInput(Runnable callback) {
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
    }*/
    static String getInput() {
        UI.textArea.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case KeyCode.ENTER:
                    System.out.println("-UserInput: Pressed Enter");
                    UI.preInput = UI.textArea.getText();
                    UI.lines = UI.preInput.split("\n");
                    if (UI.lines.length >= 1) {
                        input = UI.lines[UI.lines.length - 1];
                        System.out.println("-UserInput: " + input);
                        //why cant i put return here
                    }
                    break;
                case KeyCode.ALT_GRAPH:
                    System.out.println("-UserALTGR:\nInput: " + input + "\nPathUser: " + userPath + "\nsignedIn: " + signedIn + "\naccountNumber: " + accountNumber);
                    getInput();
                    break;
            }
        });
        return input;
    }

    public static void signUpOrLogin() {
        say("Choose an option:\n[1] Sign up\n[2] Log in");
        switch (getInput()) {
            case "1":
                signUpUs();
            case "2":
                logInUs();
            case null:
                System.out.println("null");
            default:
                say("Invalid choice");
        }
    }
    static void signUpUs() {
        usernameInUse = false;
        say("Username:");
        getInput();
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
    }
    static void signUpPass() {
        say("Password:");
        getInput();
        if (dataReqs(input, "Password")) {
            List<String> linesPass;
            try {
                linesPass = Files.readAllLines(passPath, StandardCharsets.UTF_8);
                linesPass.set(accountNumber - 1, input);
                Files.write(passPath, linesPass, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            signedIn = true;
            startMessage(true);
            console();
        } else {
            signUpPass();
        }
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
        accountNumber = 0;
        say("Username:");
        try {
            for (String line : readAllLines(userPath)) {
                accountNumber++;
                if (Objects.equals(line, input)) {
                    usernameExists = true;
                    break;
                }
            }
            if (usernameExists) {
                try (Stream<String> lines = Files.lines(passPath)) {
                    correctPass = lines.skip(accountNumber).findFirst().get();
                    System.out.println(correctPass);
                }
            } else {
                say("Username not found");
                logInUs();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void console() {
        if (signedIn) {
            getInput();
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

    public static void title() {
        say("""
                                     ________  ________  ________   ________  ________  ___       ________  ________     \s
                                    |\\   ____\\|\\   __  \\|\\   ___  \\|\\   ____\\|\\   __  \\|\\  \\     |\\   __  \\|\\   ____\\    \s
                                    \\ \\  \\___|\\ \\  \\|\\  \\ \\  \\\\ \\  \\ \\  \\___|\\ \\  \\|\\  \\ \\  \\    \\ \\  \\|\\  \\ \\  \\___|_   \s
                                     \\ \\  \\    \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \\_____  \\ \\  \\\\\\  \\ \\  \\    \\ \\   __  \\ \\_____  \\  \s
                                      \\ \\  \\____\\ \\  \\\\\\  \\ \\  \\\\ \\  \\|____|\\  \\ \\  \\\\\\  \\ \\  \\____\\ \\  \\ \\  \\|____|\\  \\ \s
                                       \\ \\_______\\ \\_______\\ \\__\\\\ \\__\\____\\_\\  \\ \\_______\\ \\_______\\ \\__\\ \\__\\____\\_\\  \\\s
                                        \\|_______|\\|_______|\\|__| \\|__|\\_________\\|_______|\\|_______|\\|__|\\|__|\\_________\\
                                                                      \\|_________|                            \\|_________|
                                                                        by quokkah\s
                """
        );
    }
    static void startMessage(boolean doWelcome) {
        if (doWelcome) {
            clear(false);
            say("Welcome!");
        }
        say("Type 'help' for commands");
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
    static void clear(boolean fullClear) {
        UI.textArea.clear();
        if (!fullClear) {
            title();
        }
    }
    static void exit() {
        System.exit(0);
    }
}
