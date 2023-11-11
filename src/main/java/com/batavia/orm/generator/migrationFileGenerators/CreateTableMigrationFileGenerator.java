package com.batavia.orm.generator.migrationFileGenerators;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.CreateTableSqlScriptGenerator;
import com.batavia.orm.utils.Utils;

public class CreateTableMigrationFileGenerator implements IMigrationFileGenerator {
    private CreateTableSqlScriptGenerator createTableSqlScriptGenerator;

    public CreateTableMigrationFileGenerator() {
        this.createTableSqlScriptGenerator = new CreateTableSqlScriptGenerator();
    }

    @Override
    public void generateMigrationFile(
        Table table,
        String fileName,
        Column[] columns
    ) {
        String migrationScript = this.createTableSqlScriptGenerator.generateCreateTableScript(table);
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
