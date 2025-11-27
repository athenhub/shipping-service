package com.athenhub.shippingservice.shipping.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingRouteId {
  private Long id;

  private ShippingRouteId(Long id) {
    this.id = id;
  }

  public static ShippingRouteId of(Long id) {
    return new ShippingRouteId(id);
  }

  public Long value() {
    return id;
  }
}
