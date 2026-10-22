package com.adelok.marketplace_catalog.dto.listings;

import com.adelok.marketplace_catalog.dto.skins.SkinDto;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ListingDto(
        UUID id,
        BigDecimal price,
        ListingStatus status,
        SkinDto skin,
        UUID sellerId,
        UUID ownerId
) {
}
