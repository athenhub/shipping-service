package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.ShippingRepository;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.exception.PermissionErrorCode;
import com.athenhub.shippingservice.shipping.domain.exception.PermissionException;
import com.athenhub.shippingservice.shipping.domain.service.PermissionChecker;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingCreateService implements ShippingCreator {

  private final ShippingRepository shippingRepository;

  private final ShippingPathFinder shippingPathFinder;

  private final PermissionChecker permissionChecker;

  @Override
  public Shipping create(ShippingCreateRequest createRequest, UUID requestId) {
    checkCreatePermission(requestId);

    Shipping shipping = Shipping.create(createRequest, shippingPathFinder);

    shipping = shippingRepository.save(shipping);

    return shipping;
  }

  private void checkCreatePermission(UUID requestId) {
    if (!permissionChecker.hasCreatePermission(requestId)) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_CREATE_PERMISSION);
    }
  }
}
