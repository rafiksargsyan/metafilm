package com.rsargsyan.metafilm.main_ctx.core.exception;

public class BlankExternalIdException extends DomainException {
  public BlankExternalIdException() {
    super("External ID must not be blank");
  }
}
