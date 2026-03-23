package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.FullName;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.NameConverter;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user_profile")
public class UserProfile extends AccountScopedAggregateRoot {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "principal_id", nullable = false)
  @Getter
  private Principal principal;

  @Getter
  @Column(nullable = false, length = FullName.MAX_LENGTH)
  @Convert(converter = NameConverter.class)
  private FullName fullName;

  @SuppressWarnings("unused")
  public UserProfile() {}

  public UserProfile(Account account, Principal principal, String name) {
    super(account);
    this.principal = principal;
    this.fullName = new FullName(name);
  }
}
