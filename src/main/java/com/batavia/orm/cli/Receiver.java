package com.batavia.orm.cli;

public class Receiver {
    public void generateMigration(String migrationFilename) {
        GenerateMigrationCommand generateMigrationCommand;
        if (migrationFilename == null) {
            generateMigrationCommand = new GenerateMigrationCommand(this);
        } else {
            generateMigrationCommand = new GenerateMigrationCommand(this, migrationFilename);
        }
        generateMigrationCommand.execute();
    }

    public void migrate() {
        MigrateCommand migrateCommand = new MigrateCommand(this);
        migrateCommand.execute();
    }

    public void revert(String previousMigrationFilename) {
        RevertCommand revertCommand;
        if (previousMigrationFilename == null) {
            revertCommand = new RevertCommand();
        } else {
            revertCommand = new RevertCommand(this, previousMigrationFilename);
        }
        revertCommand.execute();
    }
    

    public void showMigrations() {
        ShowMigrationsCommand showMigrations = new ShowMigrationsCommand(this);
        showMigrations.execute();
    }
}