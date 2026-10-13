package com.adelok.marketplace_catalog.entity;

import com.adelok.marketplace_catalog.entity.enumerated.SkinStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Skin {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "weapon")
    private String weapon;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(name = "image_url", nullable = true, length = 700)
    private String imageUrl;

    @Column(name = "rarity")
    private String rarity;

    @Column(name = "min_float")
    private BigDecimal minFloat;

    @Column(name = "max_float")
    private BigDecimal maxFloat;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SkinStatus status = SkinStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
