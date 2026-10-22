package com.adelok.marketplace_catalog.entity;

import com.adelok.marketplace_catalog.entity.enumerated.InventorySkinStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_item")
public class UserItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_id")
    private UUID ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id")
    private Skin skin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventorySkinStatus status;

}
