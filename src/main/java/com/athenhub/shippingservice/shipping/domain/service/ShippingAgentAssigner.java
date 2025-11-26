package com.athenhub.shippingservice.shipping.domain.service;

import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import java.util.UUID;

public interface ShippingAgentAssigner {
  public ShippingAgentId assignHubShippingAgent();
  public ShippingAgentId assignVendorShippingAgent(UUID hubId);
}
