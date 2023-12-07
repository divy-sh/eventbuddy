package com.eventbuddy.eventbuddy.Utils;

public class ErrorResponse {

  private String error;

  public ErrorResponse(Exception e) {
    this.error = e.getMessage();
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
