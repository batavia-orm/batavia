package com.batavia.orm.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;

public class DataSourceScannerTest {
  @TempDir
  private Path testJavaFilesDir;

  @Test
  public void Test_Case_1()
      throws IOException {
    Path fileA = testJavaFilesDir.resolve("EntityClassA.java");
    String contentA = "import com.batavia.orm.annotations.Entity;\n" +
        "\n" +
        "@Entity\n" +
        "public class EntityClassA {}";
    Files.write(fileA, contentA.getBytes());
    
    DataSourceScanner scanner = new DataSourceScanner(testJavaFilesDir.toString());
    HashMap<String, Table> tables = scanner.findAllEntities();

    assertEquals(1, tables.size());

    Table entityA = tables.get("EntityClassA");
    assertEquals("EntityClassA", entityA.getTableName());
  }
  
  @Test
  public void Test_Case_2()
      throws IOException {
    Path fileA = testJavaFilesDir.resolve("EntityClassA.java");
    String contentA = "import com.batavia.orm.annotations.Entity;\n" +
        "import com.batavia.orm.annotations.EntityColumn;\n" + 
        "\n" +
        "@Entity\n" +
        "public class EntityClassA {\n" +
        "  private String fieldA;\n" +
        "}";
    Files.write(fileA, contentA.getBytes());
    
    DataSourceScanner scanner = new DataSourceScanner(testJavaFilesDir.toString());
    HashMap<String, Table> tables = scanner.findAllEntities();

    assertEquals(1, tables.size());

    Table entityA = tables.get("EntityClassA");
    assertEquals("EntityClassA", entityA.getTableName());

    assertEquals(0, entityA.getColumns().size());
  }

  @Test
  public void Test_Case_3()
      throws IOException {
    Path fileA = testJavaFilesDir.resolve("EntityClassA.java");
    String contentA = "import com.batavia.orm.annotations.Entity;\n" +
        "import com.batavia.orm.annotations.EntityColumn;\n" + 
        "\n" +
        "@Entity\n" +
        "public class EntityClassA {\n" +
        "  @EntityColumn\n" +
        "  private String fieldA;\n" +
        "}";
    Files.write(fileA, contentA.getBytes());
    
    DataSourceScanner scanner = new DataSourceScanner(testJavaFilesDir.toString());
    HashMap<String, Table> tables = scanner.findAllEntities();

    assertEquals(1, tables.size());

    Table entityA = tables.get("EntityClassA");
    assertEquals("EntityClassA", entityA.getTableName());

    Column fieldA = entityA.getColumns().get("fieldA");
    assertEquals(1, entityA.getColumns().size());
    assertEquals("fieldA", fieldA.getColumnName());
    assertEquals("VARCHAR(255)", fieldA.getColumnType());
    assertEquals(false, fieldA.isPrimary());
    assertEquals(false, fieldA.isUnique());
  }

  @Test
  public void Test_Case_4()
      throws IOException {
    Path fileA = testJavaFilesDir.resolve("EntityClassA.java");
    String contentA = "import com.batavia.orm.annotations.Entity;\n" +
        "import com.batavia.orm.annotations.EntityColumn;\n" + 
        "\n" +
        "@Entity\n" +
        "public class EntityClassA {\n" +
        "  @EntityColumn\n" +
        "  @PrimaryColumn\n" +
        "  private Integer fieldA;\n" +
        "}";
    Files.write(fileA, contentA.getBytes());
    
    DataSourceScanner scanner = new DataSourceScanner(testJavaFilesDir.toString());
    HashMap<String, Table> tables = scanner.findAllEntities();

    assertEquals(1, tables.size());

    Table entityA = tables.get("EntityClassA");
    assertEquals("EntityClassA", entityA.getTableName());

    Column fieldA = entityA.getColumns().get("fieldA");
    assertEquals(1, entityA.getColumns().size());
    assertEquals("fieldA", fieldA.getColumnName());
    assertEquals("INT", fieldA.getColumnType());
    assertEquals(true, fieldA.isPrimary());
    assertEquals(true, fieldA.isUnique());
  }

  @Test
  public void Test_Case_5()
      throws IOException {
    Path fileA = testJavaFilesDir.resolve("EntityClassA.java");
    String contentA = "import com.batavia.orm.annotations.Entity;\n" +
        "import com.batavia.orm.annotations.EntityColumn;\n" + 
        "\n" +
        "@Entity\n" +
        "public class EntityClassA {\n" +
        "  @EntityColumn\n" +
        "  @Unique\n" +
        "  private Integer fieldA;\n" +
        "}";
    Files.write(fileA, contentA.getBytes());
    
    DataSourceScanner scanner = new DataSourceScanner(testJavaFilesDir.toString());
    HashMap<String, Table> tables = scanner.findAllEntities();

    assertEquals(1, tables.size());

    Table entityA = tables.get("EntityClassA");
    assertEquals("EntityClassA", entityA.getTableName());

    Column fieldA = entityA.getColumns().get("fieldA");
    assertEquals(1, entityA.getColumns().size());
    assertEquals("fieldA", fieldA.getColumnName());
    assertEquals("INT", fieldA.getColumnType());
    assertEquals(false, fieldA.isPrimary());
    assertEquals(true, fieldA.isUnique());
  }

  @Test
  public void Test_Case_6()
      throws IOException {
    Path fileA = testJavaFilesDir.resolve("EntityClassA.java");
    String contentA = "public class EntityClassA {}";
    Files.write(fileA, contentA.getBytes());
    
    DataSourceScanner scanner = new DataSourceScanner(testJavaFilesDir.toString());
    HashMap<String, Table> tables = scanner.findAllEntities();

    assertEquals(0, tables.size());
  }
}
