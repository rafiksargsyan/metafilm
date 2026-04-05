package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;

public record MovieImageDTO(
    ImageType type,
    String url,
    String blurhash,
    String externalSource,
    String externalPath
) {}
