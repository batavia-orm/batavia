package com.batavia.orm.generator;

import static org.junit.Assert.assertEquals;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

public class GeneratorMainTest {

  @TempDir
  private static Path tempMockMigrationDir;

  @BeforeEach
  void clearFile() {
    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    try {
      Files.write(upMigrationFilePath, new byte[0]);
      Files.write(downMigrationFilePath, new byte[0]);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test // null sql command null alter table context
  public void testRunSqlScriptGeneratorToFileNullParameters_1()
    throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(table, upPath, downPath);

    mainGenerator.runSqlScriptGeneratorToFile(null, null);

    String upExpectedScriptContent = "";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test // null sql command non null alter table context
  public void testRunSqlScriptGeneratorToFileNullSqlCommand_2()
    throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(table, upPath, downPath);

    mainGenerator.runSqlScriptGeneratorToFile(
      null,
      AlterTableContext.ADD_COLUMN
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test // non-null sql command null alter table context
  public void testRunSqlScriptGeneratorToFileNullAlterType_3() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(table, upPath, downPath);

    mainGenerator.runSqlScriptGeneratorToFile(
      SqlCommandContext.CREATE_TABLE,
      null
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileCreateTable_4() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(table, upPath, downPath);

    SqlCommandContext createTable = SqlCommandContext.CREATE_TABLE;
    AlterTableContext alterTableContext = AlterTableContext.NONE;
    mainGenerator.runSqlScriptGeneratorToFile(createTable, alterTableContext);
    String upExpectedScriptContent =
      "CREATE TABLE users (\n" + "\tid INT PRIMARY KEY\n" + ");" + "\n\n";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent = "DROP TABLE users;\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileDropTable_5() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(table, upPath, downPath);

    SqlCommandContext dropTable = SqlCommandContext.DROP_TABLE;
    AlterTableContext alterTableContext = AlterTableContext.NONE;
    mainGenerator.runSqlScriptGeneratorToFile(dropTable, alterTableContext);
    String expectedScriptContent = "DROP TABLE users;" + "\n\n";

    String fileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent =
      "CREATE TABLE users (\n" + "\tid INT PRIMARY KEY\n" + ");" + "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(expectedScriptContent, fileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileAlterTableAddColumn_6()
    throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("id", "INT", true, true);
    Column column2 = new Column("name", "VARCHAR(255)",  false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    ArrayList<Column> columnsToAdd = new ArrayList<Column>();
    columnsToAdd.add(column1);
    columnsToAdd.add(column2);

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columnsToAdd,
      upPath,
      downPath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext addColumn = AlterTableContext.ADD_COLUMN;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, addColumn);
    String upExpectedScriptContent =
      "ALTER TABLE users\n" +
      "ADD COLUMN id INT,\n" +
      "ADD COLUMN name VARCHAR(255);" +
      "\n\n";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent =
      "ALTER TABLE users\n" +
      "DROP COLUMN id,\n" +
      "DROP COLUMN name;" +
      "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileAlterTableDropColumn_7()
    throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("id", "INT", true, true);
    Column column2 = new Column("name", "VARCHAR(255)", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    ArrayList<Column> columnsToDrop = new ArrayList<Column>();
    columnsToDrop.add(column1);
    columnsToDrop.add(column2);

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columnsToDrop,
      upPath,
      downPath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext dropColumn = AlterTableContext.DROP_COLUMN;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, dropColumn);

    String upExpectedScriptContent =
      "ALTER TABLE users\n" +
      "DROP COLUMN id,\n" +
      "DROP COLUMN name;" +
      "\n\n";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent =
      "ALTER TABLE users\n" +
      "ADD COLUMN id INT,\n" +
      "ADD COLUMN name VARCHAR(255);" +
      "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileAlterTableNone_8()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upPath,
      downPath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext none = AlterTableContext.NONE;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, none);

    String upExpectedScriptContent = "";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileAlterTableOthers_9()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upPath,
      downPath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext alterTableContext = AlterTableContext.OTHERS;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, alterTableContext);

    String upExpectedScriptContent = "";

    String upFileContent = new String(Files.readAllBytes(Paths.get(upPath)));

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileSqlCommandOthers_10()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    
    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upPath,
      downPath
    );

    SqlCommandContext otherSqlCommand = SqlCommandContext.OTHERS;
    AlterTableContext alterTableContext = AlterTableContext.NONE;

    mainGenerator.runSqlScriptGeneratorToFile(
      otherSqlCommand,
      alterTableContext
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upPath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void testRunSqlScriptGeneratorToFileSqlCommandNone_11()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    
    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();
    String downPath = downMigrationFilePath.toString();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upPath,
      downPath
    );

    SqlCommandContext otherSqlCommand = SqlCommandContext.NONE;
    AlterTableContext alterTableContext = AlterTableContext.NONE;

    mainGenerator.runSqlScriptGeneratorToFile(
      otherSqlCommand,
      alterTableContext
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upPath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }
}
