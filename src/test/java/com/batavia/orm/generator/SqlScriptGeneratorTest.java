package com.batavia.orm.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;
import com.batavia.orm.generator.sqlScriptGenerators.*;

public class SqlScriptGeneratorTest {

    @Test
    public void testCreateTableSqlScriptGenerator() throws Exception {
        Table table = new Table("users");
        table.addColumn(new Column("id", "INT", true, true));
        CreateTableSqlScriptGenerator scriptGenerator = new CreateTableSqlScriptGenerator();
        String script = scriptGenerator.generateCreateTableScript(table);
        String expectedScript = "CREATE TABLE users (\n" +
                "\tid INT\n" +
                ");";

        Assertions.assertEquals(expectedScript, script);
    }

    @Test
    public void testDropTableSqlScriptGenerator() throws Exception {
        Table table = new Table("users");
        DropTableSqlScriptGenerator scriptGenerator = new DropTableSqlScriptGenerator();
        String script = scriptGenerator.generateDropTableScript(table);
        String expectedScript = "DROP TABLE users;";

        Assertions.assertEquals(expectedScript, script);
    }

    @Test
    public void testAlterTableSqlScriptGenerator_addColumn() throws Exception {
        Table table = new Table("users");
        Column[] columnsToAdd = new Column[2];
        columnsToAdd[0] = new Column("email", "VARCHAR(200)", false, false);
        columnsToAdd[1] = new Column("age", "INT", false, false);
        AlterTableCategory alterTableCategory = AlterTableCategory.ADD_COLUMN;
        AlterTableSqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

        String script = scriptGenerator.generateAlterTableScript(table, columnsToAdd, alterTableCategory);
        String expectedScript = "ALTER TABLE users\n" +
                "ADD COLUMN email VARCHAR(200),\n" +
                "ADD COLUMN age INT;";

        Assertions.assertEquals(expectedScript, script);
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

        AlterTableCategory alterTableCategory = AlterTableCategory.DROP_COLUMN;
        AlterTableSqlScriptGenerator scriptGenerator = new AlterTableSqlScriptGenerator();

        String script = scriptGenerator.generateAlterTableScript(table, columnsToDrop, alterTableCategory);
        String expectedScript = "ALTER TABLE users\n" +
                "DROP COLUMN email,\n" +
                "DROP COLUMN age;";

        Assertions.assertEquals(expectedScript, script);
    }
}
