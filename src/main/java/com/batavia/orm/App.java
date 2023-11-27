package com.batavia.orm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DATABASE_URL");

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to the PostgreSQL server successfully.");
            statement = connection.createStatement();

            // Read the migration SQL script from file
            String migrationScript = readMigrationScript("batavia\\src\\main\\java\\com\\batavia\\orm\\migration.sql");

            // Execute the migration script
            statement.executeUpdate(migrationScript);

            System.out.println("Migration executed successfully!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // Close the statement and connection
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String readMigrationScript(String filePath) throws IOException {
        StringBuilder scriptBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scriptBuilder.append(line).append(System.lineSeparator());
            }
        }
        return scriptBuilder.toString();
    }
}