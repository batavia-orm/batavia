package com.batavia.orm.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;

import java.util.ArrayList;
import org.junit.jupiter.api.*;

public class SqlScriptGeneratorTest {

  // CREATE TABLE
  @Test
  public void testCreateTableSqlScriptGenerator_1() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));
    ISqlScriptGenerator scriptGenerator = new CreateTableSqlScriptGenerator();
    String script = scriptGenerator.generateSqlScript(table);
    String expectedScript =
      "CREATE TABLE users (\n" + "\tid INT PRIMARY KEY\n" + ");" + "\n\n";

    assertEquals(expectedScript, script);
  }

  @Test
  public void testCreateTableSqlScriptGenerator_2() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));
    table.addColumn(new Column("name", "VARCHAR(255)", false, false));
    ISqlScriptGenerator scriptGenerator = new CreateTableSqlScriptGenerator();
    String script = scriptGenerator.generateSqlScript(table);
    String expectedScript =
      "CREATE TABLE users (\n" +
      "\tname VARCHAR(255),\n" +
      "\tid INT PRIMARY KEY\n" +
      ");" +
      "\n\n";

    assertEquals(expectedScript, script);
  }

  @Test
  public void testCreateTableSqlScriptGenerator_3() throws Exception {
    Table table = new Table("users");
    table.addColumn(new Column("id", "INT", true, true));
    ArrayList<Column> columns = new ArrayList<Column>();
    ISqlScriptGenerator scriptGenerator = new CreateTableSqlScriptGenerator();

    assertThrows(
      UnsupportedOperationException.class,
      () -> {
        scriptGenerator.generateSqlScript(
          table,
          columns,
          AlterTableContext.NONE
        );
      }
    );
  }

  // DROP TABLE
  @Test
  public void testDropTableSqlScriptGenerator_4() throws Exception {
    Table table = new Table("users");
    ISqlScriptGenerator scriptGenerator = new DropTableSqlScriptGenerator();
    String script = scriptGenerator.generateSqlScript(table);
    String expectedScript = "DROP TABLE users;" + "\n\n";

    assertEquals(expectedScript, script);
  }

  @Test
  public void testDropTableSqlScriptGenerator_5() throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();
    ISqlScriptGenerator scriptGenerator = new DropTableSqlScriptGenerator();

    assertThrows(
      UnsupportedOperationException.class,
      () -> {
        scriptGenerator.generateSqlScript(
          table,
          columns,
          AlterTableContext.NONE
        );
      }
    );
  }

  // ALTER TABLE
  @Test // alter table add column
  public void testAlterTableSqlScriptGenerator_6() throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columnsToAdd = new ArrayList<Column>();
    columnsToAdd.add(new Column("email", "VARCHAR(255)", false, false));
    columnsToAdd.add(new Column("age", "INT", false, false));
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columnsToAdd,
      alterTableContext
    );

    String expectedScript =
      "ALTER TABLE users\n" +
      "ADD COLUMN email VARCHAR(255),\n" +
      "ADD COLUMN age INT;" +
      "\n\n";

    assertEquals(expectedScript, script);
  }

  @Test // alter table drop column
  public void testAlterTableSqlScriptGenerator_7() throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("email", "VARCHAR(255)", false, false);
    Column column2 = new Column("age", "INT", false, false);
    table.addColumn(column1);
    table.addColumn(column2);

    ArrayList<Column> columnsToDrop = new ArrayList<Column>();
    columnsToDrop.add(column1);
    columnsToDrop.add(column2);

    AlterTableContext alterTableContext = AlterTableContext.DROP_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columnsToDrop,
      alterTableContext
    );

    String expectedScript =
      "ALTER TABLE users\n" +
      "DROP COLUMN email,\n" +
      "DROP COLUMN age;" +
      "\n\n";

    assertEquals(expectedScript, script);
  }

  @Test // unsupported alter table context
  public void testAlterTableSqlScriptGenerator_8() throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("email", "VARCHAR(255)", false, false);
    Column column2 = new Column("age", "INT", false, false);
    ArrayList<Column> columns = new ArrayList<Column>();
    columns.add(column1);
    columns.add(column2);

    AlterTableContext alterTableContext = AlterTableContext.NONE;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    assertThrows(
      UnsupportedOperationException.class,
      () -> {
        scriptGenerator.generateSqlScript(table, columns, alterTableContext);
      }
    );
  }

  @Test // alter table context not provided
  public void testAlterTableSqlScriptGenerator_9() throws Exception {
    Table table = new Table("users");
    Column column1 = new Column("email", "VARCHAR(255)", false, false);
    Column column2 = new Column("age", "INT", false, false);
    ArrayList<Column> columns = new ArrayList<Column>();
    columns.add(column1);
    columns.add(column2);

    AlterTableContext alterTableContext = AlterTableContext.OTHERS;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columns,
      alterTableContext
    );

    assertEquals(script, "");
  }

  @Test // unsupported interface function
  public void testAlterTableSqlScriptGenerator_10() throws Exception {
    Table table = new Table("users");
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    assertThrows(
      UnsupportedOperationException.class,
      () -> {
        scriptGenerator.generateSqlScript(table);
      }
    );
  }

  @Test // null table null columns
  public void testAlterTableSqlScriptGenerator_11() throws Exception {
    Table table = null;
    ArrayList<Column> columns = null;
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columns,
      alterTableContext
    );

    assertEquals(script, "");
  }

  @Test // null table non empty columns
  public void testAlterTableSqlScriptGenerator_12() throws Exception {
    Table table = null;
    Column column1 = new Column("email", "VARCHAR(255)", false, false);
    ArrayList<Column> columns = new ArrayList<Column>();
    columns.add(column1);
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columns,
      alterTableContext
    );

    assertEquals(script, "");
  }

  @Test // columns null but table not null
  public void testAlterTableSqlScriptGenerator_13() throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = null;
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columns,
      alterTableContext
    );

    assertEquals(script, "");
  }

  @Test // null table empty columns
  public void testAlterTableSqlScriptGenerator_14() throws Exception {
    Table table = null;
    ArrayList<Column> columns = new ArrayList<Column>();
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columns,
      alterTableContext
    );

    assertEquals(script, "");
  }

  @Test // non null table and empty columns
  public void testAlterTableSqlScriptGenerator_15() throws Exception {
    Table table = new Table("users");
    ArrayList<Column> columns = new ArrayList<Column>();
    AlterTableContext alterTableContext = AlterTableContext.ADD_COLUMN;
    ISqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

    String script = scriptGenerator.generateSqlScript(
      table,
      columns,
      alterTableContext
    );

    assertEquals(script, "");
  }
}
