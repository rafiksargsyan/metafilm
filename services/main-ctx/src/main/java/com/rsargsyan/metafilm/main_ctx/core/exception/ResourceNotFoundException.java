package com.rsargsyan.metafilm.main_ctx.core.exception;

public class ResourceNotFoundException extends DomainException {
  public ResourceNotFoundException() {
    super("Resource not found");
  }
}
