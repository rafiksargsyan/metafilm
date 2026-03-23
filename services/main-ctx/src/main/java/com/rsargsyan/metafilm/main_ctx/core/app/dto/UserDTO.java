package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.UserProfile;

public record UserDTO(String id, String accountId, String fullName) {
  public static UserDTO from(UserProfile userProfile) {
    return new UserDTO(
        userProfile.getStrId(),
        userProfile.getAccount().getStrId(),
        userProfile.getFullName() != null ? userProfile.getFullName().value() : null
    );
  }
}
