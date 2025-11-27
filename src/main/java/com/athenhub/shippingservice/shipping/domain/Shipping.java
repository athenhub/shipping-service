package com.athenhub.shippingservice.shipping.domain;

import com.athenhub.shippingservice.global.domain.AbstractAuditEntity;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import com.athenhub.shippingservice.shipping.domain.vo.Address;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.OrderId;
import com.athenhub.shippingservice.shipping.domain.vo.Receiver;
import com.athenhub.shippingservice.shipping.domain.vo.ReceiverId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingRouteId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Table(name = "p_shipping")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shipping extends AbstractAuditEntity {

  @EmbeddedId private ShippingId id;

  @Embedded private OrderId orderId;

  @Enumerated(EnumType.STRING)
  private ShippingStatus status;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "source_hub_id", nullable = false))
  private HubId sourceHubId;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "target_hub_id", nullable = false))
  private HubId targetHubId;

  @Embedded private Address address;

  @Embedded private Receiver receiver;

  @Getter(AccessLevel.NONE)
  @Embedded
  private ShippingAgentId shippingAgentId;

  private LocalDateTime shippingDueAt;

  private String shippingRequestMemo;

  @Getter(AccessLevel.NONE)
  @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShippingRoute> routes = new ArrayList<>();

  public static Shipping create(
      ShippingCreateRequest registerRequest, ShippingPathFinder pathFinder) {
    Shipping shipping = new Shipping();

    shipping.id = ShippingId.generateId();
    shipping.orderId = OrderId.of(registerRequest.orderId());
    shipping.status = ShippingStatus.WAITING_AT_HUB;
    shipping.receiver =
        Receiver.of(
            ReceiverId.of(registerRequest.receiverId()),
            registerRequest.receiverName(),
            registerRequest.receiverSlackId());
    shipping.shippingDueAt = registerRequest.shippingDueAt();
    shipping.shippingRequestMemo = registerRequest.shippingRequestMemo();

    setShippingInfo(registerRequest, pathFinder, shipping);

    return shipping;
  }

  public void moveHub(ShippingRouteId routeId) {
    this.routes.stream()
        .filter(r -> r.getId().equals(routeId))
        .findFirst()
        .ifPresent(ShippingRoute::startMove);

    this.status = ShippingStatus.MOVING_BETWEEN_HUBS;
  }

  public void arriveHub(ShippingRouteId routeId) {
    this.routes.stream()
        .filter(r -> r.getId().equals(routeId))
        .findFirst()
        .ifPresent(ShippingRoute::arriveHub);

    this.status = ShippingStatus.ARRIVED_AT_HUB;
  }

  public void moveVendor() {
    this.status = ShippingStatus.MOVING_TO_VENDOR;
  }

  public void arriveVendor() {
    this.status = ShippingStatus.COMPLETED;
  }

  public ShippingAgentId getVendorShippingAgent() {
    return shippingAgentId;
  }

  public ShippingAgentId getHubShippingAgent(ShippingRouteId routeId) {
    ShippingRoute route =
        this.routes.stream()
            .filter(r -> r.getId().equals(routeId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("잘못된 허브 경로 ID 입니다. id: " + routeId));

    return route.getShippingAgentId();
  }

  public List<ShippingRoute> getRoutes() {
    return List.copyOf(this.routes);
  }

  private static void setShippingInfo(
      ShippingCreateRequest registerRequest, ShippingPathFinder pathFinder, Shipping shipping) {
    ShippingPath shippingPath =
        pathFinder.find(registerRequest.sourceHubId(), registerRequest.streetAddress());

    shipping.sourceHubId = shippingPath.sourceHubId();
    shipping.targetHubId = shippingPath.targetHubId();
    shipping.address = Address.of(shippingPath.streetAddress(), registerRequest.detailAddress());
    shipping.shippingAgentId = shippingPath.shippingAgentId();

    shippingPath
        .subPaths()
        .forEach(
            subPath -> {
              ShippingRoute route = ShippingRoute.create(subPath);
              shipping.addRoute(route);
            });
  }

  private void addRoute(ShippingRoute route) {
    this.routes.add(route);
    route.assignTo(this);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Shipping shipping = (Shipping) o;
    return getId() != null && Objects.equals(getId(), shipping.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}
