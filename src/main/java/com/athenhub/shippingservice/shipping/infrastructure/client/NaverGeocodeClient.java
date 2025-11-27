package com.athenhub.shippingservice.shipping.infrastructure.client;

import com.athenhub.shippingservice.shipping.infrastructure.dto.NaverCoordinateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 네이버 지도 Directions5 API(자동차 길찾기)를 호출하는 Feign 클라이언트.
 *
 * <p>네이버 클라우드 플랫폼(NCP)의 지도 API Gateway를 통해 자동차 경로 탐색 정보를 조회한다. <br>
 * 해당 클라이언트는 API 호출 시 필요한 인증 값(X-NCP-APIGW-API-KEY-ID, X-NCP-APIGW-API-KEY) 을 {@link
 * NaverFeignConfig} 설정을 통해 자동으로 포함한다.
 *
 * <p>공식 문서: NCP Map Direction v5
 *
 * @author 김형섭
 * @since 1.0.0
 */
@FeignClient(
    name = "naver-geocode",
    url = "https://maps.apigw.ntruss.com/map-geocode/v2",
    configuration = NaverFeignConfig.class)
public interface NaverGeocodeClient {
  @GetMapping(
      value = "/geocode",
      headers = {"Accept=application/json"})
  NaverCoordinateResponse getCoordinate(
      @RequestParam("query") String query, @RequestParam("count") Integer count);
}
