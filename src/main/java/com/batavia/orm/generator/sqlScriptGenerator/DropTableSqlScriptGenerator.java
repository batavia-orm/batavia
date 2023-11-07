package com.batavia.orm.generator.sqlScriptGenerator;

import java.util.HashMap;

public class DropTableSqlScriptGenerator {

    public String generateDropTableScript(String tableName,  HashMap<String, String> columns) {
        String script = String.format("DROP TABLE %s ", tableName);

        return script;
    }
}
