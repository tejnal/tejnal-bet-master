package com.tejnalbetmaster.security.jwt.advice;

import java.util.Date;

public class ErrorMessage {
  private int statusCode;
  private Date timestamp;
  private String message;
  private String description;

  public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
    this.statusCode = statusCode;
    this.timestamp = timestamp;
    this.message = message;
    this.description = description;
  }

	@Override
	public String toString() {
		return "ErrorMessage{" +
				"statusCode=" + statusCode +
				", timestamp=" + timestamp +
				", message='" + message + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
