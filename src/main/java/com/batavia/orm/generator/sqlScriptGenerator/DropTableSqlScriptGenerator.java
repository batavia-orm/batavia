package com.batavia.orm.generator.sqlScriptGenerator;

import com.batavia.orm.commons.Table;

public class DropTableSqlScriptGenerator {

    public String generateDropTableScript(Table table) {
        String dropTableScript = String.format("DROP TABLE %s;", table.getTableName());
        return dropTableScript.toString();
    }
}
