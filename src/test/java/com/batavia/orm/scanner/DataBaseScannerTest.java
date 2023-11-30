package com.batavia.orm.scanner;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import com.batavia.orm.commons.*;

public class DataBaseScannerTest {
  private ResultSet resultSet;
  private ResultSet resultSet2;
  private DatabaseMetaData dbMetadata;

  @BeforeEach
  void setUp() throws SQLException {
    resultSet = mock(ResultSet.class);
    resultSet2 = mock(ResultSet.class);
    dbMetadata = mock(DatabaseMetaData.class);
  }
  @Test
  public void Should_Correctly_Find_The_Columns_Of_A_Table() throws SQLException {
    // Arrange
    Column columnResult;
    String primaryKey = "id";
    DatabaseScanner databaseScanner = new DatabaseScanner(null);
    
    when(resultSet.getString("COLUMN_NAME")).thenReturn("name");
    when(resultSet.getString("TYPE_NAME")).thenReturn("varchar");
    // Act
    columnResult = databaseScanner.findColumn(resultSet, primaryKey);
    // Assert
    assert(columnResult.getColumnName().equals("name"));
    assert(columnResult.getColumnType().equals("varchar"));
    assert(columnResult.isPrimary().equals(false));
    assert(columnResult.isUnique().equals(false));
  }

  @Test
  public void Should_Correctly_Find_The_Primary_Key_Of_A_Table() throws SQLException {
    // Arrange
    String primaryKeyResult;
    DatabaseScanner databaseScanner = new DatabaseScanner(null);
    when(resultSet.getString("COLUMN_NAME")).thenReturn("id");
    // Act
    primaryKeyResult = databaseScanner.findPrimaryKey(resultSet);
    // Assert
    assert(primaryKeyResult.equals("id"));
  }

  @Test
  public void Should_Correctly_Find_A_Table() throws SQLException {
    // Arrange
    String tableName = "example_table";
    DatabaseScanner databaseScanner = new DatabaseScanner(null);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet2.next()).thenReturn(true).thenReturn(false);
    when(dbMetadata.getColumns(null, null, tableName, null)).thenReturn(resultSet);
    when(dbMetadata.getPrimaryKeys(null, null, tableName)).thenReturn(resultSet2);
    // Act
    Table tableResult = databaseScanner.findTable(tableName);
    // Assert
    assert(tableResult.getTableName().equals("example_table"));

  }
}