package com.batavia.orm.generator;

import static org.junit.Assert.assertEquals;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;
import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  @Test
  public void tesRunSqlScriptGeneratorToFile_CreateTable() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext createTable = SqlCommandContext.CREATE_TABLE;
    mainGenerator.runSqlScriptGeneratorToFile(createTable, null);
    String upExpectedScriptContent =
      "CREATE TABLE users (\n" + "\tid INT\n" + ");" + "\n\n";

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
  public void tesRunSqlScriptGeneratorToFile_DropTable() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));

    GeneratorMain mainGenerator = new GeneratorMain(
      table,
      upMigrationFilePath,
      downMigrationFilePath
    );

    SqlCommandContext dropTable = SqlCommandContext.DROP_TABLE;
    mainGenerator.runSqlScriptGeneratorToFile(dropTable, null);
    String expectedScriptContent = "DROP TABLE users;" + "\n\n";

    String fileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    String downExpectedScriptContent =
      "CREATE TABLE users (\n" + "\tid INT\n" + ");" + "\n\n";

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downMigrationFilePath))
    );

    assertEquals(expectedScriptContent, fileContent);
    assertEquals(downExpectedScriptContent, downFileContent);
  }

  @Test
  public void tesRunSqlScriptGeneratorToFile_AlterTableAddColumn()
    throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("id", "INT", true, true);
    Column column2 = new Column("name", "VARCHAR(200)", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    Column[] columnsToAdd = new Column[2];
    columnsToAdd[0] = column1;
    columnsToAdd[1] = column2;

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
  public void tesRunSqlScriptGeneratorToFile_AlterTableDropColumn()
    throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("id", "INT", true, true);
    Column column2 = new Column("name", "VARCHAR(200)", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    Column[] columnsToDrop = new Column[2];
    columnsToDrop[0] = column1;
    columnsToDrop[1] = column2;

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
}
