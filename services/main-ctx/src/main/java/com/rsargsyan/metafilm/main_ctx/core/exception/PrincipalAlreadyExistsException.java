package com.rsargsyan.metafilm.main_ctx.core.exception;

public class PrincipalAlreadyExistsException extends DomainException {
  public PrincipalAlreadyExistsException() {
    super("Principal already exists");
  }
}
