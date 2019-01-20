package com.malush.account.repository;

/**
 * For cases when we don't have the adequate REST API we need to access the repository directly.
 * Sometimes this is necessary although it is better not to couple the tests with underlying DB
 * implementation. In this particular case, if there was a REST API available then the developers of
 * the application could choose to use another type of database (e.g. file storage, NoSQL) or change
 * the repository type based on client requirements and needs and our integration tests would still
 * work for all underlying repositories by testing the APIs end to end. Here we are coupling the
 * tests with the underlying implementation which makes it hard to make integration tests between
 * different microservices and different teams, as each would access the other teams database directly.
 */
public interface Repository {


  public void deleteAll() throws Exception;

}
