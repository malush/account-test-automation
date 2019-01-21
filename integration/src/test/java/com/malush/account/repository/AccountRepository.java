package com.malush.account.repository;

import com.malush.util.DatabaseConfig;
import com.malush.util.TestUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  {@inheritDoc}
 *  In a microservice architecture we might have a database per service.
 *  We should not make any assumptions on the underlying architecture of the project under test.
 *  This class provides a direct access to the account store, whether its an account service repository
 *  or a monolithic repository.
 */
public class AccountRepository extends SQLRepository {
  private static final String DELETE_ALL_ACCOUNTS = "DELETE FROM account";

  private static AccountRepository accountRepository = new AccountRepository();

  public static AccountRepository getRepository() {
    return accountRepository;
  }

  private AccountRepository() {
    super(DELETE_ALL_ACCOUNTS);
    //should be getAccountDatabaseConfig() if there is a separate account store
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
}
