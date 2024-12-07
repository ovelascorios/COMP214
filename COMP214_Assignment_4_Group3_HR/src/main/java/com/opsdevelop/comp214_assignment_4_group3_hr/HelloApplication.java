/*
  File Name: HelloApplication.java
  Description: This class serves as the main entry point for a JavaFX-based Human Resources application.
               It includes methods for initializing the graphical user interface (GUI) and
               connecting to an Oracle database for backend operations.
  Group Number: 03
  Date: December 8, 2024
*/


package com.opsdevelop.comp214_assignment_4_group3_hr;

// Imports required libraries for JavaFX and database connectivity
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {

    //Utility class for database connection.
    public static class DatabaseConnector {
        private static final String URL = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";  //Remote conexion
        //private static final String URL = "jdbc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD"; // Centennial conexion
        private static final String USER = "COMP214_F24_er_28";
        private static final String PASSWORD = "password";


        // Establishes a connection to the database.
        public static Connection connect() throws SQLException {
            try {
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                throw e;
            }
        }
    }

    // The main entry point for JavaFX application.
    @Override
    public void start(Stage stage) throws IOException {

        // Load the FXML layout file
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 600);

        // Set the stage title and scene
        stage.setTitle("Human Resources App");
        stage.setScene(scene);

        // Display the application window
        stage.show();
    }

    //The main method, entry point of the application.
    public static void main(String[] args) {
        try {
            // Test database connection at startup
            Connection connection = DatabaseConnector.connect();
            System.out.println("Database connected successfully!");
            connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }

        // Launch the JavaFX application
        launch();
    }
}
