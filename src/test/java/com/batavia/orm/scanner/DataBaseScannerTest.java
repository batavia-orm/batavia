package com.batavia.orm.scanner;

import static org.junit.Assert.assertEquals;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import com.batavia.orm.adapters.Database;
import com.batavia.orm.commons.*;

public class DataBaseScannerTest {
  private ResultSet resultSet;
  private ResultSet resultSet2;
  private DatabaseMetaData dbMetadata;
  private Database dbInstance;
  private Column cols;


  @BeforeEach
  void setUp() throws SQLException {
    resultSet = mock(ResultSet.class);
    resultSet2 = mock(ResultSet.class);
    dbMetadata = mock(DatabaseMetaData.class);
    dbInstance= mock(Database.class);
    cols = mock(Column.class);
    String colsName = "name";
    String typeName = "varchar";
    String tableName = "tabName";

    when(cols.getColumnName()).thenReturn("name");
    when(cols.getColumnType()).thenReturn("varchar");
    when(resultSet.getString("COLUMN_NAME")).thenReturn(colsName);
    when(resultSet.getString("TYPE_NAME")).thenReturn(typeName);
    when(resultSet2.getString("COLUMN_NAME")).thenReturn(colsName);
    when(resultSet2.getString("TYPE_NAME")).thenReturn(typeName);
    when(dbMetadata.getPrimaryKeys(null, null, tableName)).thenReturn(resultSet);
    when(dbMetadata.getColumns(null, null, tableName, null)).thenReturn(resultSet2);
    when(dbMetadata.getTables(null, null, null, new String[] {"TABLE"})).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet2.next()).thenReturn(true).thenReturn(false);
    when(dbInstance.getMetadata()).thenReturn(dbMetadata);
 
    

  }
  @Test
  public void Should_Correctly_Find_The_Columns_Of_A_Table() throws SQLException {
    // Arrange
    Column columnResult;
    String primaryKey = "id";
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);
    
    when(resultSet.getString("COLUMN_NAME")).thenReturn("name");
    when(resultSet.getString("TYPE_NAME")).thenReturn("varchar");
    // Act
    columnResult = databaseScanner.findColumn(resultSet, primaryKey);
    // assertEquals
    assertEquals(columnResult.getColumnName(), "name");
    assertEquals(columnResult.getColumnType(), "VARCHAR");
    assertEquals(columnResult.isPrimary(), false);
    assertEquals(columnResult.isUnique(), false);
  }

  @Test
  public void Should_Correctly_Find_The_Primary_Key_Of_A_Table() throws SQLException {
    // Arrange
    String primaryKeyResult;
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);
    when(resultSet.getString("COLUMN_NAME")).thenReturn("id");
    // Act
    primaryKeyResult = databaseScanner.findPrimaryKey(resultSet);
    // assertEquals
    assertEquals(primaryKeyResult, "id");
  }

  @Test
  public void Should_Correctly_Find_A_Table() throws SQLException {
    // Arrange
    String tableName = "example_table";
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);
    Table tableResult;
    when(dbMetadata.getPrimaryKeys(null, null, tableName)).thenReturn(resultSet);
    when(dbMetadata.getColumns(null, null, tableName, null)).thenReturn(resultSet2);
    when(databaseScanner.findPrimaryKey(resultSet)).thenReturn("id");
    // Act
    tableResult = databaseScanner.findTable(tableName);
    // assertEquals
    assertEquals(tableResult.getTableName(), "example_table");

  }

  @Test
  public void Should_Correctly_Find_Tables() throws SQLException {
    // Arrange 
    String tableName= "tabName";
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);
    HashMap<String, Table> hashTables;
    when(resultSet.getString("TABLE_NAME")).thenReturn(tableName);
    when(databaseScanner.findPrimaryKey(resultSet)).thenReturn("id");
    // Act
    hashTables = databaseScanner.findTables(resultSet);
    // Assert
    assertEquals(hashTables.get(tableName).getTableName(), "tabName" );
  }

   @Test
    public void Should_Correctly_Find_Tables_if_branch() throws SQLException {
    // Arrange 
    String tableName= "batavia_migrations";
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);
    HashMap<String, Table> hashTables;
    when(resultSet.getString("TABLE_NAME")).thenReturn(tableName);

    when(databaseScanner.findPrimaryKey(resultSet)).thenReturn("id");
    // Act
    hashTables = databaseScanner.findTables(resultSet);
    // Assert
    assertEquals(hashTables.get(tableName), null);
  }

  @Test
    public void Should_Correctly_Find_All_Tables() throws SQLException {
    // Arrange 
    String tableName= "tabName";
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);
    HashMap<String, Table> hashTables;
    when(resultSet.getString("TABLE_NAME")).thenReturn(tableName);
    when(databaseScanner.findPrimaryKey(resultSet)).thenReturn("id");
    // Act
    hashTables = databaseScanner.findAllTables();
    // Assert
    assertEquals(hashTables.get(tableName).getTableName(), "tabName" );
  }
}