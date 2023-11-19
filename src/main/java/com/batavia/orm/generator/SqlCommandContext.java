package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;
import java.util.ArrayList;

public enum SqlCommandContext {
  CREATE_TABLE,
  DROP_TABLE,
  ALTER_TABLE,
  NONE,
  OTHERS;

  private static ISqlScriptGenerator sqlScriptGenerator;

  public String runSqlScriptGenerator(
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
      script = sqlScriptGenerator.generateSqlScript(table, columns, alterType);
    } else if (this == OTHERS) {
      System.out.println("This sql command is not yet being supported");
    } else {
      System.out.println("No sql command!");
    }

    return script;
  }
}
