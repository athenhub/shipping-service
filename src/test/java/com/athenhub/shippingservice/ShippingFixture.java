package com.athenhub.shippingservice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingSubPath;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ShippingFixture {
  public static ShippingSubPath createSubPath(UUID sourceHubId) {
    return new ShippingSubPath(
        1,
        HubId.of(sourceHubId),
        HubId.of(UUID.randomUUID()),
        36.5,
        30,
        ShippingAgentId.of(UUID.randomUUID()));
  }

  public static ShippingPath createPath(UUID sourceHubId) {
    ShippingSubPath subPath = createSubPath(sourceHubId);
    return new ShippingPath(
        HubId.of(sourceHubId),
        subPath.targetHubId(),
        "서울특별시 송파구 송파대로 55",
        List.of(subPath),
        ShippingAgentId.of(UUID.randomUUID()));
  }

  public static ShippingCreateRequest createRequest() {
    return new ShippingCreateRequest(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "서울특별시 송파구 송파대로 55",
        "3층 TECH3",
        UUID.randomUUID(),
        "수령인",
        "testSlackId",
        LocalDateTime.now(),
        "");
  }

  public static Shipping create() {
    return create(createRequest());
  }

  public static Shipping create(ShippingCreateRequest request) {
    ShippingPathFinder pathFinder = mock(ShippingPathFinder.class);
    when(pathFinder.find(request.sourceHubId(), request.streetAddress()))
        .thenReturn(createPath(request.sourceHubId()));

    return Shipping.create(request, pathFinder);
  }
}
