package com.athenhub.shippingservice.shipping.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 * 네이버 지도 Directions5 API의 자동차 경로 탐색 응답을 표현하는 DTO.
 *
 * <p>네이버 길찾기 API는 다양한 경로 옵션(traoptimal, tracomfort 등)을 반환할 수 있으며, 본 클래스는 그 중 대표 경로인 {@code
 * traoptimal} 데이터를 기반으로 거리 및 소요 시간을 제공한다.
 *
 * <p>{@code @JsonIgnoreProperties(ignoreUnknown = true)} 애너테이션을 통해, 응답 필드 구조가 변경되거나 추가되더라도 역직렬화 오류
 * 없이 필요한 필드만 안전하게 파싱할 수 있다.
 *
 * <p>주요 제공 정보:
 *
 * <ul>
 *   <li><b>getDistanceKm()</b> — 총 이동 거리(킬로미터 단위)
 *   <li><b>getDurationMinutes()</b> — 총 소요 시간(분 단위)
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverCoordinateResponse {
  private List<Address> addresses;

  public Double getLatitude() {
    return addresses.getFirst().getY();
  }

  public Double getLongitude() {
    return addresses.getLast().getX();
  }

  /**
   * 경로(route) 정보를 담는 최상위 필드.
   *
   * <p>네이버 Directions API는 여러 경로 옵션을 반환하지만, 본 구조에서는 {@code traoptimal}만 사용한다.
   */
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class Address {
    private Double x;
    private Double y;
  }
}
