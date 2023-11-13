package com.batavia.orm.runner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class RunnerMain {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DATABASE_URL");
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
            Statement statement = connection.createStatement()) {

            if (args.length > 0) {
                String migrationToRevert = args[0];
                revertMigrations(migrationToRevert, statement);
            } else {
                boolean migrationsTableExists = checkMigrationsTableExists(statement);
                if (!migrationsTableExists) {
                    createMigrationsTable(statement);
                }

                File[] localMigrationFiles = getLocalMigrationFiles();
                for (File migrationFile : localMigrationFiles) {
                    if (migrationIsUnapplied(migrationFile, statement)) {
                        runMigration(migrationFile, statement);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkMigrationsTableExists(Statement statement) throws SQLException {
        String query = "SELECT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'batavia_migrations')";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                boolean exists = resultSet.getBoolean(1);
                System.out.println("Exists: " + exists);
                return exists;
            }
        }
        return false;
    }

    private static void createMigrationsTable(Statement statement) throws SQLException {
        String query = "CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))";
        statement.executeUpdate(query);
        System.out.println("Migrations table created.");
    }

    private static File[] getLocalMigrationFiles() {
        File migrationsDir = new File(MIGRATIONS_DIR);
        return migrationsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".sql"));
    }

    private static boolean migrationIsUnapplied(File migrationFile, Statement statement) throws SQLException {
        String migrationFileName = migrationFile.getName();
        String query = "SELECT COUNT(*) FROM batavia_migrations WHERE migration_file = '" + migrationFileName + "'";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            resultSet.next();
            int count = resultSet.getInt(1);
            return count == 0;
        }
    }

    private static void runMigration(File migrationFile, Statement statement) throws IOException, SQLException {
        String migrationFileName = migrationFile.getName();
        System.out.println("Executing migration: " + migrationFileName);
        String migrationContent = new String(Files.readAllBytes(Paths.get(MIGRATIONS_DIR + '/' + migrationFileName)), StandardCharsets.UTF_8);
        statement.execute(migrationContent);

        String query = "INSERT INTO batavia_migrations (migration_file) VALUES ('" + migrationFileName + "')";
        statement.executeUpdate(query);
        System.out.println("Migration executed successfully.");
    }

    private static void revertMigrations(String migrationToRevert, Statement statement) throws SQLException {
        String query = "SELECT migration_file FROM migrations ORDER BY id DESC";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            boolean migrationFound = false;
            while (resultSet.next()) {
                String migrationFile = resultSet.getString("migration_file");
                if (migrationFile.equals(migrationToRevert)) {
                    migrationFound = true;
                    break;
                } else {
                    revertMigration(migrationFile, statement);
                }
            }
            if (!migrationFound) {
                System.out.println("Specified migration file '" + migrationToRevert + "' not found.");
            }
        }
    }

    private static void revertMigration(String migrationFile, Statement statement) throws SQLException {
        String query = "DELETE FROM migrations WHERE migration_file = '" + migrationFile + "'";
        statement.executeUpdate(query);
        System.out.println("Reverted migration: " + migrationFile);
        // Add code here to revert the specific migration logic (e.g., undo SQL statements)
    }
}
