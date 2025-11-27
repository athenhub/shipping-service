package com.athenhub.shippingservice.shipping.infrastructure.dto;

import java.util.UUID;

/**
 * 허브(Hub) 정보를 나타내는 DTO.
 *
 * <p>외부 허브 서비스(hub-service)로부터 조회되는 허브 상세 정보를 담는다.
 *
 * @param id 허브 식별자(UUID)
 * @param name 허브 이름
 * @param streetAddress 기본 주소
 * @param detailAddress 상세 주소
 * @param latitude 경도
 * @param longitude 위도
 * @param isDeleted 논리 삭제 여부
 * @author 김형섭
 * @since 1.0.0
 */
public record HubInfo(
    UUID id,
    String name,
    String streetAddress,
    String detailAddress,
    Double latitude,
    Double longitude,
    boolean isDeleted) {}
