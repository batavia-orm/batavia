package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;
import com.batavia.orm.utils.Utils;
import java.util.Optional;

// Strategy PATTERN

public class GeneratorMain {

  private Table tableToBeApplied;
  private Column[] columnsToBeApplied;
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
    SqlCommandCategory sqlCommand,
    AlterTableCategory optionalAlterTableCategory
  ) {
    Optional<AlterTableCategory> optionalParameter = Optional.ofNullable(
      optionalAlterTableCategory
    );
    AlterTableCategory alterTableCategory = optionalParameter.isPresent()
      ? optionalParameter.get()
      : null;

    writeUpSqlScript(sqlCommand, alterTableCategory);
    writeDownSqlScript(sqlCommand, alterTableCategory);
  }

  private void writeUpSqlScript(
    SqlCommandCategory sqlCommand,
    AlterTableCategory alterTableCategory
  ) {
    String upScript = sqlCommand.runSqlScriptGenerator(
      this.tableToBeApplied,
      this.columnsToBeApplied,
      alterTableCategory
    );
    Utils.writeToUpMigrationFile(this.upMigrationFilePath, upScript);
  }

  private void writeDownSqlScript(
    SqlCommandCategory sqlCommand,
    AlterTableCategory alterTableCategory
  ) {
    // initialize
    SqlCommandCategory downSqlCommand = sqlCommand;
    AlterTableCategory downAlterTableCategory = alterTableCategory;

    // take the opposite for down
    if (sqlCommand == SqlCommandCategory.CREATE_TABLE) {
      downSqlCommand = SqlCommandCategory.DROP_TABLE;
    } else if (sqlCommand == SqlCommandCategory.DROP_TABLE) {
      downSqlCommand = SqlCommandCategory.CREATE_TABLE;
    } else if (sqlCommand == SqlCommandCategory.ALTER_TABLE) {
      if (alterTableCategory == null) {
        System.out.println("Alter table command needs the category!");
        return;
      }

      if (alterTableCategory == AlterTableCategory.ADD_COLUMN) {
        downAlterTableCategory = AlterTableCategory.DROP_COLUMN;
      } else if (alterTableCategory == AlterTableCategory.DROP_COLUMN) {
        downAlterTableCategory = AlterTableCategory.ADD_COLUMN;
      }
    }

    String downScript = downSqlCommand.runSqlScriptGenerator(
      this.tableToBeApplied,
      this.columnsToBeApplied,
      downAlterTableCategory
    );
    Utils.writeToDownMigrationFile(this.downMigrationFilePath, downScript);
  }
}
