package com.batavia.orm.scanner.dbScanner;

import java.sql.*;
import java.util.HashMap;

import com.batavia.orm.commons.Table;
import com.batavia.orm.commons.Column;


public class DatabaseScanner {
  public static void main(String[] args) {
    String url = "jdbc:postgresql://db.msfwnmkjpydhcuhmcssf.supabase.co:5432/postgres?user=postgres&password=butewahmansion@21";
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(url);
      DatabaseMetaData metadata = connection.getMetaData();
      ResultSet tables = metadata.getTables(null, null, null, new String[]{"TABLE"});
      HashMap<String, Table> hashTables = DatabaseScanner.findTables(tables, metadata);
      String format = "%-40s%-40s%-40s%s%n";

      for (HashMap.Entry<String, Table> entry : hashTables.entrySet()) {
        System.out.println("TABLE_NAME: " + entry.getKey());
        Table tableeee = entry.getValue();
        System.out.printf(format, "Name", "Type", "Primary Key", "Unique");
        for (HashMap.Entry<String, Column> entry2 : tableeee.getColumns().entrySet()) {
          System.out.println(entry2.getKey());
          Column column = entry2.getValue();
          System.out.printf(format, column.getColumnName(), column.getColumnType(), column.isPrimary(), column.isUnique());
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("Hello World!");
  }

  public static HashMap<String, Table> findTables(ResultSet tables, DatabaseMetaData metadata) throws SQLException {
      HashMap<String, Table> hashTables = new HashMap<String, Table>();
      while (tables.next()){
        String tableName = tables.getString("TABLE_NAME");
        Table table = DatabaseScanner.findTable(tableName, metadata);
        hashTables.put(tableName, table);
      }
      return hashTables;
  }

  public static Table findTable (String tableName, DatabaseMetaData metadata) throws SQLException{
    Table table = new Table(tableName);
    ResultSet columns = metadata.getColumns(null, null, tableName, null);
    ResultSet primaryKeys = metadata.getPrimaryKeys(null, null, tableName);
    String primaryKey = "";

    while (primaryKeys.next()) {
      primaryKey = findPrimaryKey(primaryKeys);
    }
    while (columns.next()) {
      Column column = findColumn(columns, primaryKey);
      table.addColumn(column);
    }
    return table; 
  }

  public static String findPrimaryKey(ResultSet primaryKeys) throws SQLException{
    return primaryKeys.getString("COLUMN_NAME");
  }
  
  public static Column findColumn (ResultSet columns, String primaryKey) throws SQLException{
    String columnName = columns.getString("COLUMN_NAME");
    String columnType = columns.getString("TYPE_NAME");
    Boolean isPrimaryKey = columnName.equals(primaryKey);
    Column column = new Column(columnName, columnType, isPrimaryKey, false);
    return column;
  }

  
}
