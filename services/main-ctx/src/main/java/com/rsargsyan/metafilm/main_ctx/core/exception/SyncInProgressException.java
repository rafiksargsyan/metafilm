package com.rsargsyan.metafilm.main_ctx.core.exception;

public class SyncInProgressException extends DomainException {
  public SyncInProgressException() {
    super("Sync already in progress");
  }
}
