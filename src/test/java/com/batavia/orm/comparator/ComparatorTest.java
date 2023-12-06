package com.batavia.orm.comparator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.batavia.orm.commons.Table;

public class ComparatorTest {
  
  private Path upSQLPath;
  private Path downSQLPath;

  @TempDir
  public static Path tempDir;

  @BeforeEach
  private void setup() throws IOException {
    upSQLPath = Files.createFile(tempDir.resolve("test.sql"));
    downSQLPath = Files.createFile(tempDir.resolve("test.down.sql"));
  }
  
  @Test
  public void Should_Be_Able_To_Create_Table_When_Does_Not_Exists() throws IOException {
    HashMap<String, Table> localTables = new HashMap<String, Table>();
    HashMap<String, Table> remoteTables = new HashMap<String, Table>();

    Table tableA = new Table("table_a");
    Table tableB = new Table("table_b");
    
    localTables.put("table_a", tableA);
    localTables.put("table_b", tableB);
    remoteTables.put("table_a", tableA);

    Comparator comparator = new Comparator(tempDir.toString(), ".");
    comparator.comp(localTables, remoteTables, "test");


    String upSQLContent = Files.readString(upSQLPath);
    String downSQLContent = Files.readString(downSQLPath);

    assertEquals(upSQLContent, "CREATE TABLE table_b (\n\n);\n\n");
    assertEquals(downSQLContent, "DROP TABLE table_b;\n\n");
  }
}
