package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;
import java.util.ArrayList;
import java.util.HashMap;

public enum AlterTableContext {
  ADD_COLUMN,
  DROP_COLUMN,
  OTHERS,
  NONE;

  public String getScriptAccordingToAlterType(
    Table table,
    ArrayList<Column> columns
  ) {
    String script = "";

    if (table == null || columns == null || columns.size() == 0) {
      System.out.println("Table or Columns are empty!");
      return script;
    }

    if (this == ADD_COLUMN) {
      script = generateAddColumnScript(table, columns);
    } else if (this == DROP_COLUMN) {
      script = generateDropColumnScript(table, columns);
    } else if (this == NONE) {
      throw new UnsupportedOperationException("Invalid alter type: NONE");
    } else {
      System.out.println("Alter type not provided!");
    }

    return script;
  }

  private String generateAddColumnScript(
    Table table,
    ArrayList<Column> columns
  ) {
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
    }

    alterTableAddColumnsBuilder.append(';');

    return alterTableAddColumnsBuilder.toString() + "\n\n";
  }

  private String generateDropColumnScript(
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
      Column column = tableColumns.get(columns.get(i).getColumnName());
      alterTableDropColumnsBuilder.append(
        String.format("DROP COLUMN %s", column.getColumnName())
      );
      if (i < numberOfColumnsToDrop - 1) {
        alterTableDropColumnsBuilder.append(",\n");
      }
    }

    alterTableDropColumnsBuilder.append(';');

    return alterTableDropColumnsBuilder.toString() + "\n\n";
  }
}
