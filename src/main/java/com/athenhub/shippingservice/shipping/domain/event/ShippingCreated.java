package com.athenhub.shippingservice.shipping.domain.event;

import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.OrderId;
import java.util.List;

public record ShippingCreated(OrderId orderId, HubId sourceHubId, List<HubId> stopoverHubIds) {}
