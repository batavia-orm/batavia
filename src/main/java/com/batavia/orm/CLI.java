package com.batavia.orm;
import com.batavia.orm.comparator.ComparatorMain;

public class CLI {
    public static void proccessCommands(String[] args){
        if(args.length == 0){
            System.out.println("Please provide a command.");
            return;
        }
        // TODO: parse the command from scanner class

        String command = args[0];
        if(command == null){
            System.out.println("Invalid command.");
            return;
        }

        // Initialize necessary components
        ComparatorMain comparator = new ComparatorMain();

        switch (command) {
            case "generate-migration":
                if (args.length < 2) {
                    System.out.println("Usage: batavia generate-migration <migration-filename>");
                    return;
                }
                String migrationFilename = args[1];
                break;
            case "migrate":
                break;
            case "show-migrations":
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }
}