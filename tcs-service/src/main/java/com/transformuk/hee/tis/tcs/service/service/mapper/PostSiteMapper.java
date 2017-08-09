package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import org.mapstruct.Mapper;

import java.util.Set;

/**
 * Mapper for the entity PostSite and its DTO PostSiteDTO
 */
//@Mapper(componentModel = "spring", uses = {
//})
public interface PostSiteMapper {

  PostSiteDTO postSiteToPostSiteDTO(PostSite postSite);

  Set<PostSiteDTO> postSiteToPostSiteDTOs(Set<PostSite> postSites);

  PostSite postSiteDTOToPostSite(PostSiteDTO postSiteDTO);

  Set<PostSite> postSiteDTOToPostSites(Set<PostSiteDTO> postSiteDTOs);

}
