package com.malush.util.settings;

public class DatabaseConfig {

  private String connectionUrl;
  private String username;
  private String password;

  /**
   * Returns url for the db connection that is loaded from <em>settings.yaml</em> file
   * @return db connection URL
   */
  public String getConnectionUrl() {
    return connectionUrl;
  }

  public void setConnectionUrl(String connectionUrl) {
    this.connectionUrl = connectionUrl;
  }

  /**
   * Returns username for the db connection that is loaded from <em>settings.yaml</em> file.
   * @return db connection username
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Decrypts and returns password for the db connection that is loaded from <em>settings.yaml</em>
   * @return db connection password
   */
  public String getPassword() {
    return password;
    /*if (password == null || password.isEmpty())
            return "";
        byte[] byteArr = Base64.decodeBase64(password.getBytes());
        return new String(byteArr);*/
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
