package com.batavia.orm.commons;

public class Column {
  private String columnName;
  private String columnType;
  private Boolean isPrimaryColumn;
  private Boolean isUnique;

  public Column(String columnName, String columnType, Boolean isPrimaryColumn, Boolean isUnique) {
    this.columnName = columnName;
    this.columnType = columnType;
    this.isPrimaryColumn = isPrimaryColumn;
    this.isUnique = isUnique;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getColumnType() {
    return columnType;
  }

  public Boolean isPrimary() {
    return isPrimaryColumn;
  }

  public Boolean isUnique() {
    return isUnique;
  }
}
