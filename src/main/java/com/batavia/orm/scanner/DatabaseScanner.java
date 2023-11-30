package com.batavia.orm.scanner;

import java.sql.*;
import java.util.HashMap;

import com.batavia.orm.commons.Table;
import com.batavia.orm.adapters.Database;
import com.batavia.orm.commons.Column;


public class DatabaseScanner {
  private DatabaseMetaData dbMetadata;
  
  public DatabaseScanner(Database dbInstance) {
    this.dbMetadata = dbInstance.getMetadata();
  }

  public HashMap<String, Table> findAllTables() throws SQLException {
    ResultSet tables = dbMetadata.getTables(null, null, null, new String[] {"TABLE"});
    HashMap<String, Table> hashTables = this.findTables(tables);
    return hashTables;
  }

  public HashMap<String, Table> findTables(ResultSet tables) throws SQLException {
      HashMap<String, Table> hashTables = new HashMap<String, Table>();
      while (tables.next()){
        String tableName = tables.getString("TABLE_NAME");
        if (tableName.equals("batavia_migrations")) continue;
        Table table = this.findTable(tableName);
        hashTables.put(tableName, table);
      }
      return hashTables;
  }

  public Table findTable(String tableName) throws SQLException{
    Table table = new Table(tableName);
    ResultSet columns = this.dbMetadata.getColumns(null, null, tableName, null);
    ResultSet primaryKeys = this.dbMetadata.getPrimaryKeys(null, null, tableName);
    String primaryKey = "";
    while (primaryKeys.next()) {
      primaryKey = this.findPrimaryKey(primaryKeys);
    }
    while (columns.next()) {
      Column column = this.findColumn(columns, primaryKey);
      table.addColumn(column);
    }
    return table; 
  }

  public String findPrimaryKey(ResultSet primaryKeys) throws SQLException{
    return primaryKeys.getString("COLUMN_NAME");
  }
  
  public Column findColumn(ResultSet columns, String primaryKey) throws SQLException{
    String columnName = columns.getString("COLUMN_NAME");
    String columnType = columns.getString("TYPE_NAME");
    Boolean isPrimaryKey = columnName.equals(primaryKey);
    Column column = new Column(columnName, columnType.toUpperCase(), isPrimaryKey, false);
    return column;
  }

  
}
