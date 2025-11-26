package com.athenhub.shippingservice.shipping.domain;

import com.athenhub.shippingservice.global.domain.AbstractAuditEntity;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import com.athenhub.shippingservice.shipping.domain.vo.Address;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.OrderId;
import com.athenhub.shippingservice.shipping.domain.vo.Receiver;
import com.athenhub.shippingservice.shipping.domain.vo.ReceiverId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingId;
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

  @Enumerated(EnumType.STRING) private ShippingStatus status;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "source_hub_id", nullable = false))
  private HubId sourceHubId;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "target_hub_id", nullable = false))
  private HubId targetHubId;

  @Embedded private Address address;

  @Embedded private Receiver receiver;

  @Embedded private ShippingAgentId shippingAgentId;

  private LocalDateTime shippingDueAt;

  private String shippingRequestMemo;

  @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShippingRoute> routes = new ArrayList<>();

  public static Shipping create(ShippingCreateRequest registerRequest) {
    Shipping shipping = new Shipping();

    shipping.id = ShippingId.generateId();
    shipping.orderId = OrderId.of(registerRequest.orderId());
    shipping.status = ShippingStatus.WAITING_AT_HUB;
    shipping.receiver = Receiver.of(ReceiverId.of(registerRequest.receiverId()), registerRequest.receiverName(),
        registerRequest.receiverSlackId());
    shipping.shippingDueAt = registerRequest.shippingDueAt();
    shipping.shippingRequestMemo = registerRequest.shippingRequestMemo();

    ShippingPath shippingPath = registerRequest.shippingPath();

    shipping.sourceHubId = shippingPath.sourceHubId();
    shipping.targetHubId = shippingPath.targetHubId();
    shipping.address = Address.of(shippingPath.streetAddress(), shippingPath.detailAddress());
    shipping.shippingAgentId = shippingPath.shippingAgentId();

    shippingPath.subPaths().forEach(subPath -> {
      ShippingRoute route = ShippingRoute.create(subPath);
      shipping.addRoute(route);
    });

    return shipping;
  }

  public void addRoute(ShippingRoute route) {
    routes.add(route);
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
