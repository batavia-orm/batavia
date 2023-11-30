package com.batavia.orm.cli;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.batavia.orm.reverter.MigrationReverter;

import io.github.cdimascio.dotenv.Dotenv;

public class RevertCommand implements Command{
    private String migrationToRevertTo = null;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DATABASE_URL");
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

    public RevertCommand(String migrationToRevertTo) {
        this.migrationToRevertTo = migrationToRevertTo;
    }

    public RevertCommand() {}

    @Override
    public void execute() {
        System.out.println("\nReverting...\n");
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);) {
            MigrationReverter migrationReverter = new MigrationReverter(MIGRATIONS_DIR, connection);
            if (migrationToRevertTo == null) {
                migrationReverter.revert();     
                System.out.println("\n" + ANSI_GREEN + "Reverted applied migrations" + ANSI_RESET);
            } else {
                migrationReverter.revert(migrationToRevertTo);
                System.out.println("\n" + ANSI_GREEN + "Reverted applied migrations" + ANSI_RESET);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}