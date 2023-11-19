package com.batavia.orm.generator.sqlScriptGenerators;

import com.batavia.orm.commons.*;
import java.util.ArrayList;

public interface ISqlScriptGenerator {
  public String generateSqlScript(Table table);

  public String generateSqlScript(
    Table table,
    ArrayList<Column> columns,
    AlterTableContext alterType
  );
}
