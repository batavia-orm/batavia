package com.batavia.orm.cli;

import com.batavia.orm.comparator.ComparatorMain;
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
      System.out.println("Generating migration: " + stampedFilename);
      ComparatorMain comparatorMain = new ComparatorMain(
        MIGRATIONS_DIR,
        DATASOURCE_DIR
      );
      comparatorMain.main(stampedFilename);
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
