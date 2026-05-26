package com.example;

import javafx.application.Application;
import javafx.stage.Stage;
import view.DashboardView;

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
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create and show the dashboard
        DashboardView dashboard = new DashboardView(primaryStage);
        dashboard.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}