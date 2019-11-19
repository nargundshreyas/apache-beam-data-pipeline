package com.example.bigdata.exception;

import lombok.NoArgsConstructor;

/** The type Data pipeline exception. */
@NoArgsConstructor
public class DataPipelineException extends RuntimeException {

  /**
   * Instantiates a new Data pipeline exception.
   *
   * @param message the message
   */
public DataPipelineException(String message) {
    super(message);
  }
}
