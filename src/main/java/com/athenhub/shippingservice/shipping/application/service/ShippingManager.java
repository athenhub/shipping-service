package com.athenhub.shippingservice.shipping.application.service;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface ShippingManager {

  void moveHub(@NotNull UUID shippingId, @NotNull Long shippingRouteId, @NotNull UUID requestId);

  void arriveHub(@NotNull UUID shippingId, @NotNull Long shippingRouteId, @NotNull UUID requestId);

  void moveVendor(@NotNull UUID shippingId, @NotNull UUID requestId);

  void arriveVendor(@NotNull UUID shippingId, @NotNull UUID requestId);
}
