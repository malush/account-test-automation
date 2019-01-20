package com.malush.account.repository;

import com.malush.account.util.DatabaseConfig;
import com.malush.account.util.TestUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserRepository implements Repository{

  private static final String DELETE_ALL_USERS = "DELETE FROM users";

  private static UserRepository userRepository = new UserRepository();
  private Connection connection;

  public static UserRepository getRepository() {
    return  userRepository;
  }

  private UserRepository() {
    DatabaseConfig dbConfig = TestUtil.getSettings().getDatabaseConfig();
    try {
      this.connection = DriverManager.getConnection(
          dbConfig.getConnectionUrl(),
          dbConfig.getUsername(),
          dbConfig.getPassword()
      );
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteAll(){
    try{
      connection.prepareStatement(DELETE_ALL_USERS).execute();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void closeConnection() {
    try {
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
