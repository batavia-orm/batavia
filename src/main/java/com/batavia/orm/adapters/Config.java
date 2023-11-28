package com.batavia.orm.adapters;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

  private static Config configInstance;
  private String databaseURL;

  private Config() {
    Dotenv dotenv = Dotenv.load();
    this.databaseURL = dotenv.get("DATABASE_URL");
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
}
