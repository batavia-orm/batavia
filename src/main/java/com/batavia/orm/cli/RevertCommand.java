package com.batavia.orm.cli;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.batavia.orm.reverter.MigrationReverter;

import io.github.cdimascio.dotenv.Dotenv;

public class RevertCommand implements Command{
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DATABASE_URL");
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

    @Override
    public void execute() {
        System.out.println("Reverting...");

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);) {
            MigrationReverter migrationReverter = new MigrationReverter(MIGRATIONS_DIR, connection);
            migrationReverter.revert();          
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}