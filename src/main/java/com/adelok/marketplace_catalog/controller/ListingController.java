package com.adelok.marketplace_catalog.controller;

import com.adelok.marketplace_catalog.dto.listings.ListingDto;


import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import com.adelok.marketplace_catalog.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ListingController {
    private final ListingService listingService;

    @GetMapping
    public Page<ListingDto> getCatalog(Pageable pageable) {

        return listingService.getCatalog(ListingStatus.AVAILABLE, pageable);
    }
    @GetMapping("/search")
    public Page<ListingDto> getByWeapon(@RequestParam String weapon, Pageable pageable){
return listingService.getByWeapon(weapon, ListingStatus.AVAILABLE, pageable);
    }
}
