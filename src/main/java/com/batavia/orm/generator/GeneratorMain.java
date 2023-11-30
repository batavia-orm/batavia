package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  ) throws IOException {
    if (sqlCommand == null || alterTableContext == null) {
      System.out.println("SQL command and Alter Table Context cannot be null!");
      return;
    }

    writeUpSqlScript(sqlCommand, alterTableContext);
    writeDownSqlScript(sqlCommand, alterTableContext);
  }

  private void writeToUpMigrationFile(
      String filePath,
      String migrationScript) throws IOException {
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(migrationScript);
    } catch (IOException e) {
      throw new IOException("File path not found!");
    }
  }

  private void writeUpSqlScript(
    SqlCommandContext sqlCommand,
    AlterTableContext alterTableContext
  ) throws IOException {
    String upScript = sqlCommand.runSqlScriptGenerator(
      this.tableToBeApplied,
      this.columnsToBeApplied,
      alterTableContext
    );
    this.writeToUpMigrationFile(this.upMigrationFilePath, upScript);
  }

  private void writeToDownMigrationFile(
      String filePath,
      String migrationScript) throws IOException {
    try {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        String currentFileContent = new String(Files.readAllBytes(path));
        String newContent = currentFileContent + migrationScript;
        Files.write(path, newContent.getBytes());
    } catch (IOException e) {
        throw new IOException("File path not found!");
    }
  }

  private void writeDownSqlScript(
    SqlCommandContext upSqlCommand,
    AlterTableContext alterTableContext
  ) throws IOException {
    ArrayList<Object> downContexts = getDownSqlScriptContext(
      upSqlCommand,
      alterTableContext
    );

    SqlCommandContext downSqlCommand = (SqlCommandContext) downContexts.get(0);
    AlterTableContext downAlterTableContext = (AlterTableContext) downContexts.get(
      1
    );

    String downScript = downSqlCommand.runSqlScriptGenerator(
      this.tableToBeApplied,
      this.columnsToBeApplied,
      downAlterTableContext
    );

    this.writeToDownMigrationFile(this.downMigrationFilePath, downScript);
  }

  private ArrayList<Object> getDownSqlScriptContext(
    SqlCommandContext sqlCommand,
    AlterTableContext alterTableContext
  ) {
    ArrayList<Object> contextResult = new ArrayList<Object>();

    switch (sqlCommand) {
      case CREATE_TABLE:
        contextResult.add(SqlCommandContext.DROP_TABLE);
        contextResult.add(AlterTableContext.NONE);
        break;
      case DROP_TABLE:
        contextResult.add(SqlCommandContext.CREATE_TABLE);
        contextResult.add(AlterTableContext.NONE);
        break;
      case ALTER_TABLE:
        contextResult.add(SqlCommandContext.ALTER_TABLE);

        switch (alterTableContext) {
          case ADD_COLUMN:
            contextResult.add(AlterTableContext.DROP_COLUMN);
            break;
          case DROP_COLUMN:
            contextResult.add(AlterTableContext.ADD_COLUMN);
            break;
          case OTHERS:
            contextResult.add(AlterTableContext.OTHERS);
            break;
          default:
            contextResult.add(AlterTableContext.NONE);
        }
        break;
      case OTHERS:
        contextResult.add(SqlCommandContext.OTHERS);
        contextResult.add(AlterTableContext.NONE);
        break;
      default:
        contextResult.add(SqlCommandContext.NONE);
        contextResult.add(AlterTableContext.NONE);
    }

    return contextResult;
  }
}
