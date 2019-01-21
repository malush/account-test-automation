package com.malush.account.repository;
import java.sql.Connection;

public abstract class SQLRepository implements Repository {

  private final String DELETE_ALL;

  protected Connection connection;

  protected SQLRepository(String DELETE_ALL) {
    this.DELETE_ALL = DELETE_ALL;
  }


  @Override
  public void deleteAll(){
    try{
      connection.prepareStatement(DELETE_ALL).execute();
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
