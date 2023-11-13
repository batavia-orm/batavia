package com.batavia.orm.generator.sqlScriptGenerators;

import java.util.HashMap;
import java.util.Map;

import com.batavia.orm.commons.*;

public class CreateTableSqlScriptGenerator implements ISqlScriptGenerator {

    /*
    * FORMAT:
    * CREATE TABLE table_name (
    *     column_name1 column_type1,
    *     ...
    *     column_nameN column_typeN
    * );
    */

    @Override
    public String generateSqlScript(Table table) {
        HashMap<String, Column> tableColumns = table.getColumns();
        String tableName = table.getTableName();

        StringBuilder createTableScriptBuilder = new StringBuilder(
                String.format("CREATE TABLE %s (\n", tableName));

        int mapIndex = 0;
        for (Map.Entry<String, Column> entry : tableColumns.entrySet()) {
            Column column = entry.getValue();
            String columnAttributes = "\t" + column.getColumnName() + " " + column.getColumnType();

            createTableScriptBuilder.append(
                    String.format("%s", columnAttributes));

            if (mapIndex < tableColumns.size() - 1) {
                createTableScriptBuilder.append(",\n");
            }

            mapIndex++;
        }

        createTableScriptBuilder.append("\n);");

        return createTableScriptBuilder.toString();
    }

    @Override
    public String generateSqlScript(Table table, Column[] columns, AlterTableCategory alterType) {
        throw new UnsupportedOperationException("Unimplemented method parameters for this object");
    }
}