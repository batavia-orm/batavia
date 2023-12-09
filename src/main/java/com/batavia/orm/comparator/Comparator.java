package com.batavia.orm.comparator;

import com.batavia.orm.adapters.Config;
import com.batavia.orm.adapters.Database;
import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;
import com.batavia.orm.generator.GeneratorMain;
import com.batavia.orm.generator.SqlCommandContext;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;
import com.batavia.orm.scanner.DataSourceScanner;
import com.batavia.orm.scanner.DatabaseScanner;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Comparator {

  private final String migrations_dir;
  private final String datasource_dir;

  public Comparator(String migrations_dir, String datasource_dir) {
    this.migrations_dir = migrations_dir;
    this.datasource_dir = datasource_dir;
  }

  public void run(String migrationFilename) throws SQLException, IOException {
    Database dbInstance = Database.getDatabase();
    Config configInstance = Config.getConfig();

    DataSourceScanner dataSourceScanner = new DataSourceScanner(configInstance);
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);

    HashMap<String, Table> databaseTables = databaseScanner.findAllTables();
    HashMap<String, Table> dataSourceTables = dataSourceScanner.findAllEntities();

    this.compare(dataSourceTables, databaseTables, migrationFilename);
  }

  public void compare(
    HashMap<String, Table> local,
    HashMap<String, Table> remote,
    String migrationFilename
  ) {
    String upSQLFile = migrations_dir + "/" + migrationFilename + ".sql";
    String downSQLFile = migrations_dir + "/" + migrationFilename + ".down.sql";

    local
      .keySet()
      .forEach(localTableName -> {
        if (remote.containsKey(localTableName)) {
          // Table exists in both local and remote
          // Compare columns
          Table localTable = local.get(localTableName);
          Table remoteTable = remote.get(localTableName);

          ArrayList<Column> addColumns = new ArrayList<Column>();
          localTable
            .getColumns()
            .keySet()
            .forEach(localColumnName -> {
              if (remoteTable.getColumns().containsKey(localColumnName)) {
                // Column exists in both local and remote
                // Compare column fields
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
                  addColumns.add(
                    local.get(localTableName).getColumns().get(localColumnName)
                  );
                  System.out.println(
                    "\u2713 " +
                    "create column " +
                    localColumnName +
                    " in remote"
                  );
                }
              } else {
                addColumns.add(
                  local.get(localTableName).getColumns().get(localColumnName)
                );
                System.out.println(
                  "\u2713 " + "create column " + localColumnName + " in remote"
                );
              }
            });
          //Add Column
          GeneratorMain gen2 = new GeneratorMain(
            local.get(localTableName),
            addColumns,
            upSQLFile,
            downSQLFile
          );
          gen2.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.ADD_COLUMN
          );
        } else {
          // Table exists in local but not in remote
          // Create table in remote
          //OK
          local
            .get(localTableName)
            .getColumns()
            .forEach((k, v) -> {
              System.out.println(
                "\u2713 " + "create column " + k + " in remote"
              );
            });
          GeneratorMain gen1 = new GeneratorMain(
            local.get(localTableName),
            upSQLFile,
            downSQLFile
          );
          gen1.runSqlScriptGeneratorToFile(
            SqlCommandContext.CREATE_TABLE,
            AlterTableContext.NONE
          );
          System.out.println(
            "\u2713 " + "create table " + localTableName + " in remote"
          );
        }
      });

    remote
      .keySet()
      .forEach(remoteTableName -> {
        if (local.containsKey(remoteTableName)) {
          // Table exists in both local and remote
          // Compare columns
          Table localTable = local.get(remoteTableName);
          Table remoteTable = remote.get(remoteTableName);

          ArrayList<Column> dropColumns = new ArrayList<Column>();
          remoteTable
            .getColumns()
            .keySet()
            .forEach(remoteColumnName -> {
              if (localTable.getColumns().containsKey(remoteColumnName)) {
                // Column exists in both local and remote
                // Compare column fields
                Column localColumn = localTable
                  .getColumns()
                  .get(remoteColumnName);
                Column remoteColumn = remoteTable
                  .getColumns()
                  .get(remoteColumnName);

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
                  dropColumns.add(
                    remote
                      .get(remoteTableName)
                      .getColumns()
                      .get(remoteColumnName)
                  );
                  System.out.println(
                    "\u2713 " + "drop column " + remoteColumnName + " in remote"
                  );
                }
              } else {
                // Column exists in remote but not in local
                // Drop column in remote
                dropColumns.add(
                  remote.get(remoteTableName).getColumns().get(remoteColumnName)
                );
                System.out.println(
                  "\u2713 " + "drop column " + remoteColumnName + " in remote"
                );
              }
            });
          //Drop Column
          GeneratorMain gen3 = new GeneratorMain(
            remote.get(remoteTableName),
            dropColumns,
            upSQLFile,
            downSQLFile
          );
          gen3.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.DROP_COLUMN
          );
        } else {
          // Table exists in remote but not in local
          // Drop table in remote
          GeneratorMain gen3 = new GeneratorMain(
            remote.get(remoteTableName),
            upSQLFile,
            downSQLFile
          );
          gen3.runSqlScriptGeneratorToFile(
            SqlCommandContext.DROP_TABLE,
            AlterTableContext.NONE
          );
          System.out.println(
            "\u2713 " + "drop table " + remoteTableName + " in remote"
          );
        }
      });
  }
}
