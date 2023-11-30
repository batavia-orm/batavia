package com.batavia.orm.runner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MigrationRunner {
    private final String migrations_dir;
    private final Connection connection;

    public MigrationRunner(String migrations_dir, Connection connection) {
        this.migrations_dir = migrations_dir;
        this.connection = connection;
    }

    public void migrate() {
        try (Statement statement = connection.createStatement()) {
            boolean migrationsTableExists = checkMigrationsTableExists(statement);
            if (!migrationsTableExists) {
                createMigrationsTable(statement);
            }

            File[] localMigrationFiles = getLocalMigrationFiles();
            for (File migrationFile : localMigrationFiles) {
                if (migrationIsUnapplied(migrationFile, statement)) {
                    executeMigration(migrationFile, statement);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkMigrationsTableExists(Statement statement) throws SQLException {
        String query = "SELECT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'batavia_migrations')";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                boolean exists = resultSet.getBoolean(1);
                System.out.println("Migration table exists: " + exists);
                return exists;
            }
        }
        return false;
    }

    private void createMigrationsTable(Statement statement) throws SQLException {
        String query = "CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))";
        statement.executeUpdate(query);
        System.out.println("Migrations table created.");
    }

    public File[] getLocalMigrationFiles() {
        File migrationsDir = new File(migrations_dir);
        System.out.println("Getting local migration files...");
        return migrationsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".sql") && !name.toLowerCase().endsWith(".down.sql"));
    }

    private boolean migrationIsUnapplied(File migrationFile, Statement statement) throws SQLException {
        String migrationFileName = migrationFile.getName();
        String query = "SELECT COUNT(*) FROM batavia_migrations WHERE migration_file = '" + migrationFileName + "'";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            resultSet.next();
            int count = resultSet.getInt(1);
            return count == 0;
        }
    }

    private void executeMigration(File migrationFile, Statement statement) throws IOException, SQLException {
        String migrationFileName = migrationFile.getName();
        System.out.println("Executing migration: " + migrationFileName);
        String migrationContent = new String(Files.readAllBytes(Paths.get(migrations_dir + '/' + migrationFileName)), StandardCharsets.UTF_8);
        statement.execute(migrationContent);

        String query = "INSERT INTO batavia_migrations (migration_file) VALUES ('" + migrationFileName + "')";
        statement.executeUpdate(query);
        System.out.println("Migration executed successfully.");
    }
}
