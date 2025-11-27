package com.athenhub.shippingservice.shipping.application.service;

import com.athenhub.shippingservice.shipping.domain.event.ShippingCreated;

/**
 * 허브(Hub) 관련 도메인 이벤트 메세지를 발행(publish)하기 위한 인터페이스.
 *
 * <p>해당 인터페이스는 허브 생성, 수정, 삭제, 관리자 변경과 같은 주요 도메인 이벤트 메세지를 외부 메시지 브로커(Kafka, RabbitMQ 등) 또는 내부 이벤트
 * 시스템으로 전달하는 역할을 정의한다.
 *
 * <p>이 인터페이스는 구현체에서 실제 이벤트 메세지 브로커 연동 방식을 결정하도록 하며, 도메인 계층은 구현 방식에 영향을 받지 않고 이벤트 메세지 발행 기능을 사용할 수
 * 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface ShippingMessagePublisher {
  /**
   * 신규 허브가 등록되었을 때 발행되는 이벤트 메세지를 전송한다.
   *
   * @param event 등록된 허브 정보를 담은 {@link ShippingCreated} 이벤트
   */
  void publish(ShippingCreated event);
}
