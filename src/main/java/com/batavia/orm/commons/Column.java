package com.batavia.orm.commons;

public class Column {
  private String columnName;
  private String columnType;
  private Boolean isPrimaryColumn;
  private Boolean isUnique;

  public Column(String columnName, String columnType) {
    this.columnName = columnName;
    this.columnType = Types.getSqlType(columnType);
    this.isPrimaryColumn = false;
    this.isUnique = false;
  }

  public Column(String columnName, String columnType, Boolean isPrimaryColumn, Boolean isUnique) {
    this.columnName = columnName;
    this.columnType = Types.getSqlType(columnType);
    this.isPrimaryColumn = isPrimaryColumn;
    this.isUnique = isPrimaryColumn ? true : isUnique;
  }

  public void setIsPrimaryColumn(Boolean _isPrimaryColumn) {
    this.isPrimaryColumn = _isPrimaryColumn;
  }

  public void setIsUnique(Boolean _isUnique) {
    this.isUnique = _isUnique;
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
