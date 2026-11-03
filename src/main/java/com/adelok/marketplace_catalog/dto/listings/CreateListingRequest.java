package com.adelok.marketplace_catalog.dto.listings;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateListingRequest(
        @NotNull
        UUID userItemId,
        @NotNull
        UUID sellerId,
        @NotNull
        @Positive
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal price
) {
}