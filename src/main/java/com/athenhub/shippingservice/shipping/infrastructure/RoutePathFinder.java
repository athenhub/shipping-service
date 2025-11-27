package com.athenhub.shippingservice.shipping.infrastructure;

import com.athenhub.shippingservice.shipping.domain.service.ShippingAgentAssigner;
import com.athenhub.shippingservice.shipping.domain.service.ShippingPathFinder;
import com.athenhub.shippingservice.shipping.domain.vo.HubId;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import com.athenhub.shippingservice.shipping.domain.vo.ShippingSubPath;
import com.athenhub.shippingservice.shipping.infrastructure.client.NaverGeocodeClient;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubInfo;
import com.athenhub.shippingservice.shipping.infrastructure.dto.HubRouteResponse;
import com.athenhub.shippingservice.shipping.infrastructure.dto.NaverCoordinateResponse;
import com.athenhub.shippingservice.shipping.infrastructure.dto.RouteResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoutePathFinder implements ShippingPathFinder {

  private final NaverGeocodeClient naverGeocodeClient;
  private final RouteCalculator routeCalculator;
  private final HubCacheService hubCacheService;
  private final ShippingAgentAssigner shippingAgentAssigner;

  @Override
  public ShippingPath find(UUID sourceHubId, String targetAddress) {
    // 목적지 좌표 조회
    NaverCoordinateResponse coordinate = naverGeocodeClient.getCoordinate(targetAddress, 1);
    Coordinate targetCoordinate =
        Coordinate.of(coordinate.getLatitude(), coordinate.getLongitude());

    // 출발 허브 ~ 목적지 경로 계산
    HubInfo sourceHub = findBy(sourceHubId);
    RouteResponse route =
        routeCalculator.getRoute(
            Coordinate.of(sourceHub.latitude(), sourceHub.longitude()), targetCoordinate);

    // 거리가 200km 내에 존재하면 출발 허브-목적지 경로 반환
    if (route.distanceKm() <= 200) {
      HubId hubId = HubId.of(sourceHubId);
      return new ShippingPath(
          hubId,
          hubId,
          targetAddress,
          List.of(),
          shippingAgentAssigner.assignVendorShippingAgent(sourceHubId));
    }

    // 출발 허브에서 다른 모든 허브 경로 조회
    List<HubRouteResponse> hubRoutes = findRoutesFromSource(sourceHubId);

    // 출발 허브 - 다른 허브 - 목적지 경로가 최단 거리인 중간 허브 계산
    Double minDistance = Double.MAX_VALUE;
    HubRouteResponse shortestHubRoute = null;
    RouteResponse shortestRoute = null;
    for (HubRouteResponse hubRoute : hubRoutes) {
      // 다른 허브 ~ 목적지 경로 조회
      HubInfo start = findBy(hubRoute.targetHubId());
      RouteResponse tempRoute =
          routeCalculator.getRoute(
              Coordinate.of(start.latitude(), start.longitude()), targetCoordinate);

      // 출발 허브 - 다른 허브 - 목적지 거리 계산
      double totalDistance = hubRoute.distanceKm() + tempRoute.distanceKm();

      // 현재 최단 거리인 경우 갱신
      if (totalDistance < minDistance) {
        minDistance = totalDistance;
        shortestHubRoute = hubRoute;
        shortestRoute = tempRoute;
      }
    }

    // 최단 거리일 떄의 중간 허브 경로 생성
    ShippingSubPath subPath =
        new ShippingSubPath(
            1,
            HubId.of(sourceHubId),
            HubId.of(shortestHubRoute.targetHubId()),
            shortestHubRoute.distanceKm() + minDistance,
            shortestHubRoute.durationMinutes() + shortestRoute.durationMinutes(),
            shippingAgentAssigner.assignHubShippingAgent());

    return new ShippingPath(
        subPath.sourceHubId(),
        subPath.targetHubId(),
        targetAddress,
        List.of(subPath),
        shippingAgentAssigner.assignVendorShippingAgent(sourceHubId));
  }

  private List<HubRouteResponse> findRoutesFromSource(UUID sourceHubId) {
    return hubCacheService.getRoutes().stream()
        .filter(route -> route.sourceHubId().equals(sourceHubId))
        .toList();
  }

  private HubInfo findBy(UUID hubId) {
    return hubCacheService.getHubs().stream()
        .filter(hub -> hub.id().equals(hubId))
        .findFirst()
        .orElse(null);
  }
}
