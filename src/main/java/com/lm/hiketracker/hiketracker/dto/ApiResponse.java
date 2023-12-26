package com.lm.hiketracker.hiketracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

  private T data;

  private String error;

  private HttpStatus status;

  public ApiResponse(T data) {
    this.data = data;
  }

  public ApiResponse(String error, HttpStatus status) {
    this.error = error;
    this.status = status;
  }
}
