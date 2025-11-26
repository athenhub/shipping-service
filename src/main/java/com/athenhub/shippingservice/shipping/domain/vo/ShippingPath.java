package com.athenhub.shippingservice.shipping.domain.vo;

import java.util.List;

public record ShippingPath(
    HubId sourceHubId,
    HubId targetHubId,
    String streetAddress,
    String detailAddress,
    List<ShippingSubPath> subPaths,
    ShippingAgentId shippingAgentId) {}
