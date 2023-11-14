package com.batavia.orm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

  public static void writeToUpMigrationFile(
    String filePath,
    String migrationScript
  ) {
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))
    ) {
      writer.write(migrationScript + "\n");
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.err.println(
        "An error occurred while writing to the file: " + e.getMessage()
      );
    }
  }

  public static void writeToDownMigrationFile(
    String filePath,
    String migrationScript
  ) {
    try {
      String currentFileContent = new String(
        Files.readAllBytes(Paths.get(filePath))
      );
      String newContent = currentFileContent + migrationScript + "\n";
      Files.write(Paths.get(filePath), newContent.getBytes());
    } catch (IOException e) {
      System.err.println(
        "An error occurred while writing to the file: " + e.getMessage()
      );
    }
  }
}
