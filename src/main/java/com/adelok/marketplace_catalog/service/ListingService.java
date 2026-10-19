package com.adelok.marketplace_catalog.service;

import com.adelok.marketplace_catalog.dto.listings.ListingDto;
import com.adelok.marketplace_catalog.dto.skins.SkinDto;
import com.adelok.marketplace_catalog.entity.Listing;
import com.adelok.marketplace_catalog.entity.Skin;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import com.adelok.marketplace_catalog.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;

    private SkinDto toSkinDto(Skin skin) {
        return new SkinDto(skin.getId(), skin.getName(), skin.getWeapon(), skin.getImageUrl(), skin.getRarity(), skin.getMinFloat(), skin.getMaxFloat());
    }

    private ListingDto toListingDto(Listing listing) {
        return new ListingDto(listing.getId(), listing.getPrice(), listing.getStatus(), toSkinDto(listing.getSkin()));
    }

    @Transactional(readOnly = true)
    public Page<ListingDto> getCatalog(ListingStatus status, Pageable pageable) {
        return listingRepository.findAllByStatus(status, pageable).map(this::toListingDto);
    }

    @Transactional(readOnly = true)
    public Page<ListingDto> getByWeapon(String weapon, ListingStatus status, Pageable pageable) {
        return listingRepository.findBySkin_WeaponAndStatus(weapon, status, pageable).map(this::toListingDto);
    }
@Transactional
    public void reserve(UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        listing.setStatus(ListingStatus.RESERVED);
        listingRepository.save(listing);
    }
    @Transactional
    public void unreserve(UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (listing.getStatus() != ListingStatus.RESERVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        listing.setStatus(ListingStatus.AVAILABLE);
        listingRepository.save(listing);
    }
}
