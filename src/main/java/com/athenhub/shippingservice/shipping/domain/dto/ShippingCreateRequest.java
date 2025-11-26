package com.athenhub.shippingservice.shipping.domain.dto;

import com.athenhub.shippingservice.shipping.domain.vo.ShippingPath;
import com.athenhub.vendorservice.vendor.domain.VendorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 업체 등록을 위한 요청 정보 DTO.
 *
 * <p>클라이언트가 Vendor를 신규 등록할 때 필요한 필드를 전달하는 데이터 구조이다. 필드별로 {@code @NotNull} 검증 애너테이션이 적용되어 있으며, 이는 등록
 * 요청 시 반드시 포함되어야 하는 필드를 의미한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>{@code name} — 업체명 (필수)
 *   <li>{@code hubId} — 소속 허브 ID (필수)
 *   <li>{@code type} — 업체 유형 (필수)
 *   <li>{@code streetAddress} — 기본 주소 (필수)
 *   <li>{@code detailAddress} — 상세 주소 (선택)
 *   <li>{@code latitude} — 위도 (필수)
 *   <li>{@code longitude} — 경도 (필수)
 *   <li>{@code agentId} - 담당자 ID (필수)
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ShippingCreateRequest(
    @NotNull UUID orderId,
    ShippingPath shippingPath,
    @NotNull UUID receiverId,
    @NotBlank String receiverName,
    @NotBlank String receiverSlackId,
    LocalDateTime shippingDueAt,
    String shippingRequestMemo) {}
