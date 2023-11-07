package com.batavia.orm.generator.sqlScriptGenerator;

// tables = HashMap<String, Table> 
// tables.columns = HashMap<String, Column>

public class CreateTableSqlScriptGenerator {

    public String generateCreateTableScript(String tableName, String[] columns) {
        String script = "";
        int attributesLength = columns.length;
        String createTable = String.format("CREATE TABLE %s ", tableName);
        String tableAttributes = "(";

        for (int i = 0; i < attributesLength; i++) {
            if (i == attributesLength - 1) {
                 tableAttributes += columns[i];
            }
            tableAttributes += columns[i] + ",\n";
        }

        tableAttributes += ")";
        script = String.format("%s %s;", createTable, tableAttributes);

        return script;
    }
}