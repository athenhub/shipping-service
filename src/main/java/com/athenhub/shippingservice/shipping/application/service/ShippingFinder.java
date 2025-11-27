package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.Shipping;
import java.util.UUID;

/**
 * 배송(Shipping) 조회를 담당하는 도메인 서비스 인터페이스.
 *
 * <p>배송 식별자 {@code shippingId}를 기반으로 등록된 배송 엔티티를 조회하며, 조회 실패 시 도메인 규칙에 따라 예외를 발생시킬 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface ShippingFinder {
  /**
   * 배송을 단건 조회한다.
   *
   * @param shippingId 조회할 배송 식별자
   * @return 조회된 {@link Shipping} 엔티티
   */
  Shipping find(UUID shippingId);
}
