package com.eventbuddy.eventbuddy.Utils;

public class BuddyError extends Exception {

  public BuddyError(String message) {
    super(message);
  }

  public String getError() {
    return getMessage();
  }
}
