package com.athenhub.shippingservice.shipping.domain;

import static com.athenhub.shippingservice.ShippingFixture.create;
import static com.athenhub.shippingservice.ShippingFixture.createSubPath;
import static org.assertj.core.api.Assertions.assertThat;

import com.athenhub.shippingservice.shipping.domain.vo.ShippingSubPath;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingRouteTest {

  ShippingRoute route;

  @BeforeEach
  void setUp() {
    route = ShippingRoute.create(createSubPath(UUID.randomUUID()));
  }

  @Test
  void createTest() {
    ShippingSubPath path = createSubPath(UUID.randomUUID());

    ShippingRoute route = ShippingRoute.create(path);

    assertThat(route.getSequence()).isEqualTo(1);
    assertThat(route.getSourceHubId()).isEqualTo(path.sourceHubId());
    assertThat(route.getTargetHubId()).isEqualTo(path.targetHubId());
    assertThat(route.getExpectedDistance()).isEqualTo(36.5);
    assertThat(route.getExpectedDuration()).isEqualTo(30);
    assertThat(route.getStatus()).isEqualTo(ShippingRouteStatus.WAITING);
    assertThat(route.getShippingAgentId()).isEqualTo(path.shippingAgentId());
  }

  @Test
  void assignTo() {
    Shipping shipping = create();

    route.assignTo(shipping);

    assertThat(route.getShipping()).isEqualTo(shipping);
  }

  @Test
  void startMove() {
    route.startMove();

    assertThat(route.getStatus()).isEqualTo(ShippingRouteStatus.MOVING);
  }

  @Test
  void arriveHub() {
    route.arriveHub();

    assertThat(route.getStatus()).isEqualTo(ShippingRouteStatus.ARRIVED);
  }

  @Test
  void updateActualValues() {
    route.updateActualValues(37.1, 34);

    assertThat(route.getActualDistance()).isEqualTo(37.1);
    assertThat(route.getActualDuration()).isEqualTo(34);
  }
}
