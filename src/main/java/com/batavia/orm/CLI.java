package com.batavia.orm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;
import com.batavia.orm.cli.RevertCommand;

public class CLI {
    private final BufferedReader reader;
    // Constructor for dependency injection
    public CLI(BufferedReader reader, List<Command> commands) {
        this.reader = reader;
    }

    public void startCLI() {
        while (true) {
            try {
                System.out.print("Please provide a command: ");
                String userInput = reader.readLine();

                if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting...");
                    break;
                }

                String[] args = userInput.split("\\s+");

                // Check for exit command again
                if (args.length == 1 && (args[0].equalsIgnoreCase("exit") || args[0].equalsIgnoreCase("quit"))) {
                    System.out.println("Exiting...");
                    break;
                }

                Command command = parseCommand(args);

                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("Unknown command. Type 'help' for available commands.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Command parseCommand(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: No command provided.");
            return null;
        }

        String command = args[0].toLowerCase();
        switch (command) {
            case "generate":
                if (args.length < 2) {
                    System.out.println("Usage: --generate-migration <migration-filename>");
                    return null;
                } else {
                    return new GenerateMigrationCommand(args[1]);
                }
            case "migrate":
                return new MigrateCommand();
            case "revert":
                return new RevertCommand();
            case "show":
                return new ShowMigrationsCommand();
            case "help":
                printUsage();
                return null;
            default:
                System.out.println("Unknown command: " + command);
                printUsage();
                return null;
        }
    }

    private void printUsage() {
        System.out.println("Available commands: ");
        System.out.println("  generate <migration-filename>: Generate a migration");
        System.out.println("  migrate: Migrate");
        System.out.println("  revert: Revert");
        System.out.println("  show: Show migrations");
        System.out.println("  help: Display available commands");
        System.out.println("  exit/quit: Exit the CLI");
    }

    public static void main(String[] args) {
        // Usage in main method with actual dependencies
        CLI cli = new CLI(new BufferedReader(new InputStreamReader(System.in)),
        Arrays.asList(new GenerateMigrationCommand(""), new MigrateCommand(), new RevertCommand(), new ShowMigrationsCommand()));
        cli.startCLI();
    }
}
