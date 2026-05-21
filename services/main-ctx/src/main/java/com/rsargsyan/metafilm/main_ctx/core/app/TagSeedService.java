package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Tag;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TagSeedService implements CommandLineRunner {

  private final TagRepository tagRepository;

  @Autowired
  public TagSeedService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    int created = 0;
    int updated = 0;
    for (Map.Entry<String, String> entry : TmdbGenreTagMapping.ALL_TAG_NAMES.entrySet()) {
      String key = entry.getKey();
      String name = entry.getValue();
      Map<String, String> localizations = TmdbGenreTagMapping.ALL_TAG_LOCALIZATIONS.getOrDefault(key, Map.of());
      var existing = tagRepository.findByKey(key);
      if (existing.isEmpty()) {
        tagRepository.save(new Tag(key, name, localizations));
        created++;
      } else {
        Tag tag = existing.get();
        tag.update(name, localizations);
        tagRepository.save(tag);
        updated++;
      }
    }
    if (created > 0 || updated > 0) {
      log.info("Tag seed: created={}, updated={}", created, updated);
    }
  }
}
