package com.batavia.orm.generator.sqlScriptGenerator;

import java.util.HashMap;

import com.batavia.orm.commons.*;

public class AlterTableSqlScriptGenerator {

    public String generateAddColumnScript(Table table, Column[] columns) {
        /**
         * FORMAT:
         * ALTER TABLE table_name
         * ADD COLUMN column_name1 column_type1,
         * ....
         * ADD COLUMN column_nameN column_typeN;
         */

        String tableName = table.getTableName();

        StringBuilder alterTableAddColumnsBuilder = new StringBuilder(
                String.format("ALTER TABLE %s\n", tableName));

        int numberOfColumnsToAdd = columns.length;

        for (int i = 0; i < numberOfColumnsToAdd; i++) {
            alterTableAddColumnsBuilder.append(
                    String.format("ADD COLUMN %s %s", columns[i].getColumnName(), columns[i].getColumnType()));

            if (i < numberOfColumnsToAdd - 1) {
                alterTableAddColumnsBuilder.append(",\n");
            }
        }

        alterTableAddColumnsBuilder.append(';');

        return alterTableAddColumnsBuilder.toString();
    }

    public String generateDropColumnScript(Table table, Column[] columns) {
        /**
         * FORMAT:
         * ALTER TABLE table_name
         * DROP COLUMN column_name1,
         * ....
         * DROP COLUMN column_nameN;
         */

        String tableName = table.getTableName();
        HashMap<String, Column> tableColumns = table.getColumns();

        int numberOfColumnsToDrop = columns.length;
        StringBuilder alterTableDropColumnsBuilder = new StringBuilder(
                String.format("ALTER TABLE %s\n", tableName));

        for (int i = 0; i < numberOfColumnsToDrop; i++) {
            Column column = tableColumns.get(columns[i].getColumnName());

            alterTableDropColumnsBuilder.append(
                    String.format("DROP COLUMN %s", column.getColumnName()));

            if (i < numberOfColumnsToDrop - 1) {
                alterTableDropColumnsBuilder.append(",\n");
            }
        }

        alterTableDropColumnsBuilder.append(';');

        return alterTableDropColumnsBuilder.toString();
    }
}
