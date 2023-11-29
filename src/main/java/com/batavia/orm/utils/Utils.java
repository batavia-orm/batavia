package com.batavia.orm.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Utils {

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

    public static void writeToMigrationFile(String fileName, String migrationScript) {
        String MIGRATION_DIR="migrations"; // will change to dotenv
        String timestamp = Long.toString(System.currentTimeMillis());

        // optional file name set default
        if (fileName.length() == 0) {
            fileName="migration-filename";
        }

        // Format: timestamp-filename.sql (e.g. 167493743947-create-table-employees.sql)
        String filePath = String.format("%s/%s-%s.sql",MIGRATION_DIR, timestamp, fileName);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(migrationScript);
            System.out.println("File created: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}