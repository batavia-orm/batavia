package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;

public class DropTableSqlScriptGenerator implements ISqlScriptGenerator {

    @Override
    public String generateSqlScript(Table table) {
        String dropTableScript = String.format("DROP TABLE %s;", table.getTableName());
        return dropTableScript.toString();
    }

    @Override
    public String generateSqlScript(Table table, Column[] columns, AlterTableCategory alterType) {
        throw new UnsupportedOperationException("Unimplemented method parameters for this object");
    }
}
