package com.adelok.marketplace_catalog.controller;

import com.adelok.marketplace_catalog.dto.InventoryItemDto;
import com.adelok.marketplace_catalog.dto.listings.CreateListingRequest;
import com.adelok.marketplace_catalog.dto.listings.ListingDto;


import com.adelok.marketplace_catalog.entity.Listing;
import com.adelok.marketplace_catalog.entity.UserItem;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import com.adelok.marketplace_catalog.repository.UserItemRepository;
import com.adelok.marketplace_catalog.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ListingController {
    private final ListingService listingService;
    private final UserItemRepository userItemRepository;

    @GetMapping
    public Page<ListingDto> getCatalog(Pageable pageable) {

        return listingService.getCatalog(ListingStatus.AVAILABLE, pageable);
    }

    @PostMapping
    public ResponseEntity<Void> createListing(
            @RequestBody CreateListingRequest request
    ){
        listingService.createListing(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/search")
    public Page<ListingDto> getByWeapon(@RequestParam String weapon, Pageable pageable) {
        return listingService.getByWeapon(weapon, ListingStatus.AVAILABLE, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(
            @PathVariable UUID id
    ){
      return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PostMapping("/{id}/sold")
    public void markSold(
            @PathVariable UUID id,
            @RequestParam UUID buyerId
    ) {
        listingService.markAsSold(id, buyerId);
    }
    @PostMapping("/{id}/reserve")
    public void markReserved(
            @PathVariable UUID id
    ) {
        listingService.markReserved(id);
    }
    @PostMapping("/{id}/cancel")
    public void cancelReservation(
            @PathVariable UUID id
    ){
        listingService.cancelReservation(id);
    }
    @PostMapping("/sell-to-system")
    public void sellToSystem(
            @RequestParam UUID userItemId,
            @RequestParam UUID sellerId,
            @RequestParam BigDecimal price
    ) {
        listingService.sellToSys(userItemId, sellerId, price);
    }
    @GetMapping("/my-inventory")
    public List<UserItem> getMyInventory(@RequestParam UUID ownerId) {
        return userItemRepository.findAllByOwnerId(ownerId);
    }
    @GetMapping("/inventory/{ownerId}")
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getInventory(@PathVariable UUID ownerId) {
        return userItemRepository.findAllByOwnerId(ownerId).stream()
                .map(item -> new InventoryItemDto(
                        item.getId(),
                        item.getSkin().getName(),
                        item.getSkin().getImageUrl(),
                        item.getSkin().getRarity(),
                        item.getSkin().getWeapon()
                ))
                .toList();
    }
}
