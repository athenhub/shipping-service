package com.athenhub.shippingservice.shipping.domain;

public enum ShippingStatus {
  WAITING_AT_HUB,
  MOVING_BETWEEN_HUBS,
  ARRIVED_AT_HUB,
  MOVING_TO_VENDOR,
  COMPLETED
}
