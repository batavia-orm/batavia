package com.batavia.orm.cli;

public class GenerateMigrationCommand implements Command {

  private String migrationFilename;
  private Receiver receiver;

  public GenerateMigrationCommand(Receiver receiver, String migrationFilename) {
    this.receiver = receiver;
    this.migrationFilename = migrationFilename;
  }

  public GenerateMigrationCommand(Receiver receiver) {
    this(receiver, "last");
  }

  @Override
  public void execute() {
    receiver.generateMigration(migrationFilename);
  }
}