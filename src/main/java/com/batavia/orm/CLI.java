package com.batavia.orm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;

public class CLI {
    public static void main(String[] args) {
        System.out.println("Welcome to Batavia ORM");
        if (args.length == 0) {
            // If no command-line arguments are provided, start an interactive session.
            startCLI();
        } else {
            // Parse and execute the provided command-line arguments.
            parseCommand(args);
        }
    }

    private static void startCLI() {
        // make instance from the scanner class instead of the buferred reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.print("Please provide a command: ");
                String userInput = reader.readLine();
    
                if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting...");
                    break;
                }
    
                // Parse user input and create the appropriate command
                String[] args = userInput.split("\\s+");
                Command command = parseCommand(args);
    
                // Execute the command
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("Unknown command.");
                }
    
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static Command parseCommand(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: No command provided.");
            return null;
        }
    
        String command = args[0].toLowerCase();
        switch (command) {
            case "--generate-migration":
                if (args.length < 2) {
                    System.out.println("Usage: --generate-migration <migration-filename>");
                    return null;
                } else {
                    return new GenerateMigrationCommand(args[1]);
                }
            case "--migrate":
                return new MigrateCommand();
            case "--show-migrations":
                return new ShowMigrationsCommand();
            default:
                System.out.println("Unknown command: " + command);
                printUsage();
                return null;
        }
    }

    private static void printUsage() {
        System.out.println("Usage: ");
        System.out.println("  --generate-migration <migration-filename>: Generate a migration");
        System.out.println("  --migrate: Migrate");
        System.out.println("  --show-migrations: Show migrations");
    }
}
