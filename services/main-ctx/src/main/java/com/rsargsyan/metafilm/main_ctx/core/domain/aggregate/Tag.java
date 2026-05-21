package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "tag")
public class Tag extends AggregateRoot {

  @Getter
  @Column(name = "tag_key", unique = true, nullable = false, length = 64)
  private String key;

  @Getter
  @Column(nullable = false)
  private String name;

  @Getter
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, String> localizations = new HashMap<>();

  @SuppressWarnings("unused")
  Tag() {}

  public Tag(String key, String name, Map<String, String> localizations) {
    this.key = key;
    this.name = name;
    this.localizations = localizations != null ? new HashMap<>(localizations) : new HashMap<>();
  }

  public void update(String name, Map<String, String> localizations) {
    this.name = name;
    this.localizations = localizations != null ? new HashMap<>(localizations) : new HashMap<>();
    touch();
  }
}
