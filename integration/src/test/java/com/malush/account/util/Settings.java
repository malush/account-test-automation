package com.malush.account.util;

public class Settings {

  private WebConfig webConfig;
  private DatabaseConfig databaseConfig;

  public WebConfig getWebConfig() {
    return webConfig;
  }

  public void setWebConfig(WebConfig webConfig) {
    this.webConfig = webConfig;
  }

  public DatabaseConfig getDatabaseConfig() {
    return databaseConfig;
  }

  public void setDatabaseConfig(DatabaseConfig databaseConfig) {
    this.databaseConfig = databaseConfig;
  }
}
