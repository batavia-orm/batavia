package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;

interface ISqlScriptGenerator {
    public String generateSqlScript(Table table);

    public String generateSqlScript(Table table, Column[] columns, AlterTableCategory alterType) throws Exception;
}