package com.athenhub.shippingservice.shipping.domain.service;

import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;
import java.util.UUID;

/**
 * 업체 관리 권한 여부를 확인하는 도메인 정책 인터페이스.
 *
 * <p>주어진 요청자가 특정 작업(예: 업체 등록)을 수행할 수 있는 권한을 보유하고 있는지 판단하기 위한 계약을 정의한다. 도메인 계층에서 권한 검사 로직을 추상화하여 서비스
 * 구현체에 의존하지 않도록 한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface PermissionChecker {
  /**
   * 주어진 요청자가 업체 등록 권한을 가지고 있는지 확인한다.
   *
   * @param requestId 권한 확인 대상 요청자 ID
   * @return 등록 권한이 있으면 {@code true}, 없으면 {@code false}
   */
  boolean hasCreatePermission(UUID requestId);

  /**
   * 주어진 요청자가 업체 수정 권한을 가지고 있는지 확인한다.
   *
   * @param requestId 권한 확인 대상 요청자 ID
   * @param hubId 권한 확인에 필요한 허브 식별자
   * @param agentId 업체 담당자 ID
   * @return 수정 권한이 있으면 {@code true}, 없으면 {@code false}
   */
  boolean hasUpdatePermission(UUID requestId, HubId hubId, VendorAgentId agentId);

  /**
   * 주어진 요청자가 업체 삭제 권한을 가지고 있는지 확인한다.
   *
   * @param requestId 권한 확인 대상 요청자 ID
   * @param hubId 권한 확인에 필요한 허브 식별자
   * @return 삭제 권한이 있으면 {@code true}, 없으면 {@code false}
   */
  boolean hasDeletePermission(UUID requestId, HubId hubId);
}
