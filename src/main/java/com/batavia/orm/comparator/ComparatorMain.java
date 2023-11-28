package com.batavia.orm.comparator;

import java.util.ArrayList;
import java.util.HashMap;

import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;
import com.batavia.orm.generator.GeneratorMain;
import com.batavia.orm.generator.SqlCommandContext;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableContext;

public class ComparatorMain {
  public static void main(String[] args) {
    System.out.println("Hello World!");
    HashMap<String, Table> dbTable = new HashMap<String, Table>();
    HashMap<String, Table> localTable = new HashMap<String, Table>();

    Table thisislocal = new Table("table1");
    thisislocal.addColumn(new Column("column1", "varchar", true, true));
    thisislocal.addColumn(new Column("column3", "varchar", false, false));

    Table thisisremote = new Table("table1");
    thisisremote.addColumn(new Column("column1", "varchar", true, true));
    thisisremote.addColumn(new Column("column2", "varchar", false, false));
    thisisremote.addColumn(new Column("column3", "varchar", true, false));
    
    Table thisislocal2 = new Table("table2");
    Table thisisremote2 = new Table("table69");
    localTable.put("table1", thisislocal);
    localTable.put("table2", thisislocal2);

    dbTable.put("table1", thisisremote);
    dbTable.put("table69", thisisremote2);
    
    ComparatorMain.comp(localTable, dbTable);

  }
  
  public static void comp(HashMap<String, Table> local, HashMap<String, Table> remote) {
    String upSQLFile = "/home/dannel/Documents/Dannel/Project/batavia/src/main/java/com/batavia/orm/comparator/scripts/test1.sql";
    String downSQLFile = "/home/dannel/Documents/Dannel/Project/batavia/src/main/java/com/batavia/orm/comparator/scripts/test2.sql";
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
        gen2.runSqlScriptGeneratorToFile(
          SqlCommandContext.ALTER_TABLE,
          AlterTableContext.ADD_COLUMN
        );

      } else {
        // Table exists in local but not in remote
        // Create table in remote
        //OK
        GeneratorMain gen1 = new GeneratorMain(
          local.get(localTableName),
          upSQLFile,
          downSQLFile
        );
        gen1.runSqlScriptGeneratorToFile(
          SqlCommandContext.CREATE_TABLE,
          null
        );
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
              dropColumns.add()
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
          null
        );
        System.out.println("drop table " + remoteTableName + " in remote");
      }
    });
    
  }
}
