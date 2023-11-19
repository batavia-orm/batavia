package com.batavia.orm.generator;

import static org.junit.Assert.assertEquals;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;
import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.jupiter.api.*;

public class GeneratorMainTest {

  private static final Dotenv dotenv = Dotenv
    .configure()
    .directory("C:\\Users\\alvin\\OneDrive\\Desktop\\batavia\\batavia\\.env")
    .load();

  private static final String upMigrationFilePath = dotenv.get(
    "UP_MIGRATION_PATH"
  );

  private static final String downMigrationFilePath = dotenv.get(
    "DOWN_MIGRATION_PATH"
  );

  @BeforeEach
  public void clearFile() {
    try {
      Files.write(Paths.get(upMigrationFilePath), new byte[0]);
      Files.write(Paths.get(downMigrationFilePath), new byte[0]);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test // null sql command null alter table context
  public void tesRunSqlScriptGeneratorToFileNullParameters_1()
    throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    mainGenerator.runSqlScriptGeneratorToFile(null, null);

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test // null sql command non null alter table context
  public void tesRunSqlScriptGeneratorToFileNullSqlCommand_2()
    throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    mainGenerator.runSqlScriptGeneratorToFile(
      null,
      AlterTableContext.ADD_COLUMN
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test // non-null sql command null alter table context
  public void tesRunSqlScriptGeneratorToFileNullAlterType_3() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    mainGenerator.runSqlScriptGeneratorToFile(
      SqlCommandContext.CREATE_TABLE,
      null
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileCreateTable_4() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext createTable = SqlCommandContext.CREATE_TABLE;
    AlterTableContext alterTableContext = AlterTableContext.NONE;
    mainGenerator.runSqlScriptGeneratorToFile(createTable, alterTableContext);
    String upExpectedScriptContent =
      "CREATE TABLE users (\n" + "\tid INT PRIMARY KEY\n" + ");" + "\n\n";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "DROP TABLE users;\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileDropTable_5() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext dropTable = SqlCommandContext.DROP_TABLE;
    AlterTableContext alterTableContext = AlterTableContext.NONE;
    mainGenerator.runSqlScriptGeneratorToFile(dropTable, alterTableContext);
    String expectedScriptContent = "DROP TABLE users;" + "\n\n";

    String fileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent =
      "CREATE TABLE users (\n" + "\tid INT PRIMARY KEY\n" + ");" + "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(expectedScriptContent, fileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileAlterTableAddColumn_6()
    throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("id", "INT", true, true);
    Column column2 = new Column("name", "VARCHAR(200)", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    ArrayList<Column> columnsToAdd = new ArrayList<Column>();
    columnsToAdd.add(column1);
    columnsToAdd.add(column2);

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columnsToAdd,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext addColumn = AlterTableContext.ADD_COLUMN;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, addColumn);
    String upExpectedScriptContent =
      "ALTER TABLE users\n" +
      "ADD COLUMN id INT,\n" +
      "ADD COLUMN name VARCHAR(200);" +
      "\n\n";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent =
      "ALTER TABLE users\n" +
      "DROP COLUMN id,\n" +
      "DROP COLUMN name;" +
      "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileAlterTableDropColumn_7()
    throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("id", "INT", true, true);
    Column column2 = new Column("name", "VARCHAR(200)", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    ArrayList<Column> columnsToDrop = new ArrayList<Column>();
    columnsToDrop.add(column1);
    columnsToDrop.add(column2);

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columnsToDrop,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext dropColumn = AlterTableContext.DROP_COLUMN;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, dropColumn);

    String upExpectedScriptContent =
      "ALTER TABLE users\n" +
      "DROP COLUMN id,\n" +
      "DROP COLUMN name;" +
      "\n\n";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent =
      "ALTER TABLE users\n" +
      "ADD COLUMN id INT,\n" +
      "ADD COLUMN name VARCHAR(200);" +
      "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileAlterTableNone_8()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext none = AlterTableContext.NONE;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, none);

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileAlterTableOthers_9()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext alterTable = SqlCommandContext.ALTER_TABLE;
    AlterTableContext alterTableContext = AlterTableContext.OTHERS;

    mainGenerator.runSqlScriptGeneratorToFile(alterTable, alterTableContext);

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileSqlCommandOthers_10()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext otherSqlCommand = SqlCommandContext.OTHERS;
    AlterTableContext alterTableContext = AlterTableContext.NONE;

    mainGenerator.runSqlScriptGeneratorToFile(
      otherSqlCommand,
      alterTableContext
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFileSqlCommandNone_11()
    throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      columns,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext otherSqlCommand = SqlCommandContext.NONE;
    AlterTableContext alterTableContext = AlterTableContext.NONE;

    mainGenerator.runSqlScriptGeneratorToFile(
      otherSqlCommand,
      alterTableContext
    );

    String upExpectedScriptContent = "";

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent = "";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(upExpectedScriptContent, upFileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }
}
