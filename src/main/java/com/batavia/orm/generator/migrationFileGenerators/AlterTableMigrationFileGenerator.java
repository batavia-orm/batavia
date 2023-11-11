package com.batavia.orm.generator.migrationFileGenerators;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableSqlScriptGenerator;
import com.batavia.orm.utils.Utils;

public class AlterTableMigrationFileGenerator implements IMigrationFileGenerator {
    private AlterTableSqlScriptGenerator alterTableSqlScriptGenerator;
    private AlterTableCategory alterType;

    public AlterTableMigrationFileGenerator(AlterTableCategory alterType) {
        this.alterTableSqlScriptGenerator = new AlterTableSqlScriptGenerator();
        this.alterType = alterType;
    }

    @Override
    public void generateMigrationFile(
        Table table,
        String fileName,
        Column[] columns
    ) {
        String migrationScript = "";
        try {
            migrationScript = alterTableSqlScriptGenerator.generateAlterTableScript(table, columns, alterType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.writeToMigrationFile(fileName, migrationScript);
    }
}
