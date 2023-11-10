package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerator.DropTableSqlScriptGenerator;
import com.batavia.orm.utils.*;

public class DropTableMigrationFileGenerator implements MigrationFileGenerator {

    @Override
    public void generateMigrationFile(Table table, String fileName, Column[] columns) {
        DropTableSqlScriptGenerator dropTableSqlScriptGenerator = new DropTableSqlScriptGenerator();
        String migrationScript = dropTableSqlScriptGenerator.generateDropTableScript(table);
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
