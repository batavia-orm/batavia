package com.batavia.orm.cli;


public class GenerateMigrationCommand implements Command {
    private String migrationFilename;

    public GenerateMigrationCommand(String migrationFilename) {
        this.migrationFilename = migrationFilename;
    }

    @Override
    public void execute() {
        System.out.println("Generating migration: " + migrationFilename);
    }
}