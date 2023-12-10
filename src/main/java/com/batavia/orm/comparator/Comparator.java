package com.batavia.orm.comparator;

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
import java.util.Set;

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

  private void compareLocal(HashMap<String, Table> local, HashMap<String, Table> remote) {
    Set<String> localKeySet = local.keySet();
    for (String localTableName : localKeySet) {
      if (remote.containsKey(localTableName)) {
        // Table exists in both local and remote
        // Compare columns
        Table localTable = local.get(localTableName);
        Table remoteTable = remote.get(localTableName);

        ArrayList<Column> addColumns = new ArrayList<Column>();
        Set<String> localTableColumnKeySet = localTable.getColumns().keySet();
        for (String localColumnName : localTableColumnKeySet) {
          if (remoteTable.getColumns().containsKey(localColumnName)) {
            // Column exists in both local and remote
            // Do nothing
          } else {
            Column addColumn = local.get(localTableName).getColumns().get(localColumnName);
            addColumns.add(addColumn);
            System.out.println("\u2713 " + "create column " + localColumnName + " in remote");
          }
        }
        // Add Column
        GeneratorMain addColumnGen = new GeneratorMain(
            local.get(localTableName),
            addColumns,
            upSQLFile,
            downSQLFile);
        addColumnGen.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.ADD_COLUMN);
      } else {
        // Table exists in local but not in remote
        local
            .get(localTableName)
            .getColumns()
            .forEach((k, v) -> {
              System.out.println("\u2713 " + "create column " + k + " in remote");
            });
        GeneratorMain createTableGen = new GeneratorMain(
            local.get(localTableName),
            upSQLFile,
            downSQLFile);
        createTableGen.runSqlScriptGeneratorToFile(
            SqlCommandContext.CREATE_TABLE,
            AlterTableContext.NONE);
        System.out.println("\u2713 " + "create table " + localTableName + " in remote");
      }
    }

  }

  public void compare(
      HashMap<String, Table> local,
      HashMap<String, Table> remote,
      String migrationFilename) {

    upSQLFile = migrations_dir + "/" + migrationFilename + ".sql";
    downSQLFile = migrations_dir + "/" + migrationFilename + ".down.sql";

    compareLocal(local, remote);
    compareRemote(local, remote);
  }

  private void compareRemote(HashMap<String, Table> local, HashMap<String, Table> remote) {
    Set<String> remoteKeySet = remote.keySet();
    for (String remoteTableName : remoteKeySet) {
      if (local.containsKey(remoteTableName)) {
        // Table exists in both local and remote
        // Compare columns
        Table localTable = local.get(remoteTableName);
        Table remoteTable = remote.get(remoteTableName);

        ArrayList<Column> dropColumns = new ArrayList<Column>();
        Set<String> remoteTableColumnKeySet = remoteTable.getColumns().keySet();
        for (String remoteColumnName : remoteTableColumnKeySet) {
          if (localTable.getColumns().containsKey(remoteColumnName)) {
            // Column exists in both local and remote
            // Do nothing
          } else {
            // Column exists in remote but not in local
            // Drop column in remote
            Column dropColumn = remote.get(remoteTableName).getColumns().get(remoteColumnName);
            dropColumns.add(dropColumn);
            System.out.println("\u2713 " + "drop column " + remoteColumnName + " in remote");
          }
        }
        // Drop Column
        GeneratorMain dropColumnGen = new GeneratorMain(
            remote.get(remoteTableName),
            dropColumns,
            upSQLFile,
            downSQLFile);
        dropColumnGen.runSqlScriptGeneratorToFile(
            SqlCommandContext.ALTER_TABLE,
            AlterTableContext.DROP_COLUMN);
      } else {
        // Table exists in remote but not in local
        // Drop table in remote
        GeneratorMain dropTableGen = new GeneratorMain(
            remote.get(remoteTableName),
            upSQLFile,
            downSQLFile);
        dropTableGen.runSqlScriptGeneratorToFile(
            SqlCommandContext.DROP_TABLE,
            AlterTableContext.NONE);
        System.out.println("\u2713 " + "drop table " + remoteTableName + " in remote");
      }
    }
  }
}
