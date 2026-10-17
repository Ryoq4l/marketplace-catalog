package com.adelok.marketplace_catalog.dto.skins;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SkinDto(
        UUID id,
        String name,
        String weapon,
        String imageUrl,
        String rarity,
        BigDecimal minFloat,
        BigDecimal maxFloat
) {
}
