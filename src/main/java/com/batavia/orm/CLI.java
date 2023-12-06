package com.batavia.orm;

import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.Receiver;
import com.batavia.orm.cli.RevertCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CLI {

  private BufferedReader reader;
  private Map<String, Command> commandMap = new HashMap<>();

  private GenerateMigrationCommand generateMigrationCommand;
  private MigrateCommand migrateCommand;
  private RevertCommand revertCommand;
  private ShowMigrationsCommand showMigrationsCommand;

  public CLI(BufferedReader reader, Map<String, Command> commandMap) {
    this.reader = reader;
    this.commandMap = commandMap;
  }

  public CLI(BufferedReader reader) {
    this.reader = reader;

    // Create a receiver
    Receiver receiver = new Receiver();

    // Initialize commands
    this.generateMigrationCommand =
      new GenerateMigrationCommand(receiver, "automatic");
    this.migrateCommand = new MigrateCommand(receiver);
    this.revertCommand =
      new RevertCommand(receiver, "previous-migration-filename");
    this.showMigrationsCommand = new ShowMigrationsCommand(receiver);

    // Put all the commands in the hashmap
    commandMap.put("generate", this.generateMigrationCommand);
    commandMap.put("migrate", this.migrateCommand);
    commandMap.put("revert", this.revertCommand);
    commandMap.put("show", this.showMigrationsCommand);
  }

  public void startCLI() {
    boolean continueRunning = true;
    while (continueRunning) {
      try {
        System.out.print("Please provide a command: ");
        String userInput = reader.readLine();

        if (
          userInput.equalsIgnoreCase("exit") ||
          userInput.equalsIgnoreCase("quit") ||
          userInput.trim().isEmpty()
        ) {
          System.out.println("Exiting...");
          break;
        }

        String[] args = userInput.split("\\s+");

        Command command = parseCommand(args);

        if (command != null) {
          command.execute();
        } else {
          System.out.println(
            "Unknown command. Type 'help' for available commands."
          );
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

    String commandName = args[0].toLowerCase();
    Command command = commandMap.get(commandName);

    if (command == null) {
      System.out.println("Unknown command: " + commandName);
      printUsage();
    }

    return command;
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
    BufferedReader reader = new BufferedReader(
      new InputStreamReader(System.in)
    );
    CLI cli = new CLI(reader);
    cli.startCLI();
  }
}