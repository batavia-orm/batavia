package com.batavia.orm.reverter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MigrationReverter {
    private final String migrations_dir;
    private final Connection connection;

    public MigrationReverter(String migrations_dir, Connection connection) {
        this.migrations_dir = migrations_dir;
        this.connection = connection;
    }

    public void revert(String desiredLastAppliedMigration) {
        try (Statement statement = connection.createStatement()) {
            List<String> migrationsToRevert = getMigrationsToRevert(desiredLastAppliedMigration, statement);
            for (String migrationFile : migrationsToRevert) {
                revertMigration(migrationFile, statement);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void revert() {
        try (Statement statement = connection.createStatement()) {
            String lastAppliedMigration = getLastAppliedMigration(statement);
            if (lastAppliedMigration == null) {
                System.out.println("No migration to revert");
            } else {
                revertMigration(lastAppliedMigration, statement);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getLastAppliedMigration(Statement statement) throws SQLException {
        String query = "SELECT migration_file FROM batavia_migrations ORDER BY id DESC LIMIT 1";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                String lastAppliedMigration = resultSet.getString("migration_file");
                return lastAppliedMigration;
            }
        }
        return null;
    }
    

    private List<String> getMigrationsToRevert(String desiredLastAppliedMigration, Statement statement) throws SQLException {
        String query = "SELECT migration_file FROM batavia_migrations ORDER BY id DESC";
        List<String> migrationFilesToRevert = new ArrayList<>();
        if (migrationFileExists(desiredLastAppliedMigration, statement)) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String migrationFile = resultSet.getString("migration_file");
                    if (migrationFile.equals(desiredLastAppliedMigration)) {
                        break;
                    }
                    migrationFilesToRevert.add(migrationFile);
                }
            }
        } else {
            System.out.println("Invalid migration filename or specified migration file is unapplied");
        }
        return migrationFilesToRevert;
    }

    private boolean migrationFileExists(String migrationFileName, Statement statement) throws SQLException {
        String query = String.format("SELECT EXISTS (SELECT * FROM batavia_migrations WHERE migration_file = '%s')", migrationFileName);
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                boolean exists = resultSet.getBoolean(1);
                return exists;
            }
        }
        return false;
    }

    private void revertMigration(String migrationFile, Statement statement) throws IOException, SQLException {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_YELLOW = "\u001B[33m";
        System.out.println("Reverting migration: " + ANSI_YELLOW + migrationFile + ANSI_RESET);

        // Get down migration script filename
        int dotIndex = migrationFile.lastIndexOf('.');
        String baseName = migrationFile.substring(0, dotIndex);
        String extension = migrationFile.substring(dotIndex);
        String downFileName = baseName + ".down" + extension;

        String migrationContent = new String(Files.readAllBytes(Paths.get(migrations_dir + '/' + downFileName)), StandardCharsets.UTF_8);
        statement.execute(migrationContent);

        String query = "DELETE FROM batavia_migrations WHERE migration_file = '" + migrationFile + "'";
        statement.executeUpdate(query);
        
    }
}
