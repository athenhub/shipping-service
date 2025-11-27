package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.ShippingRepository;
import com.athenhub.shippingservice.shipping.domain.exception.PermissionErrorCode;
import com.athenhub.shippingservice.shipping.domain.exception.PermissionException;
import com.athenhub.shippingservice.shipping.domain.service.PermissionChecker;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingRouteId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingManageService implements ShippingManager {

  private final ShippingFinder shippingFinder;

  private final ShippingRepository shippingRepository;

  private final PermissionChecker permissionChecker;

  @Override
  public void moveHub(UUID shippingId, Long shippingRouteId, UUID requestId) {
    Shipping shipping = shippingFinder.find(shippingId);

    ShippingRouteId routeId = ShippingRouteId.of(shippingRouteId);

    checkUpdatePermission(
        requestId, shipping.getHubShippingAgent(routeId), shipping.getSourceHubId());

    shipping.moveHub(routeId);

    shippingRepository.save(shipping);
  }

  @Override
  public void arriveHub(UUID shippingId, Long shippingRouteId, UUID requestId) {
    Shipping shipping = shippingFinder.find(shippingId);

    ShippingRouteId routeId = ShippingRouteId.of(shippingRouteId);

    checkUpdatePermission(
        requestId, shipping.getHubShippingAgent(routeId), shipping.getSourceHubId());

    shipping.arriveHub(routeId);

    shippingRepository.save(shipping);
  }

  @Override
  public void moveVendor(UUID shippingId, UUID requestId) {
    Shipping shipping = shippingFinder.find(shippingId);

    checkUpdatePermission(requestId, shipping.getVendorShippingAgent(), shipping.getSourceHubId());

    shipping.moveVendor();

    shippingRepository.save(shipping);
  }

  @Override
  public void arriveVendor(UUID shippingId, UUID requestId) {
    Shipping shipping = shippingFinder.find(shippingId);

    checkUpdatePermission(requestId, shipping.getVendorShippingAgent(), shipping.getSourceHubId());

    shipping.arriveVendor();

    shippingRepository.save(shipping);
  }

  private void checkUpdatePermission(UUID requestId, ShippingAgentId shippingAgentId, HubId hubId) {
    if (!permissionChecker.hasUpdatePermission(requestId, shippingAgentId, hubId)) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_UPDATE_PERMISSION);
    }
  }
}
