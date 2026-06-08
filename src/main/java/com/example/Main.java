package com.example;

import javafx.application.Application;
import javafx.stage.Stage;
import view.AppNavigator;

/**
 * Main class for the Ethical Decision Engine application.
 *
 * Purpose: This is the entry point of the JavaFX application. It initializes the dashboard view.
 *
 * OOP Principles Demonstrated:
 * - This class demonstrates basic class structure and inheritance (extends Application).
 *
 * Design Pattern: MVC - Initializes the View component.
 */
// is the starting point of the application and launches the JavaFX user interface.
public class Main extends Application {

    @Override
// is called by JavaFX to start the application window.
    public void start(Stage primaryStage) {
        new AppNavigator(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}