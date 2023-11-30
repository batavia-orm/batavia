package com.batavia.orm.comparator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.batavia.orm.adapters.Database;
import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;
import com.batavia.orm.generator.GeneratorMain;
import com.batavia.orm.generator.SqlCommandContext;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;
import com.batavia.orm.scanner.DataSourceScanner;
import com.batavia.orm.scanner.DatabaseScanner;

public class ComparatorMain {
  private final String migrations_dir;
  private final String datasource_dir;

  public ComparatorMain(String migrations_dir, String datasource_dir) {
      this.migrations_dir = migrations_dir;
      this.datasource_dir = datasource_dir;
  }
  public void main(String migrationFilename) throws SQLException, IOException {
    Database dbInstance = Database.getDatabase();

    DataSourceScanner dataSourceScanner = new DataSourceScanner(datasource_dir);
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);

    HashMap<String, Table> databaseTables = databaseScanner.findAllTables();
    HashMap<String, Table> dataSourceTables = dataSourceScanner.findAllEntities();

    this.comp(dataSourceTables, databaseTables, migrationFilename);

  }
  
  public void comp(HashMap<String, Table> local, HashMap<String, Table> remote, String migrationFilename) {
    String upSQLFile = migrations_dir + "/" + migrationFilename + ".sql";
    String downSQLFile = migrations_dir + "/" + migrationFilename + ".down.sql";

    local.keySet().forEach(localTableName -> {
      if (remote.containsKey(localTableName)) {
        // Table exists in both local and remote
        // Compare columns
        Table localTable = local.get(localTableName);
        Table remoteTable = remote.get(localTableName);

        ArrayList<Column> addColumns = new ArrayList<Column>();
        localTable.getColumns().keySet().forEach(localColumnName -> {
          if (remoteTable.getColumns().containsKey(localColumnName)) {
            // Column exists in both local and remote
            // Compare column fields
            Column localColumn = localTable.getColumns().get(localColumnName);
            Column remoteColumn = remoteTable.getColumns().get(localColumnName);

            if (!localColumn.getColumnType().equals(remoteColumn.getColumnType())) {
              System.out.println("Column " + localColumnName + " in table " + localTableName + " has different type");
            }
            if (!localColumn.isPrimary().equals(remoteColumn.isPrimary())) {
              System.out.println("Column " + localColumnName + " in table " + localTableName + " has different primary key");
            }
            if (!localColumn.isUnique().equals(remoteColumn.isUnique())) {
              System.out.println("Column " + localColumnName + " in table " + localTableName + " has different unique");
            }
          } else {
            addColumns.add(local.get(localTableName).getColumns().get(localColumnName));
            System.out.println("create column " + localColumnName + " in remote");
          }
        });
        //Add Column
        GeneratorMain gen2 = new GeneratorMain(
          local.get(localTableName),
          addColumns,
          upSQLFile,
          downSQLFile
        );
        try {
          gen2.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.ADD_COLUMN
          );
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      } else {
        // Table exists in local but not in remote
        // Create table in remote
        //OK
        local.get(localTableName).getColumns().forEach((k, v) -> {
          System.out.println("create column " + k + " in remote");
        });
        GeneratorMain gen1 = new GeneratorMain(
          local.get(localTableName),
          upSQLFile,
          downSQLFile
        );
        try {
          gen1.runSqlScriptGeneratorToFile(
            SqlCommandContext.CREATE_TABLE,
            AlterTableContext.NONE
          );
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        System.out.println("create table " + localTableName + " in remote");
      }
    });
    
    remote.keySet().forEach(remoteTableName -> {
      if (local.containsKey(remoteTableName)) {
        // Table exists in both local and remote
        // Compare columns
        Table localTable = local.get(remoteTableName);
        Table remoteTable = remote.get(remoteTableName);

        ArrayList<Column> dropColumns = new ArrayList<Column>();
        remoteTable.getColumns().keySet().forEach(remoteColumnName -> {
          if (localTable.getColumns().containsKey(remoteColumnName)) {
            // Column exists in both local and remote
            // Compare column fields
            Column localColumn = localTable.getColumns().get(remoteColumnName);
            Column remoteColumn = remoteTable.getColumns().get(remoteColumnName);

            if (!localColumn.getColumnType().equals(remoteColumn.getColumnType())) {

              System.out.println("Column " + remoteColumnName + " in table " + remoteTableName + " has different type, it should be " + localColumn.getColumnType() + " instead ");
            }
            if (!localColumn.isPrimary().equals(remoteColumn.isPrimary())) {
              System.out.println("Column " + remoteColumnName + " in table " + remoteTableName + " has different primary key, it should be " + localColumn.isPrimary() + " instead");
            }
            if (!localColumn.isUnique().equals(remoteColumn.isUnique())) {
              System.out.println("Column " + remoteColumnName + " in table " + remoteTableName + " has different unique, it should be " + localColumn.isUnique() + " instead");
            }
          } else {
            // Column exists in remote but not in local
            // Drop column in remote
            dropColumns.add(remote.get(remoteTableName).getColumns().get(remoteColumnName));
            System.out.println("drop column " + remoteColumnName + " in remote");
          }
        });
        //Drop Column
        GeneratorMain gen3 = new GeneratorMain(
          remote.get(remoteTableName),
          dropColumns,
          upSQLFile,
          downSQLFile
        );
        try {
          gen3.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.DROP_COLUMN
          );
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else {
        // Table exists in remote but not in local
        // Drop table in remote
        GeneratorMain gen3 = new GeneratorMain(
          remote.get(remoteTableName),
          upSQLFile,
          downSQLFile
        );
        try {
          gen3.runSqlScriptGeneratorToFile(
            SqlCommandContext.DROP_TABLE,
            AlterTableContext.NONE
          );
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        System.out.println("drop table " + remoteTableName + " in remote");
      }
    });
    
  }
}
