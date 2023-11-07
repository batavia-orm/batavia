package com.batavia.orm.generator;

import com.batavia.orm.generator.sqlScriptGenerator.AlterTableSqlScriptGenerator;
import com.batavia.orm.utils.Utils;

public class AddColumnMigrationGenerator implements MigrationGenerator {
    @Override
    public void generateMigration(String tableName) {
        // Implement the logic to generate ADD COLUMN migration script
 
        AlterTableSqlScriptGenerator dropTableSqlScriptGenerator = new AlterTableSqlScriptGenerator();
        String migrationScript = dropTableSqlScriptGenerator.generateAddColumnScript(tableName, null);

        // write to file
        String columnName = "dummycolumn";
        String fileName = "alter-table-"+tableName+"-add-column-"+columnName;
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}