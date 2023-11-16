package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;

import java.util.ArrayList;
import java.util.HashMap;

public enum AlterTableContext {
  ADD_COLUMN,
  DROP_COLUMN,
  NONE;

  public String getScriptAccordingToAlterType(Table table, ArrayList<Column> columns) {
    String script = "";

    if (this == ADD_COLUMN) {
      script = generateAddColumnScript(table, columns);
    } else if (this == DROP_COLUMN) {
      script = generateDropColumnScript(table, columns);
    }

    return script;
  }

  private static String generateAddColumnScript(Table table, ArrayList<Column> columns) {
    /**
     * FORMAT:
     * ALTER TABLE table_name
     * ADD COLUMN column_name1 column_type1,
     * ....
     * ADD COLUMN column_nameN column_typeN;
     */

    String tableName = table.getTableName();

    StringBuilder alterTableAddColumnsBuilder = new StringBuilder(
      String.format("ALTER TABLE %s\n", tableName)
    );

    int numberOfColumnsToAdd = columns.size();
    for (int i = 0; i < numberOfColumnsToAdd; i++) {
      try {
        alterTableAddColumnsBuilder.append(
          String.format(
            "ADD COLUMN %s %s",
            columns.get(i).getColumnName(),
            columns.get(i).getColumnType()
          )
        );

        if (i < numberOfColumnsToAdd - 1) {
          alterTableAddColumnsBuilder.append(",\n");
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    alterTableAddColumnsBuilder.append(';');

    return alterTableAddColumnsBuilder.toString();
  }

  private static String generateDropColumnScript(
    Table table,
    ArrayList<Column> columns
  ) {
    /**
     * FORMAT:
     * ALTER TABLE table_name
     * DROP COLUMN column_name1,
     * ....
     * DROP COLUMN column_nameN;
     */

    String tableName = table.getTableName();
    HashMap<String, Column> tableColumns = table.getColumns();

    int numberOfColumnsToDrop = columns.size();
    StringBuilder alterTableDropColumnsBuilder = new StringBuilder(
      String.format("ALTER TABLE %s\n", tableName)
    );

    for (int i = 0; i < numberOfColumnsToDrop; i++) {
      try {
        Column column = tableColumns.get(columns.get(i).getColumnName());
        alterTableDropColumnsBuilder.append(
          String.format("DROP COLUMN %s", column.getColumnName())
        );
        if (i < numberOfColumnsToDrop - 1) {
          alterTableDropColumnsBuilder.append(",\n");
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    alterTableDropColumnsBuilder.append(';');

    return alterTableDropColumnsBuilder.toString();
  }
}
