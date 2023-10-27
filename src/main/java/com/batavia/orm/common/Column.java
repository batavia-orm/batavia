package com.batavia.orm.common;

public class Column {
  private String columnName;
  private String columnType;
  private Boolean isPrimaryKey;
  private Boolean isForeignKey;

  public Column(String columnName, String columnType, Boolean isPrimaryKey, Boolean isForeignKey) {
    this.columnName = columnName;
    this.columnType = columnType;
    this.isPrimaryKey = isPrimaryKey;
    this.isForeignKey = isForeignKey;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getColumnType() {
    return columnType;
  }

  public Boolean getIsPrimaryKey() {
    return isPrimaryKey;
  }

  public Boolean getIsForeignKey() {
    return isForeignKey;
  }
}
