package com.batavia.orm.runner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class RunnerMainTest {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DATABASE_URL");

    @BeforeAll
    public static void setup() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            // Create the test migrations table
            String query = "CREATE TABLE batavia_migrations (id INT AUTO_INCREMENT PRIMARY KEY, migration_file VARCHAR(255))";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckMigrationsTableExists() throws SQLException {
       
    }

    @Test
    public void testCreateMigrationsTable() throws SQLException {
        
    }

    @Test
    public void testGetLocalMigrationFiles() {
        
    }

    @Test
    public void testMigrationIsUnapplied() throws SQLException {
        
    }

    @Test
    public void testRunMigration() throws SQLException {
        
    }
}
