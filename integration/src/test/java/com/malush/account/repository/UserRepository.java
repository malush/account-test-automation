package com.malush.account.repository;

import com.malush.util.DatabaseConfig;
import com.malush.util.TestUtil;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * {@inheritDoc}
 *
 * In a microservice architecture we might have a database per service.
 * We should not make any assumptions on the underlying architecture of the project under test.
 * This class provides a direct access to the user store, whether its a user service repository or
 * a monolithic repository.
 */
public class UserRepository extends SQLRepository{

  private static final String DELETE_ALL_USERS = "DELETE FROM users";

  private static UserRepository userRepository = new UserRepository();

  public static UserRepository getRepository() {
    return  userRepository;
  }

  private UserRepository() {

    //should be getUserDatabaseConfig if there is a separate user store
    DatabaseConfig dbConfig = TestUtil.getSettings().getDatabaseConfig();
    try {
      connection = DriverManager.getConnection(
          dbConfig.getConnectionUrl(),
          dbConfig.getUsername(),
          dbConfig.getPassword()
      );
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected String deleteAllQuery() {
    return DELETE_ALL_USERS;
  }
}
