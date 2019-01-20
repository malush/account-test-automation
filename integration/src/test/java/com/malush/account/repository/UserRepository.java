package com.malush.account.repository;

import com.malush.account.util.Database;
import com.malush.account.util.TestSupport;
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
    Database dbConfig = TestSupport.getEnvironments().getDatabase();
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
  public void deleteAll() throws Exception {
    try{
      connection.prepareStatement(DELETE_ALL_USERS).execute();
    }
    finally {
      connection.close();
    }

  }
}
