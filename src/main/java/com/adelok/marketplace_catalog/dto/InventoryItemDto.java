package com.adelok.marketplace_catalog.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryItemDto(
        UUID userItemId,
        String name,
        String imageUrl,
        String rarity,
        String weapon,
        BigDecimal buyoutPrice
) {}