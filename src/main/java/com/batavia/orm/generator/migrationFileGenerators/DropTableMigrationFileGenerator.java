package com.batavia.orm.generator.migrationFileGenerators;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.DropTableSqlScriptGenerator;
import com.batavia.orm.utils.*;

public class DropTableMigrationFileGenerator implements IMigrationFileGenerator {
    private DropTableSqlScriptGenerator dropTableSqlScriptGenerator;

    public DropTableMigrationFileGenerator() {
        this.dropTableSqlScriptGenerator = new DropTableSqlScriptGenerator();
    }

    @Override
    public void generateMigrationFile(
        Table table,
        String fileName,
        Column[] columns
    ) {
        String migrationScript = this.dropTableSqlScriptGenerator.generateDropTableScript(table);
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
