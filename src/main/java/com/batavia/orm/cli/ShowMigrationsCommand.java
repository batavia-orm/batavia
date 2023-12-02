package com.batavia.orm.cli;

// Concrete command for showing migrations
public class ShowMigrationsCommand implements Command {
    private Receiver receiver;

    public ShowMigrationsCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        System.out.println("Showing migrations...");
    }
}