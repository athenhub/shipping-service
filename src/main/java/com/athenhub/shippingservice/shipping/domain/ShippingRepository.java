package com.athenhub.shippingservice.shipping.domain;

import com.athenhub.shippingservice.shipping.domain.vo.ShippingId;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ShippingRepository extends Repository<Shipping, ShippingId> {
  Shipping save(Shipping shipping);

  Optional<Shipping> findById(ShippingId id);
}
