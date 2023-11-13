package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;

public enum SqlCommandCategory {
    CREATE_TABLE,
    DROP_TABLE,
    ALTER_TABLE,
    OTHERS;

    public String runSqlScriptGenerator(
            Table table,
            Column[] columns,
            AlterTableCategory alterType) {
        String script = "";
        if (this == CREATE_TABLE) {
            CreateTableSqlScriptGenerator generator = new CreateTableSqlScriptGenerator();
            script = generator.generateSqlScript(table);
        } else if (this == DROP_TABLE) {
            DropTableSqlScriptGenerator dropTableMigration = new DropTableSqlScriptGenerator();
            script = dropTableMigration.generateSqlScript(table);
        } else if (this == ALTER_TABLE) {
            AlterTableSqlScriptGenerator alterTableMigration = new AlterTableSqlScriptGenerator();
            script = alterTableMigration.generateSqlScript(table, columns, alterType);
        } else if (this == OTHERS) {
            System.out.println("This sql command is not yet being supported");
        }

        return script;
    }
}