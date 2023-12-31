package com.batavia.orm.cli;

import com.batavia.orm.adapters.Config;
import com.batavia.orm.adapters.Database;
import com.batavia.orm.comparator.Comparator;
import com.batavia.orm.reverter.MigrationReverter;
import com.batavia.orm.runner.MigrationRunner;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receiver {

  private Config bataviaConfig = Config.getConfig();
  private String DATASOURCE_DIR = bataviaConfig.getDatasourceDir();
  private String MIGRATIONS_DIR = bataviaConfig.getMigrationsDir();
  private String DATABASE_URL = bataviaConfig.getDatabaseURL();
  private Database database = Database.getDatabase();
  private String migrationToRevertTo = null;

  public void generateMigration(String migrationFilename) {
    try {
      String stampedFilename =
        this.generateTimestampedMigrationFilename(migrationFilename);
      String ANSI_RESET = "\u001B[0m";
      String ANSI_GREEN = "\u001B[32m";
      String ANSI_YELLOW = "\u001B[33m";
      System.out.println(
        "\nGenerating migration: " +
        ANSI_YELLOW +
        stampedFilename +
        ANSI_RESET +
        "\n"
      );
      Comparator comparator = new Comparator(
        MIGRATIONS_DIR,
        DATASOURCE_DIR
      );
      comparator.run(stampedFilename);
      System.out.println(
        "\n" + ANSI_GREEN + "Migration file generated successfully" + ANSI_RESET
      );
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

  public void migrate() {
    System.out.println("\nRunning migrations...\n");
    String ANSI_RESET = "\u001B[0m";
    String ANSI_GREEN = "\u001B[32m";

    try (Connection connection = database.getConnection();) {
      MigrationRunner migrationRunner = new MigrationRunner(
        MIGRATIONS_DIR,
        connection
      );
      migrationRunner.migrate();
      System.out.println(
        "\n" +
        ANSI_GREEN +
        "Migrations applied. Your database is now in sync with your schema" +
        ANSI_RESET
      );
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void revert(String previousMigrationFilename) {
    System.out.println("\nReverting...\n");
    String ANSI_RESET = "\u001B[0m";
    String ANSI_GREEN = "\u001B[32m";

    try (Connection connection = database.getConnection();) {
      MigrationReverter migrationReverter = new MigrationReverter(
        MIGRATIONS_DIR,
        connection
      );
      if (migrationToRevertTo == null) {
        migrationReverter.revert();
        System.out.println(
          "\n" + ANSI_GREEN + "Reverted applied migrations" + ANSI_RESET
        );
      } else {
        migrationReverter.revert(migrationToRevertTo);
        System.out.println(
          "\n" + ANSI_GREEN + "Reverted applied migrations" + ANSI_RESET
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void showMigrations() {
    System.out.println("Showing migrations...");
  }
}
