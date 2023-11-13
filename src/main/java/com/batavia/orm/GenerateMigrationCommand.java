package com.batavia.orm;

public class GenerateMigrationCommand implements Command {
    private String migrationFilename;

    public GenerateMigrationCommand(String migrationFilename) {
        this.migrationFilename = migrationFilename;
    }

    @Override
    public void execute() {
        System.out.println("Generating migration: " + migrationFilename);
        // Implement the logic for generating a migration based on the provided filename.
    }
}