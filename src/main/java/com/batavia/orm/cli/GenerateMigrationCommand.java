package com.batavia.orm.cli;

import java.io.IOException;
import java.sql.SQLException;

import com.batavia.orm.comparator.ComparatorMain;

import io.github.cdimascio.dotenv.Dotenv;

public class GenerateMigrationCommand implements Command {
    private String migrationFilename = "migration-test-1";
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATASOURCE_DIR = dotenv.get("DATASOURCE_DIR");
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

    public GenerateMigrationCommand(String migrationFilename) {}

    @Override
    public void execute() {
        try {
            System.out.println("Generating migration: " + migrationFilename);
            ComparatorMain comparatorMain = new ComparatorMain(MIGRATIONS_DIR, DATASOURCE_DIR);
            comparatorMain.main(migrationFilename);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }       
    }
}