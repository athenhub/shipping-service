package com.athenhub.shippingservice.shipping.application.service;

import static com.athenhub.shippingservice.ShippingFixture.createPath;
import static com.athenhub.shippingservice.ShippingFixture.createRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.ShippingStatus;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.exception.PermissionException;
import com.athenhub.shippingservice.shipping.domain.service.PermissionChecker;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingRouteId;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ShippingManagerTest {
  @Autowired private ShippingCreator shippingCreator;
  @Autowired private ShippingManager shippingManager;
  @Autowired private ShippingFinder shippingFinder;
  @Autowired private EntityManager em;
  @MockitoBean private ShippingPathFinder pathFinder;
  @MockitoBean private PermissionChecker permissionChecker;

  Shipping shipping;

  private final UUID requestId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    shipping = create();
  }

  @Test
  void moveHub() {
    ShippingRouteId routeId = shipping.getRoutes().getFirst().getId();
    when(permissionChecker.hasUpdatePermission(
            any(UUID.class), any(ShippingAgentId.class), any(HubId.class)))
        .thenReturn(true);

    shippingManager.moveHub(shipping.getId().toUuid(), routeId.value(), requestId);
    em.flush();
    em.clear();

    shipping = shippingFinder.find(shipping.getId().toUuid());

    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.MOVING_BETWEEN_HUBS);
  }

  @Test
  void moveHubHasNotPermission() {
    ShippingRouteId routeId = shipping.getRoutes().getFirst().getId();
    when(permissionChecker.hasUpdatePermission(
            any(UUID.class), any(ShippingAgentId.class), any(HubId.class)))
        .thenReturn(false);

    assertThatThrownBy(
            () -> shippingManager.moveHub(shipping.getId().toUuid(), routeId.value(), requestId))
        .isInstanceOf(PermissionException.class);
  }

  private Shipping create() {
    ShippingCreateRequest request = createRequest();
    when(permissionChecker.hasCreatePermission(any(UUID.class))).thenReturn(true);
    when(pathFinder.find(any(UUID.class), anyString()))
        .thenReturn(createPath(request.sourceHubId()));

    Shipping shipping = shippingCreator.create(request, requestId);
    em.flush();
    em.clear();

    return shipping;
  }
}
