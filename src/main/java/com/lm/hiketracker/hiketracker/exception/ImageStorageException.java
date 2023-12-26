package com.lm.hiketracker.hiketracker.exception;

public class ImageStorageException extends RuntimeException {
  /**
   * Constructor to receive message that gets passed to RuntimeException
   *
   * @param message
   */
  public ImageStorageException(String message) {
    super(message);
  }

  /**
   * Constructor to receive message and throwable that gets passed to RuntimeException
   *
   * @param message
   */
  public ImageStorageException(String message, Throwable ex) {
    super(message, ex);
  }
}
