package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;
import com.batavia.orm.utils.Utils;
import java.util.Optional;

// Strategy PATTERN

public class GeneratorMain {

  private Table tableToBeApplied;
  private Column[] columnsToBeApplied;
  private String upMigrationFilePath;
  private String downMigrationFilePath;
  private static ISqlScriptGenerator sqlScriptGenerator;

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
    Column[] columns,
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
    AlterTableContext optionalAlterTableContext
  ) {
    Optional<AlterTableContext> optionalParameter = Optional.ofNullable(
      optionalAlterTableContext
    );
    AlterTableContext AlterTableContext = optionalParameter.isPresent()
      ? optionalParameter.get()
      : null;

    writeUpSqlScript(sqlCommand, AlterTableContext);
    writeDownSqlScript(sqlCommand, AlterTableContext);
  }

  private void writeUpSqlScript(
    SqlCommandContext sqlCommand,
    AlterTableContext AlterTableContext
  ) {
    String upScript = sqlCommand.runSqlScriptGenerator(
      sqlScriptGenerator,
      this.tableToBeApplied,
      this.columnsToBeApplied,
      AlterTableContext
    );
    Utils.writeToUpMigrationFile(this.upMigrationFilePath, upScript);
  }

  private void writeDownSqlScript(
    SqlCommandContext upSqlCommand,
    AlterTableContext AlterTableContext
  ) {
    Object[] downContext = getDownSqlScriptContext(
      upSqlCommand,
      AlterTableContext
    );

    SqlCommandContext downSqlCommand = (SqlCommandContext) downContext[0];
    AlterTableContext downAlterTableContext = (AlterTableContext) downContext[1];

    if (downSqlCommand == null && downAlterTableContext == null) {
      return;
    }

    String downScript = downSqlCommand.runSqlScriptGenerator(
      sqlScriptGenerator,
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
        if (alterTableContext == null) {
          System.out.println("Alter table command needs the category!");
          return null;
        }

        switch (alterTableContext) {
          case ADD_COLUMN:
            contextResult[0] = SqlCommandContext.ALTER_TABLE;
            contextResult[1] = AlterTableContext.DROP_COLUMN;
            break;
          case DROP_COLUMN:
            contextResult[0] = SqlCommandContext.ALTER_TABLE;
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
