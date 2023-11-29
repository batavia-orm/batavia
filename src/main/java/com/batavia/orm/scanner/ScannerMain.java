package com.batavia.orm.scanner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.batavia.orm.adapters.Database;
import com.batavia.orm.commons.Table;

public class ScannerMain {
  public static void main(String[] args) throws SQLException, IOException {
    
    Database dbInstance = Database.getDatabase();

    DataSourceScanner dataSourceScanner = new DataSourceScanner("/Users/enryleinhard/Projects/eclipse/exampleProject/src");
    DatabaseScanner databaseScanner = new DatabaseScanner(dbInstance);

    HashMap<String, Table> databaseTables = databaseScanner.findAllTables();
    HashMap<String, Table> dataSourceTables = dataSourceScanner.findAllEntities();

    System.out.println("DATABASE TABLES");
    System.out.println(databaseTables);
    
    System.out.println("DATA SOURCE TABLES");
    System.out.println(dataSourceTables);
  }
}
