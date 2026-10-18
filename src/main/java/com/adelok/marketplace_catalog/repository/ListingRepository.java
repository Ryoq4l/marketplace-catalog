package com.adelok.marketplace_catalog.repository;

import com.adelok.marketplace_catalog.entity.Listing;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import com.adelok.marketplace_catalog.service.ListingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
    Page<Listing> findAllBySellerId(UUID sellerId, Pageable pageable);
    Page<Listing> findAllByStatus(ListingStatus status, Pageable pageable);
    Page<Listing> findBySkin_WeaponAndStatus( String weapon,ListingStatus status, Pageable pageable);
}
