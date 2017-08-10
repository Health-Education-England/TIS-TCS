package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {})
public interface PostSiteMapper {

  PostSiteDTO postSiteToPostSiteDTO(PostSite postSite);

  PostSite postSiteDTOToPostSite(PostSiteDTO postSiteDTO);

  Set<PostSiteDTO> postSitesToPostSiteDTOs(Set<PostSite> postSite);

  Set<PostSite> postSiteDTOsToPostSites(Set<PostSiteDTO> postSiteDTO);
}
