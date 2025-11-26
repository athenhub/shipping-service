package com.athenhub.shippingservice.shipping.domain;

import com.athenhub.shippingservice.global.domain.AbstractAuditEntity;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingSubPath;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Table(name = "p_shipping_route")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingRoute extends AbstractAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_id")
  private Shipping shipping;

  @Column(nullable = false)
  private Integer sequence;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "source_hub_id", nullable = false))
  private HubId sourceHubId;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "target_hub_id", nullable = false))
  private HubId targetHubId;

  @Column(nullable = false)
  private Double expectedDistance;

  @Column(nullable = false)
  private Integer expectedDuration;

  private Double actualDistance;

  private Integer actualDuration;

  @Enumerated(EnumType.STRING) private ShippingRouteStatus status;

  @Embedded private ShippingAgentId shippingAgentId;

  public static ShippingRoute create(ShippingSubPath subPath) {
    ShippingRoute shippingRoute = new ShippingRoute();

    shippingRoute.sequence = subPath.sequence();
    shippingRoute.sourceHubId = subPath.sourceHubId();
    shippingRoute.targetHubId = subPath.targetHubId();
    shippingRoute.expectedDistance = subPath.expectedDistance();
    shippingRoute.expectedDuration = subPath.expectedDuration();
    shippingRoute.status = ShippingRouteStatus.WAITING;
    shippingRoute.shippingAgentId = subPath.shippingAgentId();

    return shippingRoute;
  }

  public void assignTo(Shipping shipping) {
    this.shipping = shipping;
  }

  public void startMove() {
    this.status = ShippingRouteStatus.MOVING;
  }

  public void arriveHub() {
    this.status = ShippingRouteStatus.ARRIVED;
  }

  public void updateActualValues(Double actualDistance, Integer actualDuration) {
    this.actualDistance = actualDistance;
    this.actualDuration = actualDuration;
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
    ShippingRoute shipping = (ShippingRoute) o;
    return getId() != null && Objects.equals(getId(), shipping.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}
