package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerator.AlterTableSqlScriptGenerator;
import com.batavia.orm.utils.Utils;

public class AlterTableMigrationFileGenerator implements MigrationFileGenerator {
    private String commandType;

    public AlterTableMigrationFileGenerator(String commandType) {
        this.commandType = commandType;
    }

    @Override
    public void generateMigrationFile(Table table, String fileName, Column[] columns) {
        AlterTableSqlScriptGenerator alterTableSqlScriptGenerator = new AlterTableSqlScriptGenerator();

        String migrationScript = getScriptAccordingToCommandType(alterTableSqlScriptGenerator, table, columns);

        Utils.writeToMigrationFile(fileName, migrationScript);
    }

    private String getScriptAccordingToCommandType(AlterTableSqlScriptGenerator alterTableSqlScriptGenerator,
            Table table, Column[] columnNames) {

        String script = "";
        switch (this.commandType) {
            case "addColumn":
                script = alterTableSqlScriptGenerator.generateAddColumnScript(table, columnNames);
                break;
            case "dropColumn":
                script = alterTableSqlScriptGenerator.generateDropColumnScript(table, columnNames);
                break;

        }

        return script;
    }
}
