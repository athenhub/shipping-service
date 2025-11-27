package com.athenhub.shippingservice.shipping.infrastructure.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 설정.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RefreshScope
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitOrderProperties.class)
public class RabbitOrderConfig {

  private final RabbitOrderProperties rabbitProperties;

  /** order 관련 이벤트를 발행하는 Topic Exchange. */
  @Bean
  public TopicExchange orderExchange() {
    return new TopicExchange(rabbitProperties.getExchange(), true, false);
  }

  /** order 이벤트를 수신할 Queue. */
  @Bean
  public Queue orderQueue() {
    return QueueBuilder.durable(rabbitProperties.getQueue()).build();
  }

  /**
   * Exchange와 Queue를 Binding 한다.
   *
   * <p>routing key를 통해 어떤 메시지를 수신할지 결정된다.
   */
  @Bean
  public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
    return BindingBuilder.bind(orderQueue).to(orderExchange).with(rabbitProperties.getRoutingKey());
  }
}
