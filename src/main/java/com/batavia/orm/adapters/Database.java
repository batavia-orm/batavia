package com.batavia.orm.adapters;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  private static Database dbInstance;
  private Connection dbConnection;
  private DatabaseMetaData dbMetadata;

  private Database() {
    Config bataviaConfig = Config.getConfig();
    String databaseURL = bataviaConfig.getDatabaseURL();
    try {
      this.dbConnection = DriverManager.getConnection(databaseURL);
      this.dbMetadata = this.dbConnection.getMetaData();
    } catch (SQLException e) {
      System.out.println("ERR:FAILED-TO-CONNECT-TO-DATABASE");
    }
  }

  public static Database getDatabase() {
    if (dbInstance == null) {
      dbInstance = new Database();
    }
    return dbInstance;
  }

  public Connection getConnection() {
    return this.dbConnection;
  }

  public DatabaseMetaData getMetadata() {
    return this.dbMetadata;
  }
}
