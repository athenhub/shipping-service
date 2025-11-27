package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.ShippingRepository;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShippingQueryService implements ShippingFinder {
  private final ShippingRepository shippingRepository;

  @Override
  public Shipping find(UUID shippingId) {
    return shippingRepository
        .findById(ShippingId.of(shippingId))
        .orElseThrow(
            () -> new IllegalArgumentException("배송 정보를 찾을수 없습니다. id: " + shippingId.toString()));
  }
}
