package com.batavia.orm.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    public static void writeToMigrationFile(String fileName, String migrationScript) {
        String MIGRATION_DIR="migrations"; // will change to dotenv
        String timestamp = Long.toString(System.currentTimeMillis());

        // optional file name set default
        if (fileName.length() == 0) {
            fileName="migration-filename";
        }

        // Format: timestamp-filename.sql (e.g. 167493743947-create-table-employees.sql)
        String migrationFileName = String.format("src\\main\\java\\com\\batavia\\orm\\%s\\%s-%s.sql",MIGRATION_DIR, timestamp, fileName);

        try (FileWriter writer = new FileWriter(migrationFileName)) {
            writer.write(migrationScript);
            System.out.println("File created: " + migrationFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}