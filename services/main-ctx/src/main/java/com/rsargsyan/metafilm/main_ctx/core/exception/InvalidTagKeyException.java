package com.rsargsyan.metafilm.main_ctx.core.exception;

public class InvalidTagKeyException extends DomainException {
  public InvalidTagKeyException(String key) {
    super("Invalid tag key: '" + key + "'. Must match ^[a-z][a-z0-9]*(-[a-z0-9]+)*$ and be at most 64 characters.");
  }
}
