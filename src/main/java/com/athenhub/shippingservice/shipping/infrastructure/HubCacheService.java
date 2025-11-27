package com.athenhub.shippingservice.shipping.infrastructure;

import com.athenhub.shippingservice.shipping.infrastructure.dto.HubInfo;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubRouteResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubCacheService {

  private final CacheManager cacheManager;

  private static final String HUB_CACHE = "hubs";
  private static final String HUB_ROUTE_CACHE_NAME = "hubRoutes";

  /** 캐시 전체 갱신 */
  @CachePut(value = HUB_ROUTE_CACHE_NAME)
  public List<HubRouteResponse> updateRoutes(List<HubRouteResponse> routes) {
    return routes;
  }

  /** 캐시 전체 조회 */
  @Cacheable(value = HUB_ROUTE_CACHE_NAME)
  public List<HubRouteResponse> getRoutes() {
    return List.of(); // 최초 조회 시 빈값 → 이후 updateCache 호출 시 값이 채워짐
  }

  @CachePut(value = HUB_CACHE)
  public List<HubInfo> updateHubs(List<HubInfo> hubs) {
    return hubs;
  }

  @Cacheable(value = HUB_CACHE)
  public List<HubInfo> getHubs() {
    return List.of(); // 캐시에 데이터 없을 경우 기본값
  }
}
