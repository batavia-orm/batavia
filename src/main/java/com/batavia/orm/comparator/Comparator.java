package com.batavia.orm.comparator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.batavia.orm.adapters.Database;
import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;
import com.batavia.orm.generator.GeneratorMain;
import com.batavia.orm.generator.SqlCommandContext;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;
import com.batavia.orm.scanner.DataSourceScanner;
import com.batavia.orm.scanner.DatabaseScanner;

public class Comparator {

  private final String migrations_dir;
  private final String datasource_dir;

  private String upSQLFile;
  private String downSQLFile;

  public Comparator(String migrations_dir, String datasource_dir) {
    this.migrations_dir = migrations_dir;
    this.datasource_dir = datasource_dir;
  }

  public void run(String migrationFilename) throws SQLException, IOException {
    Database dbInstance = Database.getDatabase();

    DataSourceScanner dataSourceScanner = new DataSourceScanner(datasource_dir);
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);

    HashMap<String, Table> databaseTables = databaseScanner.findAllTables();
    HashMap<String, Table> dataSourceTables = dataSourceScanner.findAllEntities();

    this.compare(dataSourceTables, databaseTables, migrationFilename);
  }

  private ArrayList<AbstractMap.SimpleEntry<String, String>> compareTablesWithLocalAsBase(
      HashMap<String, Table> localTables, HashMap<String, Table> remoteTables) {

    ArrayList<Column> addColumns = new ArrayList<Column>();
    ArrayList<AbstractMap.SimpleEntry<String, String>> sameColumns = new ArrayList<AbstractMap.SimpleEntry<String, String>>();

    Set<String> localSet = localTables.keySet();
    for (String localTableName : localSet) {
      if (remoteTables.containsKey(localTableName)) {
        Table localTable = localTables.get(localTableName);
        Table remoteTable = remoteTables.get(localTableName);

        Set<String> localColumnSet = localTable.getColumns().keySet();

        for (String localColumnName : localColumnSet) {
          if (!remoteTable.getColumns().containsKey(localColumnName)) {
            // Column exists in local but not in remote
            Column toBeAddedColumn = localTables.get(localTableName).getColumns().get(localColumnName);
            addColumns.add(toBeAddedColumn);
            System.out.println("\u2713 " + " ADD Column " + localColumnName + " in DB");
          } else {
            // Column exists in both local and remote
            Column localColumn = localTable
                .getColumns()
                .get(localColumnName);
            Column remoteColumn = remoteTable
                .getColumns()
                .get(localColumnName);

            Boolean isTypeSame = localColumn
                .getColumnType()
                .equals(remoteColumn.getColumnType());
            Boolean isStillPrimary = localColumn
                .isPrimary()
                .equals(remoteColumn.isPrimary());
            Boolean isStillUnique = localColumn
                .isUnique()
                .equals(remoteColumn.isUnique());

            if (!isTypeSame || !isStillPrimary || !isStillUnique) {
              sameColumns.add(new AbstractMap.SimpleEntry<String, String>(localTableName, localColumnName));
            }
          }
        }
        // Run Generator to add Columns
        GeneratorMain addColumnGenerator = new GeneratorMain(
            localTables.get(localTableName),
            addColumns,
            upSQLFile,
            downSQLFile);
        addColumnGenerator.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.ADD_COLUMN);

      } else {
        GeneratorMain createTableGenerator = new GeneratorMain(
            localTables.get(localTableName),
            upSQLFile,
            downSQLFile);
        createTableGenerator.runSqlScriptGeneratorToFile(
            SqlCommandContext.CREATE_TABLE,
            AlterTableContext.NONE);

        Set<String> columnsOnTable = localTables
            .get(localTableName)
            .getColumns()
            .keySet();

        for (String columnName : columnsOnTable) {
          System.out.println("\u2713 " + "ADD Column " + columnName + " in DB");
        }
        System.out.println("\u2713 " + "create table " + localTableName + " in DB");
      }
    }
    return sameColumns;
  }

  private void compareTablesWithRemoteAsBase(HashMap<String, Table> localTables, HashMap<String, Table> remoteTables) {
    Set<String> remoteSet = remoteTables.keySet();

    for (String remoteTableName : remoteSet) {
      if (localTables.containsKey(remoteTableName)) {
        // Table exists in both local and remote
        Table localTable = localTables.get(remoteTableName);
        Table remoteTable = remoteTables.get(remoteTableName);

        ArrayList<Column> dropColumns = new ArrayList<Column>();
        Set<String> remoteColumnSet = remoteTable.getColumns().keySet();

        for (String remoteColumnName : remoteColumnSet) {
          if (!localTable.getColumns().containsKey(remoteColumnName)) {
            // Column exists in remote but not in local
            Column toBeDroppedColumn = localTables.get(remoteTableName).getColumns().get(remoteColumnName);
            dropColumns.add(toBeDroppedColumn);
            System.out.println("\u2713 " + "drop column " + remoteColumnName + " in remote");
          }
        }
        // Drop Column
        GeneratorMain dropColumnGenerator = new GeneratorMain(
            remoteTables.get(remoteTableName),
            dropColumns,
            upSQLFile,
            downSQLFile);
        dropColumnGenerator.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.DROP_COLUMN);
      } else {
        // Table exists in remote but not in local
        GeneratorMain dropTableGenerator = new GeneratorMain(
            remoteTables.get(remoteTableName),
            upSQLFile,
            downSQLFile);
        dropTableGenerator.runSqlScriptGeneratorToFile(
            SqlCommandContext.DROP_TABLE,
            AlterTableContext.NONE);
        System.out.println("\u2713 " + "drop table " + remoteTableName + " in remote");
      }
    }
  }

  private void compareLocalAndRemoteColumns(HashMap<String, Table> localTables, HashMap<String, Table> remoteTables,
      ArrayList<AbstractMap.SimpleEntry<String, String>> columnsBothExists) {

    for (AbstractMap.SimpleEntry<String, String> sameColumn : columnsBothExists) {
      ArrayList<Column> dropColumns = new ArrayList<Column>();

      String tableName = sameColumn.getKey();
      String columnName = sameColumn.getValue();
      Column remoteColumn = remoteTables
          .get(tableName)
          .getColumns()
          .get(columnName);

      dropColumns.add(remoteColumn);
      
      GeneratorMain dropColumnGenerator = new GeneratorMain(
            remoteTables.get(tableName),
            dropColumns,
            upSQLFile,
            downSQLFile);
        dropColumnGenerator.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.DROP_COLUMN);
    }

    for (AbstractMap.SimpleEntry<String, String> sameColumn : columnsBothExists) {
      ArrayList<Column> addColumns = new ArrayList<Column>();

      String tableName = sameColumn.getKey();
      String columnName = sameColumn.getValue();
      Column localColumn = localTables
          .get(tableName)
          .getColumns()
          .get(columnName);

      addColumns.add(localColumn);
      
      GeneratorMain addColumnGenerator = new GeneratorMain(
            localTables.get(tableName),
            addColumns,
            upSQLFile,
            downSQLFile);
        addColumnGenerator.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.ADD_COLUMN);
    }
  }

  public void compare(
      HashMap<String, Table> local,
      HashMap<String, Table> remote,
      String migrationFilename) {
    this.upSQLFile = migrations_dir + "/" + migrationFilename + ".sql";
    this.downSQLFile = migrations_dir + "/" + migrationFilename + ".down.sql";

    ArrayList<AbstractMap.SimpleEntry<String, String>> columnBothExists = compareTablesWithLocalAsBase(local, remote);
    compareTablesWithRemoteAsBase(local, remote);

    compareLocalAndRemoteColumns(local, remote, columnBothExists);
  }
}
