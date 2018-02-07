package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.EsrNotificationDTO;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity EsrNotification and its DTO EsrNotificationDTO.
 *
 */
@Mapper(componentModel = "spring", uses = {})
public interface EsrNotificationMapper {

  EsrNotificationDTO esrNotificationToEsrNotificationDTO(EsrNotification placementDetails);

  List<EsrNotificationDTO> esrNotificationsToPlacementDetailDTOs(List<EsrNotification> placementDetails);

  EsrNotification esrNotificationDTOToEsrNotification(EsrNotificationDTO placementDetailsDTO);

  List<EsrNotification> esrNotificationDTOsToEsrNotifications(List<EsrNotificationDTO> placementDetailsDTOs);

}
