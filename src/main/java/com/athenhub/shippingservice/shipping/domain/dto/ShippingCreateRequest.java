package com.athenhub.shippingservice.shipping.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record ShippingCreateRequest(
    @NotNull UUID orderId,
    @NotNull UUID sourceHubId,
    @NotBlank String streetAddress,
    String detailAddress,
    @NotNull UUID receiverId,
    @NotBlank String receiverName,
    @NotBlank String receiverSlackId,
    LocalDateTime shippingDueAt,
    String shippingRequestMemo) {}
