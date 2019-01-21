package com.malush.account.repository;
import java.sql.Connection;

/**
 * {@inheritDoc}
 */
public abstract class SQLRepository implements Repository {

  protected Connection connection;

  protected abstract String deleteAllQuery();

  @Override
  public void deleteAll(){
    try{
      connection.prepareStatement(deleteAllQuery()).execute();
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
