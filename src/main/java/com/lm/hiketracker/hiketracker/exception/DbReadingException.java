package com.lm.hiketracker.hiketracker.exception;

public class DbReadingException extends RuntimeException {

  /**
   * Constructor to receive message that gets passed to RuntimeException
   *
   * @param message
   */
  public DbReadingException(String message) {
    super(message);
  }

  /**
   * Constructor to receive message and throwable that gets passed to RuntimeException
   *
   * @param message
   */
  public DbReadingException(String message, Throwable ex) {
    super(message, ex);
  }
}
