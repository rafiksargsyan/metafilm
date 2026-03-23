package com.rsargsyan.metafilm.main_ctx.core.exception;

public class AuthorizationException extends DomainException {
  public AuthorizationException() {
    super("Not authorized");
  }

  public AuthorizationException(String message) {
    super(message);
  }
}
