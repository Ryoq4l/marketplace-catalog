package com.adelok.marketplace_catalog.repository;

import com.adelok.marketplace_catalog.entity.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkinRepository extends JpaRepository <Skin, UUID>{
}
