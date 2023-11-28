package com.batavia.orm.cli;

// Concrete command for showing migrations
public class ShowMigrationsCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Showing migrations...");
        // Implement the logic for the "show-migrations" command.
    }
}