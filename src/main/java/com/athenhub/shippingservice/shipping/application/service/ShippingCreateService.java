package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.service.PermissionChecker;
import com.athenhub.shippingservice.shipping.domain.service.ShippingAgentAssigner;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingCreateService  implements ShippingCreator{

  private final ShippingPathFinder shippingPathFinder;

  private final ShippingAgentAssigner shippingAgentAssigner;

  private final PermissionChecker permissionChecker;

  @Override
  public Shipping register(ShippingCreateRequest createRequest, UUID requestId) {
    if(permissionChecker.hasCreatePermission(requestId))
  }
}
