package com.batavia.orm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String url = "jdbc:postgresql://db.msfwnmkjpydhcuhmcssf.supabase.co:5432/postgres?user=postgres&password=butewahmansion@21";

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to the PostgreSQL server successfully.");
            statement = connection.createStatement();

            // Read the migration SQL script from file
            String migrationScript = readMigrationScript("src/main/java/com/batavia/orm/migration.sql");

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
