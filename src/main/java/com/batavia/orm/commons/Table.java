package com.batavia.orm.commons;

import java.util.HashMap;

public class Table {
  private String tableName;
  private HashMap<String, Column> columns;
  
  public Table(String tableName) {
    this.tableName = tableName;
    this.columns = new HashMap<String, Column>();
  }

  public String getTableName() {
    return tableName;
  }

  public HashMap<String, Column> getColumns() {
    return this.columns;
  }

  public void addColumn(Column column) {
    this.columns.put(column.getColumnName(), column);
  }

  public int getNumberOfColumns() {
    return this.columns.size();
  }

}
