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
  private static Command command;

  public CLI(BufferedReader reader, Map<String, Command> commandMap) {
    this.reader = reader;
    this.commandMap = commandMap;
  }

  public CLI(BufferedReader reader) {
    this.reader = reader;

    Receiver receiver = new Receiver();

    // Put all the commands in the hashmap
    commandMap.put("generate", new GenerateMigrationCommand(receiver, "automatic"));
    commandMap.put("migrate", new MigrateCommand(receiver));
    commandMap.put("revert", new RevertCommand(receiver, "previous-migration-filename"));
    commandMap.put("show", new ShowMigrationsCommand(receiver));
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
        } else if(userInput.equalsIgnoreCase("help")){
          printUsage();
        }

        command = commandMap.get(userInput);
        
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
    if (args.length > 0) {
        String command = args[0];
        if (cli.commandMap.containsKey(command)) {
            cli.commandMap.get(command).execute();
        } else {
            System.out.println("Invalid command. Please use 'help' to get a list of valid commands.");
        }
    } else {
        cli.startCLI();
    }
}
}