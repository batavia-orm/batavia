package com.batavia.orm.generator;

import static org.junit.Assert.assertEquals;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;
import org.junit.jupiter.api.*;

public class SqlScriptGeneratorTest {

  @Test
  public void testCreateTableSqlScriptGenerator() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));
    CreateTableSqlScriptGenerator scriptGenerator = new CreateTableSqlScriptGenerator();
    String script = scriptGenerator.generateSqlScript(table);
    String expectedScript =
      "CREATE TABLE users (\n" + "\tid INT\n" + ");" + "\n";

    assertEquals(expectedScript, script);
  }

  @Test
  public void testDropTableSqlScriptGenerator() throws Exception {
    Table table = new Table("users");
    DropTableSqlScriptGenerator scriptGenerator = new DropTableSqlScriptGenerator();
    String script = scriptGenerator.generateSqlScript(table);
    String expectedScript = "DROP TABLE users;" + "\n";

    assertEquals(expectedScript, script);
  }

  @Test
  public void testAlterTableSqlScriptGenerator_addColumn() throws Exception {
    Table table = new Table("users");
    Column[] columnsToAdd = new Column[2];
    columnsToAdd[0] = new Column("email", "VARCHAR(200)", false, false);
    columnsToAdd[1] = new Column("age", "INT", false, false);
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    AlterTableSqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columnsToAdd,
      alterTableContext
    );
    String expectedScript =
      "ALTER TABLE users\n" +
      "ADD COLUMN email VARCHAR(200),\n" +
      "ADD COLUMN age INT;" +
      "\n";

    assertEquals(expectedScript, script);
  }

  @Test
  public void testAlterTableSqlScriptGenerator_dropColumn() throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("email", "VARCHAR(200)", false, false);
    Column column2 = new Column("age", "INT", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    Column[] columnsToDrop = new Column[2];
    columnsToDrop[0] = column1;
    columnsToDrop[1] = column2;

    AlterTableContext alterTableContext = AlterTableContext.DROP_COLUMN;
    AlterTableSqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columnsToDrop,
      alterTableContext
    );
    String expectedScript =
      "ALTER TABLE users\n" +
      "DROP COLUMN email,\n" +
      "DROP COLUMN age;" +
      "\n";

    assertEquals(expectedScript, script);
  }
}
