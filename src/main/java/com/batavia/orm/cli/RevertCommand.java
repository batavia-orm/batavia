package com.batavia.orm.cli;

public class RevertCommand implements Command {

  private String migrationToRevertTo = null;
  private Receiver receiver;

  public RevertCommand(Receiver receiver, String migrationToRevertTo) {
    this.receiver = receiver;
    this.migrationToRevertTo = migrationToRevertTo;
  }

  public RevertCommand() {}

  @Override
  public void execute() {
    receiver.revert(migrationToRevertTo);
  }
}