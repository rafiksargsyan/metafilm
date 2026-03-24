package com.rsargsyan.metafilm.main_ctx.core.exception;

public class SeasonAlreadyExistsException extends DomainException {
  public SeasonAlreadyExistsException(Integer seasonNumber) {
    super("Season " + seasonNumber + " already exists for this TV show");
  }
}
