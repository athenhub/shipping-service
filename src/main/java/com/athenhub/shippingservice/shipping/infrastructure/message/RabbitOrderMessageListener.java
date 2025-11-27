package com.athenhub.shippingservice.shipping.infrastructure.message;

import com.athenhub.shippingservice.shipping.application.service.ShippingCreateService;
import com.athenhub.shippingservice.shipping.domain.dto.ShippingCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitOrderMessageListener {

  private final ShippingCreateService shippingCreateService;

  @RabbitListener(queues = "athenhub.order.shipping.queue")
  public void handleMessage(ShippingCreateRequest request) {
    shippingCreateService.create(request, request.receiverId());
  }
}
