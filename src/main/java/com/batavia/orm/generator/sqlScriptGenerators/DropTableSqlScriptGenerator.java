package com.batavia.orm.generator.sqlScriptGenerators;

import java.util.ArrayList;

import com.batavia.orm.commons.*;

public class DropTableSqlScriptGenerator implements ISqlScriptGenerator {

  @Override
  public String generateSqlScript(Table table) {
    String dropTableScript = String.format(
      "DROP TABLE %s;\n",
      table.getTableName()
    );
    return dropTableScript.toString();
  }

  @Override
  public String generateSqlScript(
    Table table,
    ArrayList<Column> columns,
    AlterTableContext alterType
  ) {
    throw new UnsupportedOperationException(
      "Unimplemented method parameters for this object"
    );
  }
}
