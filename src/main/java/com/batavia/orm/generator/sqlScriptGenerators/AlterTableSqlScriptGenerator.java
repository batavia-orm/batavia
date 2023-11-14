package com.batavia.orm.generator.sqlScriptGenerators;

import java.util.ArrayList;

import com.batavia.orm.commons.*;

public class AlterTableSqlScriptGenerator implements ISqlScriptGenerator {

  public String generateSqlScript(
    Table table,
    ArrayList<Column> columns,
    AlterTableContext alterType
  ) {
    String script = "";

    if (alterType == AlterTableContext.NONE) {
      return script;
    }

    script = alterType.getScriptAccordingToAlterType(table, columns) + "\n";

    return script;
  }

  @Override
  public String generateSqlScript(Table table) {
    throw new UnsupportedOperationException(
      "Unimplemented method parameters for this object"
    );
  }
}
