package com.lm.hiketracker.hiketracker.exception;

public class DbCreationException extends RuntimeException {

  /**
   * Constructor to receive message that gets passed to RuntimeException
   *
   * @param message
   */
  public DbCreationException(String message) {
    super(message);
  }

  /**
   * Constructor to receive message and throwable that gets passed to RuntimeException
   *
   * @param message
   */
  public DbCreationException(String message, Throwable ex) {
    super(message, ex);
  }
}
