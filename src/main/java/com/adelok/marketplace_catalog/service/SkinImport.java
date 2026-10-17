package com.adelok.marketplace_catalog.service;

import com.adelok.marketplace_catalog.dto.parsing.Cs2SkinDto;
import com.adelok.marketplace_catalog.entity.Skin;
import com.adelok.marketplace_catalog.repository.SkinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.util.List;


@Slf4j
@Service
public class SkinImport {
    private final String skinsUrl;
    private final SkinRepository skinRepository;
    private final RestClient restClient;
    private final ObjectMapper mapper;

    public SkinImport(
            @Value("${app.csgo-api.skins-url}") String skinsUrl,
            SkinRepository skinRepository,
            RestClient.Builder builder,
            ObjectMapper mapper){
        this.skinsUrl = skinsUrl;
        this.mapper = mapper;
        this.restClient = builder.build();
        this.skinRepository = skinRepository;
    }

    private List<Cs2SkinDto> downloadSkins() {
        String json = restClient.get().uri(skinsUrl).retrieve().body(String.class);
        return mapper.readValue(json, new TypeReference<List<Cs2SkinDto>>() {});

    }

    public void importSkins() {
        if (skinRepository.count() > 0) {
            log.info("already imported");
            return;
        }
        List<Cs2SkinDto> skins = downloadSkins();
        log.info("Downloaded {} skins", skins.size());
        List <Skin> entities = skins.stream().map(this::toEntity).toList();
        skinRepository.saveAll(entities);
        log.info("Imported {} skins", entities.size());
    }
    private Skin toEntity (Cs2SkinDto cs2SkinDto){
        return Skin.builder()
                .name(cs2SkinDto.name())
                .weapon(cs2SkinDto.weapon() != null ? cs2SkinDto.weapon().name() : null)
                .externalId(cs2SkinDto.id())
                .imageUrl(cs2SkinDto.image())
                .rarity(cs2SkinDto.rarity() != null ? cs2SkinDto.rarity().name() : null)
                .minFloat(cs2SkinDto.minFloat())
                .maxFloat(cs2SkinDto.maxFloat())
                .build();
    }
}
