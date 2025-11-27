package com.athenhub.shippingservice.shipping.infrastructure;

import com.athenhub.shippingservice.shipping.domain.service.ShippingAgentAssigner;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingAgentId;
import com.athenhub.shippingservice.shipping.infrastructure.client.MemberServiceClient;
import com.athenhub.shippingservice.shipping.infrastructure.dto.MemberInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShippingAgentAssignService implements ShippingAgentAssigner {

  private final MemberServiceClient memberServiceClient;

  @Override
  public ShippingAgentId assignHubShippingAgent() {
    MemberInfo agentInfo = memberServiceClient.getHubShippingAgentInfo();

    return ShippingAgentId.of(agentInfo.id());
  }

  @Override
  public ShippingAgentId assignVendorShippingAgent(UUID hubId) {
    MemberInfo agentInfo = memberServiceClient.getVendorShippingAgentInfo(hubId);

    return ShippingAgentId.of(agentInfo.id());
  }
}
