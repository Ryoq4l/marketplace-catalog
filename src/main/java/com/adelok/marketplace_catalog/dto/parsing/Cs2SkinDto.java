package com.adelok.marketplace_catalog.dto.parsing;


import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import java.math.BigDecimal;
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Cs2SkinDto(
        String id,
        String name,
        Cs2Weapon weapon,
        String paintIndex,
        BigDecimal minFloat,
        BigDecimal maxFloat,
        SkinRarity rarity,
        String marketHashName,
        String image
) {
}
