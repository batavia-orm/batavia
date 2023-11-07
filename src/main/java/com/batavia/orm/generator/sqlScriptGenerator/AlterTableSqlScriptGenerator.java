package com.batavia.orm.generator.sqlScriptGenerator;

import java.util.HashMap;

public class AlterTableSqlScriptGenerator {
    
    public String generateAddColumnScript(String tableName, HashMap<String, String> columns) {
        String script = "";

        String alterTable = String.format("ALTER TABLE %s ", tableName);
        String columnName = columns.get("dummy_column_name");
        //String columnDataType = columns.get("dummy_column_name").get("data_type");
        String columnDataType = "VARCHAR (500)";

        String addColumns = String.format("ADD %s %s", columnName, columnDataType);

        script = String.format("%s %s;", alterTable, addColumns);

        return script;
    }

    public String generateDropColumnScript(String tableName){
         String script = "";

         return script;
    }
}
