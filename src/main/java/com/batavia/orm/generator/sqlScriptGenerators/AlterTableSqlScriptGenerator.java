package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;

public class AlterTableSqlScriptGenerator implements ISqlScriptGenerator {

    public String generateSqlScript (
        Table table,
        Column[] columns,
        AlterTableCategory alterType
    ) {
        String script = "";
        
        if (alterType == null) {
            return script;
        }

        script = alterType.getScriptAccordingToAlterType(table, columns);

        return script;
    }

    @Override
    public String generateSqlScript(Table table) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSqlScript'");
    }

    public void generateMigrationFile(Table table, String fileName, Column[] columns) {
    }

}
