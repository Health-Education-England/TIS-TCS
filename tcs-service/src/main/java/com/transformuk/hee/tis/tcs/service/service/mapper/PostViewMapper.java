/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for post elastic search index.
 */
@Mapper(componentModel = "spring")
public interface PostViewMapper {

  @Mapping(source = "currentTraineeSurnames", target = "currentTraineeSurname")
  @Mapping(source = "programmeNames", target = "programmeNames", qualifiedByName = "joinWithSemicolon")
  @Mapping(source = "fundingTypes", target = "fundingType", qualifiedByName = "joinWithSemicolon")
  PostViewDTO toDto(PostView postView);

  List<PostViewDTO> toDtos(List<PostView> postViews);

  @Named("joinWithSemicolon")
  default String joinWithSemicolon(List<String> values) {
    if (CollectionUtils.isEmpty(values)) {
      return "";
    }

    return String.join("; ", values);
  }
}
