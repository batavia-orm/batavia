package com.batavia.orm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;
import com.batavia.orm.cli.RevertCommand;

public class CLI {
    private final BufferedReader reader;
    private final GenerateMigrationCommand generateMigrationCommand;
    private final MigrateCommand migrateCommand;
    private final RevertCommand revertCommand;
    private final ShowMigrationsCommand showMigrationsCommand;

    public CLI(BufferedReader reader, GenerateMigrationCommand generateMigrationCommand, MigrateCommand migrateCommand, RevertCommand revertCommand, ShowMigrationsCommand showMigrationsCommand) {
        this.reader = reader;
        this.generateMigrationCommand = generateMigrationCommand;
        this.migrateCommand = migrateCommand;
        this.revertCommand = revertCommand;
        this.showMigrationsCommand = showMigrationsCommand;
    }

    public void startCLI() {
        boolean continueRunning = true;
        while (continueRunning) {
            try {
                System.out.print("Please provide a command: ");
                String userInput = reader.readLine();

                if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit") || userInput.trim().isEmpty()) {
                    System.out.println("Exiting...");
                    break;
                }

                String[] args = userInput.split("\\s+");

                Command command = parseCommand(args);

                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("Unknown command. Type 'help' for available commands.");
                }

            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
                continueRunning = false;
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
                if (args.length == 1) {
                    return generateMigrationCommand;
                } else if (args.length == 2) {
                    return new GenerateMigrationCommand(args[1]);
                } else {
                    System.out.println("Usage: generate OR generate <migration-filename>");
                    return null;
                }
            case "migrate":
                return migrateCommand;
            case "revert":
                if (args.length == 1) {
                    return revertCommand;
                } else if (args.length == 2) {
                    return new RevertCommand(args[1]);
                } else {
                    System.out.println("Usage: revert OR revert <previous-migration-filename>");
                    return null;
                }
            case "show":
                return showMigrationsCommand;
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
        GenerateMigrationCommand generateMigrationCommand = new GenerateMigrationCommand("automatic");
        MigrateCommand migrateCommand = new MigrateCommand();
        RevertCommand revertCommand = new RevertCommand();
        ShowMigrationsCommand showMigrationsCommand = new ShowMigrationsCommand();

        CLI cli = new CLI(new BufferedReader(new InputStreamReader(System.in)), generateMigrationCommand, migrateCommand, revertCommand, showMigrationsCommand);
        
        // Check if there are command-line arguments
        if (args.length > 0) {
            // Use the first argument as the command
            String command = args[0].toLowerCase();

            // Check if the provided command is "migrate"
            if ("migrate".equals(command)) {
                migrateCommand.execute();
                return; 
            } else if("revert".equals(command)){
                if (args.length == 1) {
                    revertCommand.execute(); 
                    return;
                } else if (args.length == 2) {
                    new RevertCommand(args[1]).execute(); 
                    return;
                } else {
                    System.out.println("Usage: generate OR generate <migration-filename>");
                    return;
                }
            } else if("show".equals(command)){
                showMigrationsCommand.execute();
                return;
            } else if ("generate".equals(command)) {
                // Check for "generate" command variations
                if (args.length == 1) {
                    generateMigrationCommand.execute(); 
                    return;
                } else if (args.length == 2) {
                    new GenerateMigrationCommand(args[1]).execute(); 
                    return;
                } else {
                    System.out.println("Usage: generate OR generate <migration-filename>");
                    return;
                }
            }
        }
        cli.startCLI();
    }
}