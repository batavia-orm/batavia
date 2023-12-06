package com.batavia.orm.cli;

public class ShowMigrationsCommand implements Command {

  private Receiver receiver;

  public ShowMigrationsCommand(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute() {
    receiver.showMigrations();
  }
}