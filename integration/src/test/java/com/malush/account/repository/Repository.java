package com.malush.account.repository;

/**
 * For cases when we don't have the adequate REST API we need to access the repository directly.
 * Sometimes this is necessary although it is better not to couple the tests with underlying DB
 * implementation. In this particular case, if we had s a REST API available then the developers of
 * the application could easily switch to another type of database (e.g. cache, file storage, NoSQL)
 * and for example change the repository type based on client requirements and needs, so in that case
 * our integration tests would still work for all underlying repositories as we would be testing the APIs
 * end to end. Now since we don't have the API we need to use the database directly which means that
 * we are coupling the tests with the underlying implementation which will make many tests to fail
 * as developers change the database schema. It also makes it hard to make integration tests between
 * different microservices maintained by different teams, as each would access the other teams database
 * directly and would then have to know the schema. This means that you can have failing tests whenever
 * another team you have integrated with changes their schema.
 */
public interface Repository {


  public void deleteAll();

  public void closeConnection();

}
