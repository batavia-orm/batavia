package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;

public interface ISqlScriptGenerator {
    public String generateSqlScript(Table table);

    public String generateSqlScript(Table table, Column[] columns, AlterTableContext alterType) throws Exception;
}