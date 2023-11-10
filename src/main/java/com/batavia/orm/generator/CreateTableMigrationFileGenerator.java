package com.batavia.orm.generator;

import com.batavia.orm.generator.sqlScriptGenerator.CreateTableSqlScriptGenerator;
import com.batavia.orm.commons.*;
import com.batavia.orm.utils.Utils;

public class CreateTableMigrationFileGenerator implements MigrationFileGenerator {
    @Override
    public void generateMigrationFile(Table table, String fileName, Column[] columns) {
        CreateTableSqlScriptGenerator createTableSqlScriptGenerator = new CreateTableSqlScriptGenerator();
        String migrationScript = createTableSqlScriptGenerator.generateCreateTableScript(table);
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
