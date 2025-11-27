package com.athenhub.shippingservice.shipping.infrastructure.message;

import com.athenhub.shippingservice.shipping.infrastructure.HubCacheService;
import com.athenhub.shippingservice.shipping.infrastructure.client.HubServiceClient;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubInfo;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubRouteResponse;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubRouteUpdated;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitHubMessageListener {

  private final HubServiceClient hubServiceClient;
  private final HubCacheService hubRouteCacheService;

  @RabbitListener(queues = "athenhub.hub.queue")
  public void handleMessage(HubRouteUpdated request) {
    List<HubRouteResponse> routes = hubServiceClient.getHubRoutes();
    hubRouteCacheService.updateRoutes(routes);

    Page<HubInfo> response = hubServiceClient.getHubInfos(0, 50);
    hubRouteCacheService.updateHubs(response.getContent());
  }
}
