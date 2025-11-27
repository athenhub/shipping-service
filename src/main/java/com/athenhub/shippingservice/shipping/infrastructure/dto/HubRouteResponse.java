package com.athenhub.shippingservice.shipping.infrastructure.dto;

import java.util.UUID;

public record HubRouteResponse(
    UUID sourceHubId, UUID targetHubId, Double distanceKm, Integer durationMinutes) {}
