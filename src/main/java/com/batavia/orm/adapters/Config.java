package com.batavia.orm.adapters;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

  private static Config configInstance;
  private String databaseURL;
  private String migrationsDir;
  private String dataSourceDir;

  private Config() {
    Dotenv dotenv = Dotenv.load();
    this.databaseURL = dotenv.get("DATABASE_URL");
    this.migrationsDir = dotenv.get("MIGRATIONS_DIR");
    this.dataSourceDir = dotenv.get("DATASOURCE_DIR");
  }
  
  public static Config getConfig() {
    if (configInstance == null) {
      configInstance = new Config();
    }
    return configInstance;
  }

  public String getDatabaseURL() {
    return this.databaseURL;
  }

  public String getMigrationsDir() {
    return this.migrationsDir;
  }

  public String getDataSourceDir() {
    return this.dataSourceDir;
  }
}
