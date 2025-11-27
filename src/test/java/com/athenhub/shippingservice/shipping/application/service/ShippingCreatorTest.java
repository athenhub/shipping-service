package com.athenhub.shippingservice.shipping.application.service;

import static com.athenhub.shippingservice.ShippingFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import com.athenhub.shippingservice.shipping.domain.ShippingStatus;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.exception.PermissionException;
import com.athenhub.shippingservice.shipping.domain.service.PermissionChecker;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ShippingCreatorTest {
  @Autowired private ShippingCreator shippingCreator;
  @Autowired private ShippingFinder shippingFinder;
  @Autowired private EntityManager em;
  @MockitoBean private ShippingPathFinder pathFinder;
  @MockitoBean private PermissionChecker permissionChecker;

  private final UUID requestId = UUID.randomUUID();

  @Test
  void createTest() {
    ShippingCreateRequest request = createRequest();
    when(permissionChecker.hasCreatePermission(any(UUID.class))).thenReturn(true);
    when(pathFinder.find(any(UUID.class), anyString()))
        .thenReturn(createPath(request.sourceHubId()));

    Shipping shipping = shippingCreator.create(request, requestId);
    em.flush();
    em.clear();

    shipping = shippingFinder.find(shipping.getId().toUuid());
    assertThat(shipping.getId()).isNotNull();
    assertThat(shipping.getStatus()).isEqualTo(ShippingStatus.WAITING_AT_HUB);
  }

  @Test
  void createHasNotPermission() {
    ShippingCreateRequest request = createRequest();
    when(permissionChecker.hasCreatePermission(any(UUID.class))).thenReturn(false);

    assertThatThrownBy(() -> shippingCreator.create(request, requestId))
        .isInstanceOf(PermissionException.class);
  }
}
