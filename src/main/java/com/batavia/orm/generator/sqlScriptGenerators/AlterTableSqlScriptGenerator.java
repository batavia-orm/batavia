package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;

public class AlterTableSqlScriptGenerator {

    public String generateAlterTableScript(
        Table table,
        Column[] columns,
        AlterTableCategory alterType
    ) throws Exception {
        
        if (alterType == null) {
            throw new Exception("Invalid alter table sql command");
        }

        String script = "";

        script = alterType.getScriptAccordingToAlterType(table, columns);

        return script;
    }

}
