package com.batavia.orm.cli;


public class GenerateMigrationCommand implements Command {
    private String migrationFilename;

    // Constructor for generation with filename
    public GenerateMigrationCommand(String migrationFilename) {
        this.migrationFilename = migrationFilename;
    }

    @Override
    public void execute() {
        System.out.println("Generating migration: " + migrationFilename);
    }
}