package com.adelok.marketplace_catalog.repository;

import com.adelok.marketplace_catalog.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserItemRepository extends JpaRepository<UserItem, UUID> {
    List<UserItem> findAllByOwnerId(UUID ownerId);
}
