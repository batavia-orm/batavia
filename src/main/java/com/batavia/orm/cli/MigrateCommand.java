package com.batavia.orm.cli;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.batavia.orm.runner.MigrationRunner;

import io.github.cdimascio.dotenv.Dotenv;

// Concrete command for migrating
public class MigrateCommand implements Command {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DATABASE_URL");
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

    @Override
    public void execute() {
        System.out.println("Migrating...");

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);) {
            MigrationRunner migrationRunner = new MigrationRunner(MIGRATIONS_DIR, connection);
            migrationRunner.migrate();          
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}