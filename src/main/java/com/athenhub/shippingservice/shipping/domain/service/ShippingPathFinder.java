package com.athenhub.shippingservice.shipping.domain.service;

import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import java.util.UUID;

public interface ShippingPathFinder {
  ShippingPath find(UUID sourceHubId, String targetAddress);
}
