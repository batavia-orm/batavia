package com.batavia.orm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

  private Utils() {
    throw new AssertionError(
      "Cannot instantiate the Utils class. Use its static methods."
    );
  }

  public static void writeToUpMigrationFile(
    String filePath,
    String migrationScript
  ) throws IOException {
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))
    ) {
      writer.write(migrationScript);
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      throw new IOException("File path not found!");
    }
  }

  public static void writeToDownMigrationFile(
    String filePath,
    String migrationScript
  ) throws IOException {
    try {
      String currentFileContent = new String(
        Files.readAllBytes(Paths.get(filePath))
      );
      String newContent = currentFileContent + migrationScript;
      Files.write(Paths.get(filePath), newContent.getBytes());
    } catch (IOException e) {
      throw new IOException("File path not found!");
    }
  }
}
