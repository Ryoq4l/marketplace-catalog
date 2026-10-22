package com.adelok.marketplace_catalog.service;

import com.adelok.marketplace_catalog.dto.listings.CreateListingRequest;
import com.adelok.marketplace_catalog.dto.listings.ListingDto;
import com.adelok.marketplace_catalog.dto.skins.SkinDto;
import com.adelok.marketplace_catalog.entity.Listing;
import com.adelok.marketplace_catalog.entity.Skin;
import com.adelok.marketplace_catalog.entity.UserItem;
import com.adelok.marketplace_catalog.entity.enumerated.InventorySkinStatus;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import com.adelok.marketplace_catalog.repository.ListingRepository;
import com.adelok.marketplace_catalog.repository.SkinRepository;
import com.adelok.marketplace_catalog.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;
    private final SkinRepository skinRepository;
    @Value("${app.mock-seller-id}")
    UUID mockSellerId;
    private final UserItemRepository userItemRepository;

    private SkinDto toSkinDto(Skin skin) {
        return new SkinDto(skin.getId(), skin.getName(), skin.getWeapon(), skin.getImageUrl(), skin.getRarity(), skin.getMinFloat(), skin.getMaxFloat());
    }

    private ListingDto toListingDto(Listing listing) {
        return new ListingDto(listing.getId(), listing.getPrice(), listing.getStatus(), toSkinDto(listing.getUserItem().getSkin()), listing.getSellerId(), listing.getUserItem().getOwnerId());
    }

    @Transactional
    public void createListing(CreateListingRequest request) {
        UserItem userItem = userItemRepository.findById(request.userItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skin not found"));
        if (!userItem.getOwnerId().equals(request.sellerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont own this item");
        }
        Listing listing = Listing.builder()
                .sellerId(mockSellerId)
                .userItem(userItem)
                .status(ListingStatus.AVAILABLE)
                .price(request.price())
                .build();
        userItem.setStatus(InventorySkinStatus.ON_SALE);
        userItemRepository.save(userItem);
        listingRepository.save(listing);
    }

    @Transactional(readOnly = true)
    public Page<ListingDto> getCatalog(ListingStatus status, Pageable pageable) {
        return listingRepository.findAllByStatus(status, pageable).map(this::toListingDto);
    }

    @Transactional(readOnly = true)
    public Page<ListingDto> getByWeapon(String weapon, ListingStatus status, Pageable pageable) {
        return listingRepository.findByUserItem_Skin_WeaponAndStatus(weapon, status, pageable).map(this::toListingDto);
    }

    @Transactional(readOnly = true)
    public ListingDto getListingById(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return toListingDto(listing);
    }

    @Transactional
    public void markAsSold(UUID id, UUID buyerId) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (listing.getStatus() != ListingStatus.RESERVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        listing.setStatus(ListingStatus.SOLD);
        UserItem userItem = listing.getUserItem();
        userItem.setOwnerId(buyerId);
        userItem.setStatus(InventorySkinStatus.INVENTORY);
        userItemRepository.save(userItem);
        listingRepository.save(listing);
    }

    @Transactional
    public void markReserved(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cant reserve item already been reserved");
        }
        listing.setStatus(ListingStatus.RESERVED);
        listingRepository.save(listing);
    }

    @Transactional
    public void cancelReservation(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (listing.getStatus() != ListingStatus.RESERVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        listing.setStatus(ListingStatus.AVAILABLE);
        listingRepository.save(listing);
    }

    @Transactional
    public void sellToSys(UUID userItemId, UUID sellerId, BigDecimal price) {
        UserItem item = userItemRepository.findById(userItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        if (!item.getOwnerId().equals(sellerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        listingRepository.findByUserItemId(userItemId).ifPresent(listing -> {
            listingRepository.delete(listing);
            listingRepository.flush();
        });
        item.setOwnerId(mockSellerId);
        item.setStatus(InventorySkinStatus.ON_SALE);
        userItemRepository.save(item);

        Listing newListing = Listing.builder()
                .userItem(item)
                .sellerId(mockSellerId)
                .price(price)
                .status(ListingStatus.AVAILABLE)
                .build();
        listingRepository.save(newListing);
    }

}
