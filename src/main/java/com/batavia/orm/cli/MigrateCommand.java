package com.batavia.orm.cli;

// Concrete command for migrating
public class MigrateCommand implements Command {

  private Receiver receiver;

  public MigrateCommand(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute() {
    receiver.migrate();
  }
}