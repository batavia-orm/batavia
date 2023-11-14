package com.batavia.orm.generator;

import java.util.ArrayList;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;

public enum SqlCommandContext {
  CREATE_TABLE,
  DROP_TABLE,
  ALTER_TABLE,
  OTHERS;

  public String runSqlScriptGenerator(
    ISqlScriptGenerator sqlScriptGenerator,
    Table table,
    ArrayList<Column> columns,
    AlterTableContext alterType
  ) {
    String script = "";
    if (this == CREATE_TABLE) {
      sqlScriptGenerator = new CreateTableSqlScriptGenerator();
      script = sqlScriptGenerator.generateSqlScript(table);
    } else if (this == DROP_TABLE) {
      sqlScriptGenerator = new DropTableSqlScriptGenerator();
      script = sqlScriptGenerator.generateSqlScript(table);
    } else if (this == ALTER_TABLE) {
      sqlScriptGenerator = new AlterTableSqlScriptGenerator();
      try {
        script =
          sqlScriptGenerator.generateSqlScript(table, columns, alterType);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    } else if (this == OTHERS) {
      System.out.println("This sql command is not yet being supported");
    }

    return script;
  }
}
