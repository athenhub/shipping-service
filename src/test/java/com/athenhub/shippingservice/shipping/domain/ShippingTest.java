package com.athenhub.shippingservice.shipping.domain;

import static com.athenhub.shippingservice.ShippingFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import com.athenhub.shippingservice.shipping.domain.vo.Address;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.OrderId;
import com.athenhub.shippingservice.shipping.domain.vo.Receiver;
import com.athenhub.shippingservice.shipping.domain.vo.ReceiverId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingTest {
  Shipping shipping;

  @BeforeEach
  void setUp() {
    shipping = create();
  }

  @Test
  void createTest() {
    ShippingCreateRequest request = createRequest();

    ShippingPathFinder pathFinder = mock(ShippingPathFinder.class);
    ShippingPath shippingPath = createPath(request.sourceHubId());
    when(pathFinder.find(request.sourceHubId(), request.streetAddress())).thenReturn(shippingPath);

    Shipping shipping = Shipping.create(request, pathFinder);

    assertThat(shipping.getId()).isNotNull();
    assertThat(shipping.getOrderId()).isEqualTo(OrderId.of(request.orderId()));
    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.WAITING_AT_HUB);
    assertThat(shipping.getSourceHubId()).isEqualTo(HubId.of(request.sourceHubId()));
    assertThat(shipping.getTargetHubId()).isEqualTo(shippingPath.targetHubId());
    assertThat(shipping.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(shipping.getReceiver())
        .isEqualTo(
            Receiver.of(
                ReceiverId.of(request.receiverId()),
                request.receiverName(),
                request.receiverSlackId()));
    assertThat(shipping.getVendorShippingAgent()).isEqualTo(shippingPath.shippingAgentId());
    assertThat(shipping.getShippingDueAt()).isEqualTo(request.shippingDueAt());
    assertThat(shipping.getShippingRequestMemo()).isEqualTo(request.shippingRequestMemo());
    assertThat(shipping.getRoutes()).hasSize(1);
  }

  @Test
  void moveHub() {
    ShippingRoute route = shipping.getRoutes().getFirst();

    shipping.moveHub(route.getId());

    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.MOVING_BETWEEN_HUBS);
    assertThat(route.getStatus()).isEqualTo(ShippingRouteStatus.MOVING);
  }

  @Test
  void arriveHub() {
    ShippingRoute route = shipping.getRoutes().getFirst();

    shipping.arriveHub(route.getId());

    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.ARRIVED_AT_HUB);
    assertThat(route.getStatus()).isEqualTo(ShippingRouteStatus.ARRIVED);
  }

  @Test
  void moveVendor() {
    shipping.moveVendor();

    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.MOVING_TO_VENDOR);
  }

  @Test
  void arriveVendor() {
    shipping.arriveVendor();

    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.COMPLETED);
  }
}
