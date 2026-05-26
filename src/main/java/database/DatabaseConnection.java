package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection class implements the Singleton pattern to manage a single database connection.
 *
 * Purpose: This class provides a single point of access to the MySQL database connection,
 * ensuring that only one connection instance exists throughout the application lifecycle.
 * This prevents resource exhaustion and ensures thread-safe access to the database.
 *
 * Design Pattern Used: Singleton Pattern
 * Why: The Singleton pattern is chosen here because database connections are expensive to create
 * and manage. Having multiple connections can lead to resource leaks, performance issues,
 * and inconsistent data states. Singleton ensures that all parts of the application use
 * the same connection, promoting efficient resource usage and data integrity.
 *
 * OOP Principles Demonstrated:
 * - Encapsulation: The connection details (URL, username, password) are encapsulated within the class.
 * - Abstraction: The class abstracts the complexity of connection management from the rest of the application.
 */
public class DatabaseConnection {
    // Singleton instance
    private static DatabaseConnection instance;
    private Connection connection;

    // Database credentials (in a real application, these should be externalized to a config file or environment variables)
    private static final String URL = "jdbc:mysql://localhost:3306/ethical_decision_engine";
    private static final String USERNAME = "root"; // Change as per your MySQL setup
    private static final String PASSWORD = "password"; // Change as per your MySQL setup

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the database connection.
     */
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC driver (optional in newer versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of DatabaseConnection.
     * If the instance doesn't exist, it creates one.
     *
     * @return The singleton DatabaseConnection instance
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the database connection.
     *
     * @return The Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the database connection.
     * This method should be called when the application is shutting down.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}