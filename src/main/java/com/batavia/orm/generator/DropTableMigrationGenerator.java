package com.batavia.orm.generator;

import com.batavia.orm.generator.sqlScriptGenerator.DropTableSqlScriptGenerator;
import com.batavia.orm.utils.*;

public class DropTableMigrationGenerator implements MigrationGenerator {
    @Override
    public void generateMigration(String tableName) {
        // Implement the logic to generate DROP TABLE migration
        DropTableSqlScriptGenerator dropTableSqlScriptGenerator = new DropTableSqlScriptGenerator();
        String migrationScript = dropTableSqlScriptGenerator.generateDropTableScript(tableName, null);

        // write to file
        String fileName = "drop-table-"+tableName;
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
