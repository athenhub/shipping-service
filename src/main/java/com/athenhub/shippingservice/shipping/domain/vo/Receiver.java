package com.athenhub.shippingservice.shipping.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Receiver {
  @Embedded
  private ReceiverId id;
  @Column(name = "receiver_name", nullable = false)
  private String name;
  @Column(name = "receiver_slack_id", nullable = false)
  private String slackId;

  public static Receiver of(ReceiverId id, String name, String slackId) {
    return new Receiver(id, name, slackId);
  }
}
