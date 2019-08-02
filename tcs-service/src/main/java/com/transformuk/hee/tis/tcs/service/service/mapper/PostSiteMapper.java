package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PostSiteMapper {

  PostSiteDTO postSiteToPostSiteDTO(PostSite postSite);

  PostSite postSiteDTOToPostSite(PostSiteDTO postSiteDTO);

  List<PostSiteDTO> postSitesToPostSiteDTOs(List<PostSite> postSite);

  List<PostSite> postSiteDTOsToPostSites(List<PostSiteDTO> postSiteDTO);
}
