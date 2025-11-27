package com.athenhub.shippingservice.shipping.infrastructure.client;

import com.athenhub.shippingservice.shipping.infrastructure.dto.HubInfo;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubRouteResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Hub 관련 외부 서비스 호출을 위한 Feign 클라이언트.
 *
 * <p>허브 서비스(hub-service)와 통신하여 특정 회원의 상세 정보를 조회한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@FeignClient("hub-service")
public interface HubServiceClient {
  /**
   * 지정된 허브의 상세 정보를 조회한다.
   *
   * @param hubId 허브 식별자(UUID)
   * @return 멤버 정보 객체 {@link HubInfo}
   */
  @GetMapping("v1/hubs/{hubId}")
  HubInfo getHubInfo(@PathVariable("hubId") UUID hubId);

  @GetMapping("v1/hubs")
  Page<HubInfo> getHubInfos(@RequestParam("page") int page, @RequestParam("size") int size);

  /**
   * 허브 간 경로 정보를 조회한다.
   *
   * @return 허브 간 경로 {@link HubRouteResponse} 목록
   */
  @GetMapping("v1/routes")
  List<HubRouteResponse> getHubRoutes();

  /** */
  @GetMapping("v1/hubs/{hubId}/routes")
  List<HubRouteResponse> getHubRoutes(@PathVariable("hubId") UUID hubId);
}
