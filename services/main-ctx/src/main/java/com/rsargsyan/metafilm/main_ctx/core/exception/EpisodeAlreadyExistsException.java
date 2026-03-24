package com.rsargsyan.metafilm.main_ctx.core.exception;

public class EpisodeAlreadyExistsException extends DomainException {
  public EpisodeAlreadyExistsException() {
    super("Episode already exists for this TV show");
  }
}
