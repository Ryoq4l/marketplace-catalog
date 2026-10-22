package com.adelok.marketplace_catalog.dto.listings;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateListingRequest(
        UUID userItemId,
        UUID sellerId,
        BigDecimal price
) {
}