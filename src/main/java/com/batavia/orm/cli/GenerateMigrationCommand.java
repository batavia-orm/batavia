package com.batavia.orm.cli;

import com.batavia.orm.comparator.Comparator;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateMigrationCommand implements Command {

  private String migrationFilename;
  private static final Dotenv dotenv = Dotenv.load();
  private static final String DATASOURCE_DIR = dotenv.get("DATASOURCE_DIR");
  private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");

  // Constructor for generation with filename
  public GenerateMigrationCommand(String migrationFilename) {
    this.migrationFilename = migrationFilename;
  }

  @Override
  public void execute() {
    try {
      String stampedFilename = this.generateTimestampedMigrationFilename(
        migrationFilename
      );
      String ANSI_RESET = "\u001B[0m";
      String ANSI_GREEN = "\u001B[32m";
      String ANSI_YELLOW = "\u001B[33m";
      System.out.println("\nGenerating migration: " + ANSI_YELLOW + stampedFilename + ANSI_RESET + "\n");
      Comparator comparator = new Comparator(
        MIGRATIONS_DIR,
        DATASOURCE_DIR
      );
      comparator.run(stampedFilename);
      System.out.println("\n" + ANSI_GREEN + "Migration file generated successfully" + ANSI_RESET);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }

  private String generateTimestampedMigrationFilename(String migration_name) {
    LocalDateTime timestamp = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd_HHmmss"
    );
    String formattedTimestamp = timestamp.format(formatter);

    return formattedTimestamp + "_" + migration_name;
  }
}
