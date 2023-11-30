package com.batavia.orm.cli;

import java.io.IOException;
import java.sql.SQLException;

import com.batavia.orm.comparator.ComparatorMain;
import com.batavia.orm.utils.Utils;

import io.github.cdimascio.dotenv.Dotenv;

public class GenerateMigrationCommand implements Command {
    private String migrationFilename;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATASOURCE_DIR = dotenv.get("DATASOURCE_DIR");
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

    // Constructor for generation with filename
    public GenerateMigrationCommand(String migrationFilename) {
        this.migrationFilename = migrationFilename;
    }

    @Override
    public void execute() {
        try {
            String stampedFilename = Utils.generateTimestampedMigrationFilename(migrationFilename);
            System.out.println("Generating migration: " + stampedFilename);
            ComparatorMain comparatorMain = new ComparatorMain(MIGRATIONS_DIR, DATASOURCE_DIR);
            comparatorMain.main(stampedFilename);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }       
    }
}