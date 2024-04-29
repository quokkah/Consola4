package org.example.javafxdemo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.util.Objects;

public class UI extends Application {

    public TextArea textArea = new TextArea();
    public String preInput = "";
    public String[] lines = new String[0];
    Console Console = new Console(this);
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Font font = Font.loadFont("file:resources/fonts/CONSOLA.ttf", 20);
        textArea.setFont(font);
        StackPane.setAlignment(textArea, Pos.TOP_LEFT);

        root.getChildren().add(textArea);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        primaryStage.setTitle("Consolas");
        primaryStage.setScene(scene);
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setFullScreen(false); //turn this off when debugging
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        org.example.javafxdemo.Console.title();
        org.example.javafxdemo.Console.signUpOrLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
