package com.batavia.orm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    public static void writeToMigrationFile(String filePath, String migrationScript) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(migrationScript);
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}