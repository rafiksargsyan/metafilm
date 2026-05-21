package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.TagService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class PublicTagController {

  private final TagService tagService;

  @Autowired
  public PublicTagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public ResponseEntity<List<TagDTO>> listTags() {
    return ResponseEntity.ok(tagService.listTags());
  }
}
