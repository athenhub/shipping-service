package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface ShippingCreator {
  Shipping create(@Valid ShippingCreateRequest createRequest, @NotNull UUID requestId);
}
