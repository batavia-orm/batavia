package com.batavia.orm.generator;

import com.batavia.orm.generator.sqlScriptGenerator.CreateTableSqlScriptGenerator;
import com.batavia.orm.utils.Utils;

public class CreateTableMigrationGenerator implements MigrationGenerator {
    @Override
    public void generateMigration(String tableName) {
        // Implement the logic to generate CREATE TABLE migration
        CreateTableSqlScriptGenerator dropTableSqlScriptGenerator = new CreateTableSqlScriptGenerator();
        String migrationScript = dropTableSqlScriptGenerator.generateCreateTableScript(tableName, null);

        // write to file
        String fileName = "create-table-"+tableName;
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
