package com.adelok.marketplace_catalog.repository;

import com.adelok.marketplace_catalog.entity.Listing;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
    Page<Listing> findAllBySellerId(UUID sellerId, Pageable pageable);

    Page<Listing> findAllByStatus(ListingStatus status, Pageable pageable);

    Page<Listing> findByUserItem_Skin_WeaponAndStatus(String weapon, ListingStatus status, Pageable pageable);

    Optional<Listing> findByUserItemId(UUID userItemId);

    @Query("SELECT MIN(l.price) FROM Listing l WHERE l.userItem.skin.id = :skinId AND l.status = com.adelok.marketplace_catalog.entity.enumerated.ListingStatus.AVAILABLE")
    BigDecimal findMinPriceBySkinId(@Param("skinId") UUID skinId);

    @Query("SELECT l.price FROM Listing l WHERE l.userItem.skin.id = :skinId ORDER BY l.createdAt DESC LIMIT 1")
    BigDecimal findLastPriceBySkinId(@Param("skinId") UUID skinId);

    boolean existsByIdAndStatus(UUID id, ListingStatus status);
}
