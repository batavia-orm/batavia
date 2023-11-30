package com.batavia.orm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
      String currentFileContent = new String(Files.readAllBytes(Paths.get(filePath)));
      String newContent = currentFileContent + migrationScript;
      Files.write(Paths.get(filePath), newContent.getBytes());
      System.out.println("Successfully wrote to the down migration file.");
    } catch (IOException e) {
      throw new IOException("File path not found!");
    }
  }
}
