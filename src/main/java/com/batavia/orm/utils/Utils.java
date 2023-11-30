package com.batavia.orm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

  private Utils() {
    throw new AssertionError(
        "Cannot instantiate the Utils class. Use its static methods.");
  }

  public static String camelCaseToSnakeCase(String camelCaseString) {
    StringBuilder snakeCaseString = new StringBuilder();
    for (int i = 0; i < camelCaseString.length(); i++) {
      char c = camelCaseString.charAt(i);
      if (i != 0 && Character.isUpperCase(c)) {
        snakeCaseString.append("_");
        snakeCaseString.append(Character.toLowerCase(c));
      } else {
        snakeCaseString.append(Character.toLowerCase(c));
      }
    }
    return snakeCaseString.toString();
  }

  public static String pascalCaseToSnakeCase(String camelCaseString) {
    StringBuilder snakeCaseString = new StringBuilder();
    for (int i = 0; i < camelCaseString.length(); i++) {
      char c = camelCaseString.charAt(i);
      if (i != 0 && Character.isUpperCase(c)) {
        snakeCaseString.append("_");
        snakeCaseString.append(Character.toLowerCase(c));
      } else {
        snakeCaseString.append(Character.toLowerCase(c));
      }
    }
    return snakeCaseString.toString();
  }

  public static void writeToUpMigrationFile(
      String filePath,
      String migrationScript) throws IOException {
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(migrationScript);
      System.out.println("Successfully wrote to the up migration file.");
    } catch (IOException e) {
      throw new IOException("File path not found!");
    }
  }

  public static void writeToDownMigrationFile(
      String filePath,
      String migrationScript) throws IOException {
    try {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        String currentFileContent = new String(Files.readAllBytes(path));
        String newContent = currentFileContent + migrationScript;
        Files.write(path, newContent.getBytes());

        System.out.println("Successfully wrote to the down migration file.");
    } catch (IOException e) {
        throw new IOException("File path not found!");
    }
  }

  public static String generateTimestampedMigrationFilename(String migration_name) {
    LocalDateTime timestamp = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
    String formattedTimestamp = timestamp.format(formatter);

    if (migration_name == null) {
        migration_name = "auto";
    }

    return formattedTimestamp + "_" + migration_name;
  }
}
