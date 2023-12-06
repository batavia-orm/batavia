package com.batavia.orm.comparator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;


    // Comparator comparator = new Comparator("/Users/enryleinhard/Projects/eclipse/exampleProject/src/migrations", ".");
    // comparator.comp(localTables, remoteTables, "comparatorTest");


    // String upSQLContent = Files.readString(Path.of("/Users/enryleinhard/Projects/eclipse/exampleProject/src/migrations/comparatorTest.sql"));
    // String downSQLContent = Files.readString(Path.of("/Users/enryleinhard/Projects/eclipse/exampleProject/src/migrations/comparatorTest.down.sql"));


public class ComparatorTest {
  @Test
  public void Should_Generate_Create_Table_When_Local_Exists_Remote_Not_Exists(@TempDir Path tempDir) throws IOException {
    Path upSQLPath = Files.createFile(tempDir.resolve("comparatorTest.sql"));
    Path downSQLPath = Files.createFile(tempDir.resolve("comparatorTest.down.sql"));

    HashMap<String, Table> localTables = new HashMap<String, Table>();
    HashMap<String, Table> remoteTables = new HashMap<String, Table>();

    Table tableA = new Table("table_a");
    Table tableB = new Table("table_b");
    
    localTables.put("table_a", tableA);
    localTables.put("table_b", tableB);
    remoteTables.put("table_a", tableA);

    Comparator comparator = new Comparator(tempDir.toString(), ".");
    comparator.comp(localTables, remoteTables, "comparatorTest");


    String upSQLContent = Files.readString(upSQLPath);
    String downSQLContent = Files.readString(downSQLPath);

    assertEquals(upSQLContent, "CREATE TABLE table_b (\n\n);\n\n");
    assertEquals(downSQLContent, "DROP TABLE table_b;\n\n");
  }

  @Test
  public void Should_Generate_Drop_Table_When_Local_Not_Exists_Remote_Exists(@TempDir Path tempDir) throws IOException {
    Path upSQLPath = Files.createFile(tempDir.resolve("comparatorTest.sql"));
    Path downSQLPath = Files.createFile(tempDir.resolve("comparatorTest.down.sql"));

    HashMap<String, Table> localTables = new HashMap<String, Table>();
    HashMap<String, Table> remoteTables = new HashMap<String, Table>();

    Table tableA = new Table("table_a");
    Table tableB = new Table("table_b");
    
    localTables.put("table_a", tableA);
    remoteTables.put("table_a", tableA);
    remoteTables.put("table_b", tableB);

    Comparator comparator = new Comparator(tempDir.toString(), ".");
    comparator.comp(localTables, remoteTables, "comparatorTest");


    String upSQLContent = Files.readString(upSQLPath);
    String downSQLContent = Files.readString(downSQLPath);

    assertEquals(upSQLContent, "DROP TABLE table_b;\n\n");
    assertEquals(downSQLContent, "CREATE TABLE table_b (\n\n);\n\n");
  }

  @Test
  public void Should_Generate_Create_Column_When_Local_Exists_Remote_Not_Exists(@TempDir Path tempDir) throws IOException {
    Path upSQLPath = Files.createFile(tempDir.resolve("comparatorTest.sql"));
    Path downSQLPath = Files.createFile(tempDir.resolve("comparatorTest.down.sql"));

    HashMap<String, Table> localTables = new HashMap<String, Table>();
    HashMap<String, Table> remoteTables = new HashMap<String, Table>();

    Table tableA = new Table("table_a");
    Column columnA = new Column("col_a", "String");
    tableA.addColumn(columnA);

    Table remoteTableA = new Table("table_a");

    localTables.put("table_a", tableA);
    remoteTables.put("table_a", remoteTableA);

    Comparator comparator = new Comparator(tempDir.toString(), ".");
    comparator.comp(localTables, remoteTables, "comparatorTest");


    String upSQLContent = Files.readString(upSQLPath);
    String downSQLContent = Files.readString(downSQLPath);

    assertEquals(upSQLContent, "ALTER TABLE table_a\n" + "ADD COLUMN col_a VARCHAR;\n\n");
    assertEquals(downSQLContent, "ALTER TABLE table_a\n" + "DROP COLUMN col_a;\n\n");
  }

  @Test
  public void Should_Generate_Drop_Column_When_Local_Not_Exists_Remote_Exists(@TempDir Path tempDir) throws IOException {
    Path upSQLPath = Files.createFile(tempDir.resolve("comparatorTest.sql"));
    Path downSQLPath = Files.createFile(tempDir.resolve("comparatorTest.down.sql"));

    HashMap<String, Table> localTables = new HashMap<String, Table>();
    HashMap<String, Table> remoteTables = new HashMap<String, Table>();

    Table tableA = new Table("table_a");

    Table remoteTableA = new Table("table_a");
    Column columnA = new Column("col_a", "String");
    remoteTableA.addColumn(columnA);


    localTables.put("table_a", tableA);
    remoteTables.put("table_a", remoteTableA);

    Comparator comparator = new Comparator(tempDir.toString(), ".");
    comparator.comp(localTables, remoteTables, "comparatorTest");


    String upSQLContent = Files.readString(upSQLPath);
    String downSQLContent = Files.readString(downSQLPath);

    assertEquals(upSQLContent, "ALTER TABLE table_a\n" + "DROP COLUMN col_a;\n\n");
    assertEquals(downSQLContent, "ALTER TABLE table_a\n" + "ADD COLUMN col_a VARCHAR;\n\n");
  }
}
