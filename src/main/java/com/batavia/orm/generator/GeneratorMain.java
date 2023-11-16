package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;
import com.batavia.orm.utils.Utils;
import java.util.ArrayList;

// Strategy PATTERN

public class GeneratorMain {

  private Table tableToBeApplied;
  private ArrayList<Column> columnsToBeApplied;
  private String upMigrationFilePath;
  private String downMigrationFilePath;

  public GeneratorMain(
    Table table,
    String upMigrationFilePath,
    String downMigrationFilePath
  ) {
    this.tableToBeApplied = table;
    this.upMigrationFilePath = upMigrationFilePath;
    this.downMigrationFilePath = downMigrationFilePath;
  }

  public GeneratorMain(
    Table table,
    ArrayList<Column> columns,
    String upMigrationFilePath,
    String downMigrationFilePath
  ) {
    this.tableToBeApplied = table;
    this.columnsToBeApplied = columns;
    this.upMigrationFilePath = upMigrationFilePath;
    this.downMigrationFilePath = downMigrationFilePath;
  }

  public void runSqlScriptGeneratorToFile(
    SqlCommandContext sqlCommand,
    AlterTableContext alterTableContext
  ) {
    writeUpSqlScript(sqlCommand, alterTableContext);
    writeDownSqlScript(sqlCommand, alterTableContext);
  }

  private void writeUpSqlScript(
    SqlCommandContext sqlCommand,
    AlterTableContext alterTableContext
  ) {
    String upScript = sqlCommand.runSqlScriptGenerator(
      this.tableToBeApplied,
      this.columnsToBeApplied,
      alterTableContext
    );
    Utils.writeToUpMigrationFile(this.upMigrationFilePath, upScript);
  }

  private void writeDownSqlScript(
    SqlCommandContext upSqlCommand,
    AlterTableContext alterTableContext
  ) {
    Object[] downContext = getDownSqlScriptContext(
      upSqlCommand,
      alterTableContext
    );

    SqlCommandContext downSqlCommand = (SqlCommandContext) downContext[0];
    AlterTableContext downAlterTableContext = (AlterTableContext) downContext[1];

    if (downSqlCommand == null && downAlterTableContext == null) {
      return;
    }

    String downScript = downSqlCommand.runSqlScriptGenerator(
      this.tableToBeApplied,
      this.columnsToBeApplied,
      downAlterTableContext
    );

    Utils.writeToDownMigrationFile(this.downMigrationFilePath, downScript);
  }

  private Object[] getDownSqlScriptContext(
    SqlCommandContext sqlCommand,
    AlterTableContext alterTableContext
  ) {
    Object[] contextResult = new Object[2];

    switch (sqlCommand) {
      case CREATE_TABLE:
        contextResult[0] = SqlCommandContext.DROP_TABLE;
        contextResult[1] = null;
        break;
      case DROP_TABLE:
        contextResult[0] = SqlCommandContext.CREATE_TABLE;
        contextResult[1] = null;
        break;
      case ALTER_TABLE:
        if (alterTableContext == AlterTableContext.NONE) {
          System.out.println("Alter table command needs the category!");
          return null;
        }

        contextResult[0] = SqlCommandContext.ALTER_TABLE;

        switch (alterTableContext) {
          case ADD_COLUMN:
            contextResult[1] = AlterTableContext.DROP_COLUMN;
            break;
          case DROP_COLUMN:
            contextResult[1] = AlterTableContext.ADD_COLUMN;
            break;
          default:
            return null;
        }
        break;
      default:
        return null;
    }

    return contextResult;
  }
}
