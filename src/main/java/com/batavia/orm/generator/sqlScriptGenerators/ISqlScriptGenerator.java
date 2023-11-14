package com.batavia.orm.generator.sqlScriptGenerators;

import java.util.ArrayList;

import com.batavia.orm.commons.*;

public interface ISqlScriptGenerator {
  public String generateSqlScript(Table table);

  public String generateSqlScript(
    Table table,
    ArrayList<Column> columns,
    AlterTableContext alterType
  ) throws Exception;
}
