package com.athenhub.shippingservice.shipping.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 주문 식별자(OrderId)를 표현하는 값 객체(Value Object).
 *
 * <p>UUID 기반의 주문 ID를 래핑하며, JPA에서 임베디드 타입으로 사용된다. 주문가 여러 도메인에서 공유되는 식별자이므로 VO 형태로 캡슐화한다.
 *
 * <h2>생성 규칙</h2>
 *
 * <ul>
 *   <li>{@link #of(UUID)} — 기존 UUID를 이용하여 OrderId 생성
 * </ul>
 *
 * <p>null 값은 허용되지 않으며, null 전달 시 예외가 발생한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderId {

  @Column(name = "order_id", nullable = false)
  private UUID id;

  /**
   * 주어진 UUID로 OrderId를 생성한다.
   *
   * @param id 주문 식별자 UUID
   * @return {@link OrderId} 인스턴스
   * @throws NullPointerException id가 null인 경우
   */
  public static OrderId of(UUID id) {
    return new OrderId(Objects.requireNonNull(id));
  }

  /**
   * 내부 UUID 값을 반환한다.
   *
   * @return UUID 값
   */
  public UUID toUuid() {
    return id;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
