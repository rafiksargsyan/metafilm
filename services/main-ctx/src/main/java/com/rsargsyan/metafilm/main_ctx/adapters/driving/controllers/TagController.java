package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.TagService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("/admin/tag")
  public ResponseEntity<List<TagDTO>> listTags() {
    return ResponseEntity.ok(tagService.listTags());
  }

  @PostMapping("/admin/tag")
  public ResponseEntity<TagDTO> createTag(@RequestBody TagCreationDTO dto) {
    return new ResponseEntity<>(tagService.createTag(dto), HttpStatus.CREATED);
  }

  @PatchMapping("/admin/tag/{tagId}")
  public ResponseEntity<TagDTO> updateTag(@PathVariable String tagId,
                                          @RequestBody TagUpdateDTO dto) {
    return ResponseEntity.ok(tagService.updateTag(tagId, dto));
  }

  @DeleteMapping("/admin/tag/{tagId}")
  public ResponseEntity<Void> deleteTag(@PathVariable String tagId) {
    tagService.deleteTag(tagId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/admin/movie/{movieId}/tag/{tagId}")
  public ResponseEntity<Void> addTagToMovie(@PathVariable String movieId,
                                             @PathVariable String tagId) {
    tagService.addTagToMovie(movieId, tagId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/admin/movie/{movieId}/tag/{tagId}")
  public ResponseEntity<Void> removeTagFromMovie(@PathVariable String movieId,
                                                  @PathVariable String tagId) {
    tagService.removeTagFromMovie(movieId, tagId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/admin/tvshow/{tvShowId}/tag/{tagId}")
  public ResponseEntity<Void> addTagToTVShow(@PathVariable String tvShowId,
                                              @PathVariable String tagId) {
    tagService.addTagToTVShow(tvShowId, tagId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/admin/tvshow/{tvShowId}/tag/{tagId}")
  public ResponseEntity<Void> removeTagFromTVShow(@PathVariable String tvShowId,
                                                   @PathVariable String tagId) {
    tagService.removeTagFromTVShow(tvShowId, tagId);
    return ResponseEntity.noContent().build();
  }
}
