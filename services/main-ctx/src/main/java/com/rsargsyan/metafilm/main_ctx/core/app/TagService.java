package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagUpdateDTO;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Tag;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.exception.InvalidTagKeyException;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TagRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TagService {

  private static final Pattern KEY_PATTERN = Pattern.compile("^[a-z][a-z0-9]*(-[a-z0-9]+)*$");

  private final TagRepository tagRepository;
  private final MovieRepository movieRepository;
  private final TVShowRepository tvShowRepository;

  @Autowired
  public TagService(TagRepository tagRepository,
                    MovieRepository movieRepository,
                    TVShowRepository tvShowRepository) {
    this.tagRepository = tagRepository;
    this.movieRepository = movieRepository;
    this.tvShowRepository = tvShowRepository;
  }

  public List<TagDTO> listTags() {
    return tagRepository.findAll().stream().map(TagDTO::from).toList();
  }

  @Transactional
  public TagDTO createTag(TagCreationDTO dto) {
    validateKey(dto.key());
    Tag tag = new Tag(dto.key(), dto.name(), dto.localizations());
    return TagDTO.from(tagRepository.save(tag));
  }

  @Transactional
  public TagDTO updateTag(String tagIdStr, TagUpdateDTO dto) {
    Tag tag = tagRepository.findById(Util.validateTSID(tagIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    tag.update(dto.name(), dto.localizations());
    return TagDTO.from(tagRepository.save(tag));
  }

  @Transactional
  public void deleteTag(String tagIdStr) {
    Tag tag = tagRepository.findById(Util.validateTSID(tagIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    tagRepository.delete(tag);
  }

  @Transactional
  public void addTagToMovie(String movieIdStr, String tagIdStr) {
    Movie movie = movieRepository.findByIdWithTags(Util.validateTSID(movieIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    Tag tag = tagRepository.findById(Util.validateTSID(tagIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    movie.addTag(tag);
    movieRepository.save(movie);
  }

  @Transactional
  public void removeTagFromMovie(String movieIdStr, String tagIdStr) {
    Movie movie = movieRepository.findByIdWithTags(Util.validateTSID(movieIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    Tag tag = tagRepository.findById(Util.validateTSID(tagIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    movie.removeTag(tag);
    movieRepository.save(movie);
  }

  @Transactional
  public void addTagToTVShow(String tvShowIdStr, String tagIdStr) {
    TVShow tvShow = tvShowRepository.findByIdWithTags(Util.validateTSID(tvShowIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    Tag tag = tagRepository.findById(Util.validateTSID(tagIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    tvShow.addTag(tag);
    tvShowRepository.save(tvShow);
  }

  @Transactional
  public void removeTagFromTVShow(String tvShowIdStr, String tagIdStr) {
    TVShow tvShow = tvShowRepository.findByIdWithTags(Util.validateTSID(tvShowIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    Tag tag = tagRepository.findById(Util.validateTSID(tagIdStr))
        .orElseThrow(ResourceNotFoundException::new);
    tvShow.removeTag(tag);
    tvShowRepository.save(tvShow);
  }

  @Transactional
  public void syncMovieTags(Long movieId, List<String> tagKeys) {
    if (tagKeys.isEmpty()) return;
    Movie movie = movieRepository.findByIdWithTags(movieId)
        .orElseThrow(ResourceNotFoundException::new);
    Set<String> existingKeys = movie.getTags().stream()
        .map(Tag::getKey).collect(Collectors.toSet());
    List<Tag> toAdd = tagRepository.findAllByKeyIn(tagKeys).stream()
        .filter(t -> !existingKeys.contains(t.getKey()))
        .toList();
    toAdd.forEach(movie::addTag);
    if (!toAdd.isEmpty()) movieRepository.save(movie);
  }

  @Transactional
  public void syncTVShowTags(Long tvShowId, List<String> tagKeys) {
    if (tagKeys.isEmpty()) return;
    TVShow tvShow = tvShowRepository.findByIdWithTags(tvShowId)
        .orElseThrow(ResourceNotFoundException::new);
    Set<String> existingKeys = tvShow.getTags().stream()
        .map(Tag::getKey).collect(Collectors.toSet());
    List<Tag> toAdd = tagRepository.findAllByKeyIn(tagKeys).stream()
        .filter(t -> !existingKeys.contains(t.getKey()))
        .toList();
    toAdd.forEach(tvShow::addTag);
    if (!toAdd.isEmpty()) tvShowRepository.save(tvShow);
  }

  private static void validateKey(String key) {
    if (key == null || key.length() > 64 || !KEY_PATTERN.matcher(key).matches()) {
      throw new InvalidTagKeyException(key);
    }
  }
}
