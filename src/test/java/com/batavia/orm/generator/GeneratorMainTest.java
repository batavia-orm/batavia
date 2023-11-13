package com.batavia.orm.generator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;

public class GeneratorMainTest {

    @BeforeEach
    public void clearFile() {
        String migrationFilePath = "src\\main\\java\\com\\batavia\\orm\\migrations\\dummy-migration-test.sql";

        try {
            Files.write(Paths.get(migrationFilePath), new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tesRunSqlScriptGeneratorToFile_CreateTable() throws Exception {
        Table table = new Table("users");
        table.addColumn(new Column("id", "INT", true, true));
        String migrationFilePath = "src\\main\\java\\com\\batavia\\orm\\migrations\\dummy-migration-test.sql";

        GeneratorMain mainGenerator = new GeneratorMain(table, migrationFilePath);
        SqlCommandCategory createTable = SqlCommandCategory.CREATE_TABLE;
        mainGenerator.runSqlScriptGeneratorToFile(createTable, null);
        String expectedScriptContent = "CREATE TABLE users (\n" +
                "\tid INT\n" +
                ");";

        String fileContent = new String(Files.readAllBytes(Paths.get(migrationFilePath)));

        // assert if the file contains the script;
        assertEquals(expectedScriptContent, fileContent);
    }

    @Test
    public void tesRunSqlScriptGeneratorToFile_DropTable() throws Exception {
        Table table = new Table("users");
        table.addColumn(new Column("id", "INT", true, true));
        String migrationFilePath = "src\\main\\java\\com\\batavia\\orm\\migrations\\dummy-migration-test.sql";

        GeneratorMain mainGenerator = new GeneratorMain(table, migrationFilePath);
        SqlCommandCategory dropTable = SqlCommandCategory.DROP_TABLE;
        mainGenerator.runSqlScriptGeneratorToFile(dropTable, null);
        String expectedScriptContent = "DROP TABLE users;";

        String fileContent = new String(Files.readAllBytes(Paths.get(migrationFilePath)));

        // assert if the file contains the script;
        assertEquals(expectedScriptContent, fileContent);
    }

    @Test
    public void tesRunSqlScriptGeneratorToFile_AlterTableAddColumn() throws Exception {
        Table table = new Table("users");
        Column column1 = new Column("id", "INT", true, true);
        Column column2 = new Column("name", "VARCHAR(200)", false, false);

        Column[] columnsToAdd = new Column[2];
        columnsToAdd[0] = column1;
        columnsToAdd[1] = column2;

        String migrationFilePath = "src\\main\\java\\com\\batavia\\orm\\migrations\\dummy-migration-test.sql";

        GeneratorMain mainGenerator = new GeneratorMain(table, columnsToAdd, migrationFilePath);
        SqlCommandCategory alterTable = SqlCommandCategory.ALTER_TABLE;
        AlterTableCategory addColumn = AlterTableCategory.ADD_COLUMN;
        mainGenerator.runSqlScriptGeneratorToFile(alterTable, addColumn);
        String expectedScriptContent = "ALTER TABLE users\n" +
                "ADD COLUMN id INT,\n" +
                "ADD COLUMN name VARCHAR(200);";

        String fileContent = new String(Files.readAllBytes(Paths.get(migrationFilePath)));

        // assert if the file contains the script;
        assertEquals(expectedScriptContent, fileContent);
    }

    @Test
    public void tesRunSqlScriptGeneratorToFile_AlterTableDropColumn() throws Exception {
        Table table = new Table("users");
        Column column1 = new Column("id", "INT", true, true);
        Column column2 = new Column("name", "VARCHAR(200)", false, false);
        table.addColumn(column1);
        table.addColumn(column2);

        Column[] columnsToDrop = new Column[2];
        columnsToDrop[0] = column1;
        columnsToDrop[1] = column2;

        String migrationFilePath = "src\\main\\java\\com\\batavia\\orm\\migrations\\dummy-migration-test.sql";

        GeneratorMain mainGenerator = new GeneratorMain(table, columnsToDrop, migrationFilePath);
        SqlCommandCategory alterTable = SqlCommandCategory.ALTER_TABLE;
        AlterTableCategory dropColumn = AlterTableCategory.DROP_COLUMN;
        mainGenerator.runSqlScriptGeneratorToFile(alterTable, dropColumn);
        String expectedScriptContent = "ALTER TABLE users\n" +
                "DROP COLUMN id,\n" +
                "DROP COLUMN name;";

        String fileContent = new String(Files.readAllBytes(Paths.get(migrationFilePath)));

        // assert if the file contains the script;
        assertEquals(expectedScriptContent, fileContent);
    }
}
