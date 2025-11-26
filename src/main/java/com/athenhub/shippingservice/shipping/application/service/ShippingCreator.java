package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import jakarta.validation.Valid;

public interface ShippingCreator {
  Shipping register(@Valid ShippingCreateRequest createRequest);
}
