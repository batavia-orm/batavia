package com.batavia.orm.commons;

public class Table {
  private String tableName;
  private int numberOfColumns;
  private Column[] columns;
  
  public Table(String tableName, int numberOfColumns, Column[] columns) {
    this.tableName = tableName;
    this.numberOfColumns = numberOfColumns;
    this.columns = columns;
  }

  public String getTableName() {
    return tableName;
  }

  public int getNumberOfColumns() {
    return numberOfColumns;
  }

  public Column[] getColumns() {
    return columns;
  }
}
