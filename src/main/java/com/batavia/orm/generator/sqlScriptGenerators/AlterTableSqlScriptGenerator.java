package com.batavia.orm.generator.sqlScriptGenerators;

import java.util.ArrayList;

import com.batavia.orm.commons.*;

public class AlterTableSqlScriptGenerator implements ISqlScriptGenerator {

  @Override
  public String generateSqlScript(
    Table table,
    ArrayList<Column> columns,
    AlterTableContext alterType
  ) {
    String script = "";

    script = alterType.getScriptAccordingToAlterType(table, columns);

    return script;
  }

  @Override
  public String generateSqlScript(Table table) {
    throw new UnsupportedOperationException(
      "Unimplemented method parameters for this object"
    );
  }
}
